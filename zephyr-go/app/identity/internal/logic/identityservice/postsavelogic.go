package identityservicelogic

import (
	"context"

	"zephyr-go/app/identity/internal/svc"
	"zephyr-go/app/identity/pb"

	"github.com/zeromicro/go-zero/core/logx"
)

type PostSaveLogic struct {
	ctx    context.Context
	svcCtx *svc.ServiceContext
	logx.Logger
}

func NewPostSaveLogic(ctx context.Context, svcCtx *svc.ServiceContext) *PostSaveLogic {
	return &PostSaveLogic{
		ctx:    ctx,
		svcCtx: svcCtx,
		Logger: logx.WithContext(ctx),
	}
}

func (l *PostSaveLogic) PostSave(in *pb.PostSaveReq) (*pb.SuccessResp, error) {
	// todo: add your logic here and delete this line

	return &pb.SuccessResp{}, nil
}
