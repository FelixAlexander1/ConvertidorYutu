# Usa openjdk como base
FROM openjdk:17-slim

# Instala Python, pip actualizado, ffmpeg, curl, yt-dlp actualizado
RUN apt-get update && \
    apt-get install -y python3 python3-pip ffmpeg curl && \
    python3 -m pip install --upgrade pip && \
    python3 -m pip install -U yt-dlp && \
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



