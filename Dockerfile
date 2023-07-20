FROM openjdk:17-alpine
RUN mkdir -p deploy
WORKDIR /deploy
COPY ./build/libs/*.jar nengtul.jar
ENTRYPOINT ["java", "-jar", "/deploy/nengtul.jar"]