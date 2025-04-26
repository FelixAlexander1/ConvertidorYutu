# Usa una imagen de Java más completa
FROM openjdk:17-jdk-slim-bullseye

# Instala Python3, pip, ffmpeg, certificados SSL y yt-dlp actualizado
RUN apt-get update && apt-get install -y \
    python3 python3-pip ffmpeg curl ca-certificates && \
    pip3 install --upgrade yt-dlp && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Directorio de trabajo
WORKDIR /app

# Copia el proyecto
COPY . .

# Permisos a mvnw
RUN chmod +x mvnw

# Compila el proyecto
RUN ./mvnw clean package

# Exponer puerto de Spring Boot
EXPOSE 8080

# Correr la app
CMD ["java", "-jar", "target/convertidor-0.0.1-SNAPSHOT.jar"]