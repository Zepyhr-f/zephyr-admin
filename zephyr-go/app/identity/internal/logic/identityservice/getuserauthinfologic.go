package identityservicelogic

import (
	"context"

	"zephyr-go/app/identity/internal/svc"
	"zephyr-go/app/identity/pb"

	"github.com/zeromicro/go-zero/core/logx"
)

type GetUserAuthInfoLogic struct {
	ctx    context.Context
	svcCtx *svc.ServiceContext
	logx.Logger
}

func NewGetUserAuthInfoLogic(ctx context.Context, svcCtx *svc.ServiceContext) *GetUserAuthInfoLogic {
	return &GetUserAuthInfoLogic{
		ctx:    ctx,
		svcCtx: svcCtx,
		Logger: logx.WithContext(ctx),
	}
}

func (l *GetUserAuthInfoLogic) GetUserAuthInfo(in *pb.GetUserAuthInfoReq) (*pb.GetUserAuthInfoResp, error) {
	// todo: add your logic here and delete this line

	return &pb.GetUserAuthInfoResp{}, nil
}
