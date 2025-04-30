FROM eclipse-temurin:17-jdk-jammy

# Устанавливаем tzdata и указываем часовой пояс для корректной работы @Scheduled по МСК
RUN rm -rf /etc/localtime
RUN ln -s /usr/share/zoneinfo/Europe/Moscow /etc/localtime
RUN echo "Europe/Moscow" > /etc/timezone

ARG JAR_FILE=build/libs/\*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]