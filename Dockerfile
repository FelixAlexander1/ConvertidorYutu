# Usa una imagen de Java
FROM openjdk:21-jdk-slim

# Instalar dependencias necesarias y yt-dlp
RUN apt-get update --fix-missing -y && \
    apt-get install -y python3-pip curl git && \
    pip3 install --upgrade pip && \
    pip3 install -U yt-dlp && \
    apt-get clean

# Crea un directorio para la app
WORKDIR /app

# Copia todo el proyecto al contenedor
COPY . .

# Construye el proyecto
RUN ./mvnw clean package

# Expone el puerto que usa tu aplicación Spring Boot (por defecto 8080)
EXPOSE 8080

# Comando para correr tu app
CMD ["java", "-jar", "target/convertidor-0.0.1-SNAPSHOT.jar"]


