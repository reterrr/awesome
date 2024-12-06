package server

import (
	"context"
	"google.golang.org/grpc"
	"user-service/gen/pb-go/src/services/user-service"
)

type UserServiceServer struct {
	user.UnimplementedUserServiceServer
}

func (uss *UserServiceServer) CreateUser(ctx context.Context, request *user.CreateUserRequest) (*user.ApiResponse, error) {

}

func (uss *UserServiceServer) GetUser(ctx context.Context, request *user.GetUserRequest) (*user.ApiResponse, error) {

}

func (uss *UserServiceServer) LoginUser(ctx context.Context, request *user.LoginUserRequest) (*user.ApiResponse, error) {

}
