FROM openjdk:17-alpine

RUN apk update && apk add --no-cache python3 py3-pip ffmpeg curl ca-certificates && \
    pip3 install --upgrade yt-dlp

WORKDIR /app
COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package

EXPOSE 8080
CMD ["java", "-jar", "target/convertidor-0.0.1-SNAPSHOT.jar"]