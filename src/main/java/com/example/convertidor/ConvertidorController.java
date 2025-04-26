package com.example.convertidor;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class ConvertidorController {

    @PostMapping("/convert")
    public ResponseEntity<?> convert(@RequestBody ConversionRequest request) {
        String url = request.getUrl();
        String format = request.getFormat();

        // Nombre único para el archivo
        String fileName = UUID.randomUUID().toString() + "." + format;
        File outputFile = new File("downloads", fileName); // carpeta downloads/

        try {
            // Asegura que la carpeta downloads existe
            outputFile.getParentFile().mkdirs();

            // Comando yt-dlp
            ProcessBuilder pb = new ProcessBuilder(
                    "yt-dlp",
                    "-f", "bestaudio",
                    "--extract-audio",
                    "--audio-format", format,
                    "-o", outputFile.getAbsolutePath(),
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
}
