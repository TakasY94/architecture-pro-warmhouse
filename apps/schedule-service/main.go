package main

import (
	"log"
	config2 "schedule-service/config"
	"schedule-service/handlers"
	"schedule-service/repository"

	"github.com/gin-gonic/gin"
)

func main() {
	config, err := config2.NewConfig()
	if err != nil {
		log.Fatalf("Failed to initialize config: %v", err)
	}
	defer config.DB.Close()

	repo := repository.NewRepository(config.DB)
	handler := handlers.NewHandler(repo)

	r := gin.Default()
	r.POST("/scenarios", handler.CreateScenario)
	r.GET("/scenarios/:scenarioId", handler.GetScenario)
	r.PATCH("/scenarios/:scenarioId", handler.UpdateScenario)

	if err := r.Run(":8082"); err != nil {
		log.Fatalf("Failed to run server: %v", err)
	}
}
