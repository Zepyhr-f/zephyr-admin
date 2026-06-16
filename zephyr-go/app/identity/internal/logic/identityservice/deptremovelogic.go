package identityservicelogic

import (
	"context"

	"zephyr-go/app/identity/internal/svc"
	"zephyr-go/app/identity/pb"

	"github.com/zeromicro/go-zero/core/logx"
)

type DeptRemoveLogic struct {
	ctx    context.Context
	svcCtx *svc.ServiceContext
	logx.Logger
}

func NewDeptRemoveLogic(ctx context.Context, svcCtx *svc.ServiceContext) *DeptRemoveLogic {
	return &DeptRemoveLogic{
		ctx:    ctx,
		svcCtx: svcCtx,
		Logger: logx.WithContext(ctx),
	}
}

func (l *DeptRemoveLogic) DeptRemove(in *pb.DeptRemoveReq) (*pb.SuccessResp, error) {
	// todo: add your logic here and delete this line

	return &pb.SuccessResp{}, nil
}
