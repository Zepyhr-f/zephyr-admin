package identityservicelogic

import (
	"context"

	"zephyr-go/app/identity/internal/svc"
	"zephyr-go/app/identity/pb"

	"github.com/zeromicro/go-zero/core/logx"
)

type PostPageLogic struct {
	ctx    context.Context
	svcCtx *svc.ServiceContext
	logx.Logger
}

func NewPostPageLogic(ctx context.Context, svcCtx *svc.ServiceContext) *PostPageLogic {
	return &PostPageLogic{
		ctx:    ctx,
		svcCtx: svcCtx,
		Logger: logx.WithContext(ctx),
	}
}

// Post
func (l *PostPageLogic) PostPage(in *pb.PostPageReq) (*pb.PostPageResp, error) {
	// todo: add your logic here and delete this line

	return &pb.PostPageResp{}, nil
}
