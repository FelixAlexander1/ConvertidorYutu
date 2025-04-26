package com.example.convertidor;
public class ConversionResponse {
    private String message;
    private boolean success;
    private String fileName; // NUEVO: nombre del archivo convertido

    public ConversionResponse(String message, boolean success, String fileName) {
        this.message = message;
        this.success = success;
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFileName() {
        return fileName;
    }
}
