package identityservicelogic

import (
	"context"

	"zephyr-go/app/identity/internal/repository/model"
	"zephyr-go/app/identity/internal/svc"
	"zephyr-go/app/identity/pb"

	"github.com/zeromicro/go-zero/core/logx"
)

type MenuTreeLogic struct {
	ctx    context.Context
	svcCtx *svc.ServiceContext
	logx.Logger
}

func NewMenuTreeLogic(ctx context.Context, svcCtx *svc.ServiceContext) *MenuTreeLogic {
	return &MenuTreeLogic{
		ctx:    ctx,
		svcCtx: svcCtx,
		Logger: logx.WithContext(ctx),
	}
}

// Menu
func (l *MenuTreeLogic) MenuTree(in *pb.EmptyReq) (*pb.MenuTreeResp, error) {
	var menus []model.SysMenu
	err := l.svcCtx.DB.Order("order_num asc").Find(&menus).Error
	if err != nil {
		return nil, err
	}

	var list []*pb.MenuDetail
	for _, menu := range menus {
		list = append(list, &pb.MenuDetail{
			MenuCode:   menu.Code,
			ParentCode: menu.ParentCode,
			MenuName:   menu.MenuName,
			Icon:       menu.Icon,
			MenuType:   menu.MenuType,
			Perms:      menu.Perms,
			Path:       menu.Path,
			Component:  menu.Component,
			OrderNum:   int32(menu.OrderNum),
		})
	}

	return &pb.MenuTreeResp{
		List: list,
	}, nil
}
