package models

import (
	"time"
)

type Scenario struct {
	ScenarioID  int64     `gorm:"primaryKey;column:scenario_id" json:"scenario_id"`
	UserID      int64     `gorm:"column:user_id" json:"user_id"`
	Name        string    `gorm:"column:name" json:"name"`
	Description string    `gorm:"column:description" json:"description"`
	IsActive    bool      `gorm:"column:is_active" json:"is_active"`
	Devices     []int64   `gorm:"-" json:"devices"` // Не хранится в таблице scenarios, обрабатывается через ScenarioDevice
	CreatedAt   time.Time `gorm:"column:created_at" json:"created_at"`
}

type ScenarioDevice struct {
	ScenarioID int64 `gorm:"primaryKey;column:scenario_id" json:"scenario_id"`
	DeviceID   int64 `gorm:"primaryKey;column:device_id" json:"device_id"`
}

type ScenarioCreate struct {
	UserID      int64   `json:"user_id" binding:"required"`
	Name        string  `json:"name" binding:"required"`
	Description string  `json:"description"`
	IsActive    bool    `json:"is_active" binding:"required"`
	Devices     []int64 `json:"devices"`
}

type ScenarioUpdate struct {
	Name        *string `json:"name"`
	Description *string `json:"description"`
	IsActive    *bool   `json:"is_active"`
	Devices     []int64 `json:"devices"`
}

type ErrorResponse struct {
	Error string `json:"error"`
}
