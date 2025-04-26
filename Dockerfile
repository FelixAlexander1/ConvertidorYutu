# Usa una imagen de Python para instalar yt-dlp y las dependencias necesarias
FROM python:3.9-slim

# Actualiza los repositorios y asegúrate de que apt-get no falle
RUN apt-get update --fix-missing -y && \
    apt-get install -y python3-pip curl git && \
    pip3 install --upgrade pip && \
    pip3 install -U yt-dlp && \
    apt-get clean

# Instala OpenJDK 17 (más común y compatible)
RUN apt-get update && apt-get install -y openjdk-17-jdk

# Crea un directorio para la app
WORKDIR /app

# Copia el proyecto al contenedor
COPY . .

# Instala las dependencias de Maven y construye el proyecto
RUN ./mvnw clean package

# Expone el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/convertidor-0.0.1-SNAPSHOT.jar"]



