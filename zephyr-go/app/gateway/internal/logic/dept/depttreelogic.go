// Code scaffolded by goctl. Safe to edit.
// goctl 1.10.1

package dept

import (
	"context"

	"zephyr-go/app/gateway/internal/svc"
	"zephyr-go/app/gateway/internal/types"
	"zephyr-go/app/identity/identityservice"

	"github.com/zeromicro/go-zero/core/logx"
)

type DeptTreeLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewDeptTreeLogic(ctx context.Context, svcCtx *svc.ServiceContext) *DeptTreeLogic {
	return &DeptTreeLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *DeptTreeLogic) DeptTree() (resp *types.DeptTreeResp, err error) {
	rpcResp, err := l.svcCtx.IdentityRpc.DeptTree(l.ctx, &identityservice.EmptyReq{})
	if err != nil {
		return nil, err
	}

	var list []types.DeptDetail
	for _, v := range rpcResp.List {
		list = append(list, mapDeptToTypes(v))
	}

	return &types.DeptTreeResp{
		List: list,
	}, nil
}

func mapDeptToTypes(v *identityservice.DeptDetail) types.DeptDetail {
	d := types.DeptDetail{
		Id:         v.Id,
		Code:       v.Code,
		ParentCode: v.ParentCode,
		Leaf:       v.Leaf,
		DeptName:   v.DeptName,
		OrderNum:   v.OrderNum,
		Status:     v.Status,
		CreateTime: v.CreateTime,
	}
	for _, child := range v.Children {
		d.Children = append(d.Children, mapDeptToTypes(child))
	}
	return d
}
