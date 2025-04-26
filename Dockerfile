# Usa openjdk como base
FROM openjdk:17-slim

# Instala Python, pip, ffmpeg (yt-dlp depende de ffmpeg) y yt-dlp
RUN apt-get update && apt-get install -y python3 python3-pip ffmpeg curl && \
    pip3 install yt-dlp && \
    apt-get clean

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


