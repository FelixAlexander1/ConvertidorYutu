package com.example.convertidor;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@RestController
public class ConvertidorController {

    @PostMapping("/convert")
    public ResponseEntity<?> convert(@RequestBody ConversionRequest request) {
        String url = request.getUrl();
        String format = request.getFormat();

        String fileName = UUID.randomUUID().toString() + "." + format;
        File outputFile = new File(System.getProperty("java.io.tmpdir"), fileName);

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "yt-dlp",
                    "--geo-bypass",
                    "--no-check-certificate",
                    "-f", "bestaudio",
                    "--extract-audio",
                    "--audio-format", format,
                    "-o", outputFile.getAbsolutePath(),
                    url
            );

            // Capturamos salida y errores para debug
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Leemos la salida del proceso
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line); // Log de yt-dlp
                }
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ConversionResponse("Error al convertir el video", false, null));
            }

            return ResponseEntity.ok(new ConversionResponse("Conversión exitosa", true, fileName));

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ConversionResponse("Error en el servidor: " + e.getMessage(), false, null));
        }
    }

    @GetMapping("/download")
    public ResponseEntity<?> download(@RequestParam String file) {
        try {
            Path tempFilePath = Path.of(System.getProperty("java.io.tmpdir"), file);
            if (Files.exists(tempFilePath)) {
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=\"" + file + "\"")
                        .body(Files.readAllBytes(tempFilePath));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Archivo no encontrado");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al intentar descargar el archivo.");
        }
    }
}
