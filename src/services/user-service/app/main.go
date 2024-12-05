package app

import (
	"user-service/gen/pb-go/src/services/user-service"
)

type UserService struct {
	db *DB
	user.UnimplementedUserServiceServer
}

func NewUserService(db *DB) UserService {
	return UserService{db: db}
}
