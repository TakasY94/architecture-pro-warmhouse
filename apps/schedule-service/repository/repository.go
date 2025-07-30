package repository

import (
	"fmt"
	"schedule-service/models"

	"github.com/jinzhu/gorm"
)

type Repository struct {
	db *gorm.DB
}

func NewRepository(db *gorm.DB) *Repository {
	return &Repository{db: db}
}

func (r *Repository) CreateScenario(scenario *models.Scenario, deviceIDs []int64) error {
	tx := r.db.Begin()
	if tx.Error != nil {
		return tx.Error
	}

	// Создание сценария (исключаем ScenarioID, чтобы БД сама сгенерировала)
	if err := tx.Omit("scenario_id").Create(scenario).Error; err != nil {
		tx.Rollback()
		return err
	}

	// Добавление связанных устройств
	for _, deviceID := range deviceIDs {
		scenarioDevice := models.ScenarioDevice{
			ScenarioID: scenario.ScenarioID,
			DeviceID:   deviceID,
		}
		if err := tx.Create(&scenarioDevice).Error; err != nil {
			tx.Rollback()
			return err
		}
	}

	return tx.Commit().Error
}

func (r *Repository) GetScenario(scenarioID int64) (*models.Scenario, error) {
	var scenario models.Scenario
	if err := r.db.First(&scenario, "scenario_id = ?", scenarioID).Error; err != nil {
		if gorm.IsRecordNotFoundError(err) {
			return nil, fmt.Errorf("scenario not found")
		}
		return nil, err
	}

	// Получение связанных устройств
	var scenarioDevices []models.ScenarioDevice
	if err := r.db.Find(&scenarioDevices, "scenario_id = ?", scenarioID).Error; err != nil {
		return nil, err
	}

	for _, sd := range scenarioDevices {
		scenario.Devices = append(scenario.Devices, sd.DeviceID)
	}

	return &scenario, nil
}

func (r *Repository) UpdateScenario(scenarioID int64, update *models.ScenarioUpdate) (*models.Scenario, error) {
	tx := r.db.Begin()
	if tx.Error != nil {
		return nil, tx.Error
	}

	// Поиск сценария
	var scenario models.Scenario
	if err := tx.First(&scenario, "scenario_id = ?", scenarioID).Error; err != nil {
		tx.Rollback()
		if gorm.IsRecordNotFoundError(err) {
			return nil, fmt.Errorf("scenario not found")
		}
		return nil, err
	}

	// Обновление полей
	updates := make(map[string]interface{})
	if update.Name != nil {
		updates["name"] = *update.Name
	}
	if update.Description != nil {
		updates["description"] = *update.Description
	}
	if update.IsActive != nil {
		updates["is_active"] = *update.IsActive
	}

	if len(updates) > 0 {
		if err := tx.Model(&scenario).Updates(updates).Error; err != nil {
			tx.Rollback()
			return nil, err
		}
		// Обновляем объект в памяти
		if update.Name != nil {
			scenario.Name = *update.Name
		}
		if update.Description != nil {
			scenario.Description = *update.Description
		}
		if update.IsActive != nil {
			scenario.IsActive = *update.IsActive
		}
	}

	// Обновление связанных устройств
	if len(update.Devices) > 0 {
		// Удаление старых связей
		if err := tx.Delete(&models.ScenarioDevice{}, "scenario_id = ?", scenarioID).Error; err != nil {
			tx.Rollback()
			return nil, err
		}
		// Добавление новых связей
		for _, deviceID := range update.Devices {
			scenarioDevice := models.ScenarioDevice{
				ScenarioID: scenarioID,
				DeviceID:   deviceID,
			}
			if err := tx.Create(&scenarioDevice).Error; err != nil {
				tx.Rollback()
				return nil, err
			}
		}
		scenario.Devices = update.Devices
	} else {
		// Получение текущих устройств, если не обновлялись
		var scenarioDevices []models.ScenarioDevice
		if err := tx.Find(&scenarioDevices, "scenario_id = ?", scenarioID).Error; err != nil {
			tx.Rollback()
			return nil, err
		}
		for _, sd := range scenarioDevices {
			scenario.Devices = append(scenario.Devices, sd.DeviceID)
		}
	}

	return &scenario, tx.Commit().Error
}
