FROM openjdk:latest
ADD target/notifier-latest.jar notifier.jar
EXPOSE 9093/tcp
ENTRYPOINT ["java", "-jar", "notifier.jar"]