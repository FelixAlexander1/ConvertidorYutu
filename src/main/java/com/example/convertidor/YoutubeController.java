/*package com.example.convertidor;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class YoutubeController {

    @GetMapping("/download")
    public ResponseEntity<?> downloadAudio(@RequestParam String url) {
        // Crear un nombre temporal único
        String fileName = UUID.randomUUID().toString() + ".mp3";
        File outputFile = new File(fileName);

        try {
            // Comando yt-dlp para descargar el audio
            ProcessBuilder pb = new ProcessBuilder(
                    "yt-dlp",
                    "-f", "bestaudio",
                    "--extract-audio",
                    "--audio-format", "mp3",
                    "-o", outputFile.getAbsolutePath(),
                    url
            );
            pb.inheritIO();
            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al descargar el audio");
            }

            // Devolver el archivo como respuesta
            FileSystemResource resource = new FileSystemResource(outputFile);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputFile.getName() + "\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error procesando la solicitud: " + e.getMessage());
        } finally {
            // Nota: podríamos borrar el archivo después si quieres, pero ahora no lo borramos para probar.
        }
    }
}*/
