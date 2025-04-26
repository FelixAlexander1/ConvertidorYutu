FROM openjdk:25-slim

# Instala Python3, pip, ffmpeg, certificados SSL y yt-dlp actualizado
RUN apt-get update && \
    apt-get install -y python3 python3-pip ffmpeg curl ca-certificates && \
    pip3 install --upgrade yt-dlp && \
    apt-get clean

# Directorio de trabajo
WORKDIR /app

# Copia el proyecto
COPY . .

# Permisos para mvnw
RUN chmod +x mvnw

# Compilar la app
RUN ./mvnw clean package

# Exponer el puerto
EXPOSE 8080

# Comando de inicio
CMD ["java", "-jar", "target/convertidor-0.0.1-SNAPSHOT.jar"]




