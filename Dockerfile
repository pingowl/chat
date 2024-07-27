FROM eclipse-temurin:17-jdk-jammy
ENV TZ=Asia/Seoul
RUN apt-get update && apt-get install -y tzdata
ARG JAR_PATH=./build/libs/*.jar
COPY ${JAR_PATH} app.jar
CMD ["java","-jar","app.jar"]