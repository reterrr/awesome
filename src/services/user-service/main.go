package user_service

import (
	common "awesome/src/services/common"
	"user-service/app"
	"user-service/config"
)

func main() {
	conf := config.GetConfig()
	service := &common.App{}

	service.Initialize(config.GetConfig())
	service.Run("tcp", ":8080")
}
