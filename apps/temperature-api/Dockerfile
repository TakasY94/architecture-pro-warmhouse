# Базовый образ с Java 17 и Maven для сборки
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# Рабочая директория
WORKDIR /app

# Копирование Maven файлов и исходного кода
COPY pom.xml .
COPY src ./src

# Сборка приложения
RUN mvn clean package -DskipTests

# Финальный образ с Java 17
FROM openjdk:17-jdk-alpine

# Рабочая директория
WORKDIR /app

# Копирование JAR файла из builder
COPY --from=builder /app/target/*.jar app.jar

# Открытие порта
EXPOSE 8081

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]