package db

import (
	"database/sql"
	"fmt"
	"log"
	"user-service/config"

	common "awesome/src/services/common"

	_ "github.com/lib/pq" // Import PostgreSQL driver
)

// GetPostgresConnection initializes and returns a PostgreSQL connection
func GetPostgresConnection() (*sql.DB, error) {
	// Retrieve the PostgreSQL configuration
	dbConfigProvider := &config.DbConfigProvider{}
	dbs := dbConfigProvider.Get()

	// Ensure the PostgreSQL configuration exists
	postgresConfig, exists := dbs["postgres"]
	if !exists {
		return nil, fmt.Errorf("postgres configuration not found")
	}

	// Generate the connection string
	connectionString, err := common.GenerateConnectionString(postgresConfig)
	if err != nil {
		return nil, fmt.Errorf("failed to generate PostgreSQL connection string: %v", err)
	}

	// Open a PostgreSQL connection
	db, err := sql.Open("postgres", connectionString)
	if err != nil {
		return nil, fmt.Errorf("failed to connect to PostgreSQL: %v", err)
	}

	// Test the connection
	if err := db.Ping(); err != nil {
		return nil, fmt.Errorf("failed to ping PostgreSQL: %v", err)
	}

	log.Println("Successfully connected to PostgreSQL")
	return db, nil
}
