package config

import (
	"os"
)

type Config struct {
	DB     []*DBConfig
	Server *ServerConfig
}

type DBConfig struct {
	Dialect string
	Host    string
	Port    string
	User    string
	Pass    string
	Name    string
	Charset string
}

type ServerConfig struct {
	Port string
	Type string
}

func GetConfig() *Config {
	return &Config{
		DB: []*DBConfig{
			{
				Dialect: os.Getenv("SQL_DIALECT"),
				Host:    os.Getenv("POSTGRES_HOST"),
				Port:    os.Getenv("POSTGRES_PORT"),
				User:    os.Getenv("POSTGRES_USER"),
				Pass:    os.Getenv("POSTGRES_PASSWORD"),
				Name:    os.Getenv("POSTGRES_SCHEME"),
				Charset: os.Getenv("CHARSET"),
			},
		},
		Server: &ServerConfig{
			Port: os.Getenv("SERVER_PORT"),
			Type: os.Getenv("SERVER_TYPE"),
		},
	}
}
