package server

import (
	"context"
	"google.golang.org/grpc"
	"user-service/app/db"
	"user-service/gen/pb-go/src/services/user-service"
)

type UserServiceServer struct {
	user.UnimplementedUserServiceServer
}

var postgresConnection, _ = db.GetPostgresConnection()

func (uss *UserServiceServer) CreateUser(ctx context.Context, request *user.CreateUserRequest) (*user.ApiResponse, error) {
	if request.CheckPassword != request.Password {
		return &user.ApiResponse{StatusCode: 400, Message: "Passwords must be identical"}, nil
	}

}

func (uss *UserServiceServer) GetUser(ctx context.Context, request *user.GetUserRequest) (*user.ApiResponse, error) {

}

func (uss *UserServiceServer) LoginUser(ctx context.Context, request *user.LoginUserRequest) (*user.ApiResponse, error) {

}
