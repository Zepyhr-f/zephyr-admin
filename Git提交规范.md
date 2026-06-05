一、核心总则
小规模敏捷团队、持续迭代开发，核心规则：main主干永久可上线、禁止直推；所有开发走功能分支+PR合并；PR前必同步主干、本地解冲突。
二、分支命名规范
所有分支均从 main 拉出，单需求单分支，合并后立即删除，统一命名：

- 新功能：feature/功能名（例：feature/user-login）
- 缺陷修复：fix/问题描述（例：fix/list-sort-error）
- 线上热修：hotfix/问题简述（例：hotfix/pay-timeout）
- 文档修改：docs/文档内容

三、标准协作提交流程（固定步骤）
一套流程贯穿所有开发场景，简单高效、杜绝冲突。

1. 初始化：拉最新主干 + 创建分支
   git checkout main
   git pull
   git checkout -b feature/xxx
   说明：仅首次开发执行，一个分支对应一个需求，全程复用，无需重复建分支。
2. 日常开发：多次提交推送
   同一分支可无限次提交、推送，无需频繁同步main：
   git add .
   git commit -m "feat: 对应功能更新"
   git push origin feature/xxx
3. 提PR前置：必同步最新主干（关键）
   开发完成、准备提PR前，必须同步main最新代码，本地解决全部冲突，避免远端合并失败。
   一键同步命令：
   git checkout main && git pull && git checkout - && git merge main
   冲突解决后提交推送：
   git add .
   git commit -m "merge: 同步主干，解决代码冲突"
   git push origin feature/xxx
4. 合并收尾：评审通过 + 清理分支
5. 提交PR，完成同事代码评审、CI校验
6. 合并至main主干
7. 删除本地、远程对应功能分支，避免堆积

四、Commit 提交规范（强制统一）
禁止 update、修改代码、fix bug 等无意义提交，统一格式：type: 简短中文描述

1. 提交类型定义

- feat：新增业务功能
- fix：修复日常开发bug
- hotfix：修复线上紧急问题
- docs：注释、文档内容调整
- refactor：代码重构，无功能变更
- style：代码格式、缩进优化
- perf：代码性能优化Refining Login Interface Aesthetics
- chore：依赖、配置、构建脚本调整

2. 规范示例

- feat: 新增手机号登录功能
- fix: 修复列表分页重复加载问题
- hotfix: 修复线上支付超时异常
- refactor: 重构用户信息查询逻辑

五、核心红线规则

- 严禁直接push、force push到main主干
- 严禁未同步主干、带冲突提交PR
- 严禁超大批次代码合并、长期留存闲置分支
