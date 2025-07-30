package handlers

import (
	"fmt"
	"net/http"
	"schedule-service/models"
	"schedule-service/repository"
	"strconv"
	"time"

	"github.com/gin-gonic/gin"
)

type Handler struct {
	repo *repository.Repository
}

func NewHandler(repo *repository.Repository) *Handler {
	return &Handler{repo: repo}
}

func (h *Handler) CreateScenario(c *gin.Context) {
	var input models.ScenarioCreate
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, models.ErrorResponse{Error: "Invalid request data"})
		return
	}

	scenario := &models.Scenario{
		UserID:      input.UserID,
		Name:        input.Name,
		Description: input.Description,
		IsActive:    input.IsActive,
		CreatedAt:   time.Now(),
		// ScenarioID не задаём - БД сама сгенерирует
	}

	if err := h.repo.CreateScenario(scenario, input.Devices); err != nil {
		c.JSON(http.StatusInternalServerError, models.ErrorResponse{Error: "Internal server error"})
		return
	}

	scenario.Devices = input.Devices
	c.Header("Location", fmt.Sprintf("/scenarios/%d", scenario.ScenarioID))
	c.JSON(http.StatusCreated, scenario)
}

func (h *Handler) GetScenario(c *gin.Context) {
	scenarioID, err := strconv.ParseInt(c.Param("scenarioId"), 10, 64)
	if err != nil {
		c.JSON(http.StatusBadRequest, models.ErrorResponse{Error: "Invalid scenario ID"})
		return
	}

	scenario, err := h.repo.GetScenario(scenarioID)
	if err != nil {
		if err.Error() == "scenario not found" {
			c.JSON(http.StatusNotFound, models.ErrorResponse{Error: "Scenario not found"})
			return
		}
		c.JSON(http.StatusInternalServerError, models.ErrorResponse{Error: "Internal server error"})
		return
	}

	c.JSON(http.StatusOK, scenario)
}

func (h *Handler) UpdateScenario(c *gin.Context) {
	scenarioID, err := strconv.ParseInt(c.Param("scenarioId"), 10, 64)
	if err != nil {
		c.JSON(http.StatusBadRequest, models.ErrorResponse{Error: "Invalid scenario ID"})
		return
	}

	var input models.ScenarioUpdate
	if err := c.ShouldBindJSON(&input); err != nil {
		c.JSON(http.StatusBadRequest, models.ErrorResponse{Error: "Invalid request data"})
		return
	}

	// Проверка, что хотя бы одно поле указано
	if input.Name == nil && input.Description == nil && input.IsActive == nil && len(input.Devices) == 0 {
		c.JSON(http.StatusBadRequest, models.ErrorResponse{Error: "At least one field must be provided"})
		return
	}

	scenario, err := h.repo.UpdateScenario(scenarioID, &input)
	if err != nil {
		if err.Error() == "scenario not found" {
			c.JSON(http.StatusNotFound, models.ErrorResponse{Error: "Scenario not found"})
			return
		}
		c.JSON(http.StatusInternalServerError, models.ErrorResponse{Error: "Internal server error"})
		return
	}

	c.JSON(http.StatusOK, scenario)
}
