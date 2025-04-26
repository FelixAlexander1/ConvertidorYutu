package com.example.convertidor;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class DownloadController {

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam String file) {
        File outputFile = new File("downloads", file);

        if (!outputFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Archivo no encontrado");
        }

        FileSystemResource resource = new FileSystemResource(outputFile);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputFile.getName() + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(outputFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
