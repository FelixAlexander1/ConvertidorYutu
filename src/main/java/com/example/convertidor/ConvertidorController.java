package com.example.convertidor;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@RestController
public class ConvertidorController {

    @PostMapping("/convert")
    public ResponseEntity<?> convert(@RequestBody ConversionRequest request) {
        String url = request.getUrl();
        String format = request.getFormat();

        // Nombre único para el archivo
        String fileName = UUID.randomUUID().toString() + "." + format;
        File outputFile = new File(System.getProperty("java.io.tmpdir"), fileName); // Usar un directorio temporal

        try {
            // Comando yt-dlp
            ProcessBuilder pb = new ProcessBuilder(
                "yt-dlp",
                "-f", "bestaudio",
                "--extract-audio",
                "--audio-format", format,
                "-o", outputFile.getAbsolutePath(),
                "--user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36",
                url
            );
            pb.inheritIO();
            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ConversionResponse("Error al convertir el video", false, null));
            }

            // Devolver el nombre del archivo para poder descargarlo luego
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
                // Hacer que el archivo se descargue directamente
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