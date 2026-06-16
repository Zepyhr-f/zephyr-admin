package identityservicelogic

import (
	"context"

	"zephyr-go/app/identity/internal/svc"
	"zephyr-go/app/identity/pb"

	"github.com/zeromicro/go-zero/core/logx"
)

type DeptSaveLogic struct {
	ctx    context.Context
	svcCtx *svc.ServiceContext
	logx.Logger
}

func NewDeptSaveLogic(ctx context.Context, svcCtx *svc.ServiceContext) *DeptSaveLogic {
	return &DeptSaveLogic{
		ctx:    ctx,
		svcCtx: svcCtx,
		Logger: logx.WithContext(ctx),
	}
}

func (l *DeptSaveLogic) DeptSave(in *pb.DeptSaveReq) (*pb.SuccessResp, error) {
	// todo: add your logic here and delete this line

	return &pb.SuccessResp{}, nil
}
