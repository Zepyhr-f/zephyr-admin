// Code scaffolded by goctl. Safe to edit.
// goctl 1.10.1

package menu

import (
	"context"

	"zephyr-go/app/identity/identityservice"
	"zephyr-go/app/gateway/internal/svc"
	"zephyr-go/app/gateway/internal/types"

	"github.com/zeromicro/go-zero/core/logx"
)

type MenuTreeLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewMenuTreeLogic(ctx context.Context, svcCtx *svc.ServiceContext) *MenuTreeLogic {
	return &MenuTreeLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *MenuTreeLogic) MenuTree() (resp *types.MenuTreeResp, err error) {
	rpcResp, err := l.svcCtx.IdentityRpc.MenuTree(l.ctx, &identityservice.EmptyReq{})
	if err != nil {
		return nil, err
	}

	var list []types.MenuDetail
	for _, item := range rpcResp.List {
		list = append(list, types.MenuDetail{
			MenuCode:   item.MenuCode,
			ParentCode: item.ParentCode,
			MenuName:   item.MenuName,
			Icon:       item.Icon,
			MenuType:   item.MenuType,
			Perms:      item.Perms,
			Path:       item.Path,
			Component:  item.Component,
			OrderNum:   item.OrderNum,
		})
	}

	return &types.MenuTreeResp{
		List: list,
	}, nil
}
