import { useMemo, useState } from "react";
import { Button, Checkbox, Divider, Form, Input, Space, Typography, message } from "antd";
import { LockOutlined, SafetyCertificateOutlined, UserOutlined } from "@ant-design/icons";
import { useLocation, useNavigate } from "react-router-dom";
import { loginDemo } from "@/app/auth";
import "./login.css";

type FormValues = {
  username: string;
  password: string;
  remember: boolean;
};

export function LoginPage() {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const redirectTo = useMemo(() => {
    const s = location.state as { from?: string } | null;
    return s?.from || "/";
  }, [location.state]);

  async function onFinish(values: FormValues) {
    setLoading(true);
    try {
      // demo：做一点“真实感”的延迟
      await new Promise((r) => setTimeout(r, 450));

      if (!values.username || !values.password) {
        message.error("请输入账号与密码");
        return;
      }

      loginDemo(values.username);
      message.success("登录成功");
      navigate(redirectTo, { replace: true });
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="z-auth">
      <section className="z-auth__brand" aria-label="产品介绍">
        <div className="z-auth__brandInner">
          <div className="z-auth__logo" aria-hidden="true" />
          <h1 className="z-auth__title">Zephyr 后台管理</h1>
          <p className="z-auth__subtitle">
            面向 RBAC、审计、监控场景的企业级后台模板。更快定位问题、更稳地做权限、让运维与合规协作更顺滑。
          </p>

          <div className="z-auth__kpis" aria-label="亮点">
            <div className="z-auth__kpi">
              <div className="z-auth__kpiLabel">权限颗粒度</div>
              <div className="z-auth__kpiValue">按钮级</div>
            </div>
            <div className="z-auth__kpi">
              <div className="z-auth__kpiLabel">审计追踪</div>
              <div className="z-auth__kpiValue">全链路</div>
            </div>
            <div className="z-auth__kpi">
              <div className="z-auth__kpiLabel">监控面板</div>
              <div className="z-auth__kpiValue">可扩展</div>
            </div>
          </div>
        </div>
      </section>

      <section className="z-auth__cardWrap" aria-label="登录表单">
        <div className="z-auth__card">
          <h2 className="z-auth__cardTitle">欢迎回来</h2>
          <p className="z-auth__cardDesc">请使用你的账号登录。演示环境：任意账号/密码均可。</p>

          <Form<FormValues>
            layout="vertical"
            requiredMark={false}
            initialValues={{ username: "admin", password: "123456", remember: true }}
            onFinish={onFinish}
          >
            <Form.Item
              name="username"
              label="账号"
              rules={[{ required: true, message: "请输入账号" }]}
            >
              <Input
                size="large"
                autoFocus
                autoComplete="username"
                prefix={<UserOutlined />}
                placeholder="请输入账号"
              />
            </Form.Item>

            <Form.Item
              name="password"
              label="密码"
              rules={[{ required: true, message: "请输入密码" }]}
            >
              <Input.Password
                size="large"
                autoComplete="current-password"
                prefix={<LockOutlined />}
                placeholder="请输入密码"
              />
            </Form.Item>

            <div className="z-auth__helperRow">
              <Form.Item name="remember" valuePropName="checked" style={{ marginBottom: 0 }}>
                <Checkbox>记住我</Checkbox>
              </Form.Item>
              <Typography.Link onClick={() => message.info("可对接“重置密码/短信/邮箱验证码”流程")}>
                忘记密码？
              </Typography.Link>
            </div>

            <Form.Item style={{ marginTop: 16 }}>
              <Button
                type="primary"
                htmlType="submit"
                size="large"
                loading={loading}
                block
                icon={<SafetyCertificateOutlined />}
              >
                登录
              </Button>
            </Form.Item>
          </Form>

          <Divider style={{ margin: "14px 0" }} />
          <Space direction="vertical" style={{ width: "100%" }}>
            <Button block onClick={() => message.info("可扩展：SSO / LDAP / OAuth / 钉钉/飞书登录")}>
              其他登录方式（占位）
            </Button>
          </Space>

          <div className="z-auth__footer">
            <span>登录即表示你同意</span>{" "}
            <Typography.Link onClick={() => message.info("可对接：用户协议")}>用户协议</Typography.Link>{" "}
            <span>与</span>{" "}
            <Typography.Link onClick={() => message.info("可对接：隐私政策")}>隐私政策</Typography.Link>
          </div>
        </div>
      </section>
    </div>
  );
}

