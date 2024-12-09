package common

import (
	"fmt"
)

type ServerConfigProvider interface {
	Get() *ServerConfig
}

type ClientConfigProvider interface {
	Get() *ClientConfig
}

type DbConfigProvider interface {
	Get() map[string]*DBConfig
}

type AppProvider interface {
	ApplyRouting()
}

type ServerConfig struct {
	Port string //port to listen
	Type string
}

type ClientConfig struct {
	Port string //port to ask
	Type string
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

func GenerateConnectionString(dbConfig *DBConfig) (string, error) {
	switch dbConfig.Dialect {
	case "postgres":
		return fmt.Sprintf(
			"host=%s port=%s user=%s password=%s dbname=%s sslmode=disable",
			dbConfig.Host,
			dbConfig.Port,
			dbConfig.User,
			dbConfig.Pass,
			dbConfig.Name,
		), nil
	case "redis":
		if dbConfig.Pass != "" {
			return fmt.Sprintf("redis://:%s@%s:%s", dbConfig.Pass, dbConfig.Host, dbConfig.Port), nil
		}
		return fmt.Sprintf("redis://%s:%s", dbConfig.Host, dbConfig.Port), nil
	case "mongo":
		if dbConfig.User != "" && dbConfig.Pass != "" {
			return fmt.Sprintf(
				"mongodb://%s:%s@%s:%s/%s",
				dbConfig.User,
				dbConfig.Pass,
				dbConfig.Host,
				dbConfig.Port,
				dbConfig.Name,
			), nil
		}
		return fmt.Sprintf("mongodb://%s:%s/%s", dbConfig.Host, dbConfig.Port, dbConfig.Name), nil
	default:
		return "", fmt.Errorf("unsupported dialect: %s", dbConfig.Dialect)
	}
}
