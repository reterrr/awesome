package config

import (
	"awesome/src/services/common"
	"os"
)

type ServerConfigProvider struct{}
type ClientConfigProvider struct{}
type DbConfigProvider struct{}

func (s *ServerConfigProvider) Get() *common.ServerConfig {
	return &common.ServerConfig{
		Port: os.Getenv("SERVER_PORT"),
		Type: os.Getenv("SERVER_TYPE"),
	}
}

func (c *ClientConfigProvider) Get() *common.ClientConfig {
	return &common.ClientConfig{
		Port: os.Getenv("CLIENT_PORT"),
		Type: os.Getenv("CLIENT_TYPE"),
	}
}

func (db *DbConfigProvider) Get() map[string]*common.DBConfig {
	return map[string]*common.DBConfig{
		"postgres": {
			Dialect: os.Getenv("POSTGRES_DIALECT"),
			Host:    os.Getenv("POSTGRES_HOST"),
			Port:    os.Getenv("POSTGRES_PORT"),
			User:    os.Getenv("POSTGRES_USER"),
			Pass:    os.Getenv("POSTGRES_PASSWORD"),
			Name:    os.Getenv("POSTGRES_SCHEME"),
			Charset: os.Getenv("CHARSET"),
		},
	}
}
