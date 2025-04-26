FROM openjdk:25-slim


RUN apt-get update && apt-get install -y python3 python3-pip ffmpeg curl ca-certificates && \
    pip3 install --upgrade yt-dlp && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean package

EXPOSE 8080

CMD ["java", "-jar", "target/convertidor-0.0.1-SNAPSHOT.jar"]