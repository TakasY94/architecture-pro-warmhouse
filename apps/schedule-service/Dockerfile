
# Базовый образ для сборки
FROM golang:1.24-alpine AS builder

# Рабочая директория
WORKDIR /build

ADD go.mod .

# Копирование исходного кода
COPY . .

# Сборка приложения
RUN go mod download
RUN CGO_ENABLED=0 GOOS=linux go build -o schedule_service main.go

# Финальный образ
FROM alpine:latest

# Рабочая директория
WORKDIR /build

# Копирование собранного бинарника
COPY --from=builder /build/schedule_service .

# Открытие порта
EXPOSE 8082

# Команда для запуска приложения
CMD ["./schedule_service"]