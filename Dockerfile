FROM openjdk:17-alpine
RUN mkdir -p deploy
WORKDIR /deploy
COPY ./build/libs/*.jar nengtul.jar
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "/deploy/nengtul.jar"]