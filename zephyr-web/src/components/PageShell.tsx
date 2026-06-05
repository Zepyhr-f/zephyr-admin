import { ReactNode } from "react";

export function PageShell(props: {
  title?: string; // 👈 改成可选！
  description?: string;
  extra?: ReactNode;
  children?: ReactNode;
}) {
  return (
    <div className="z-page">
      {/* 只有传了 title 才渲染头部 */}
      {props.title && (
        <div className="z-page__header">
          <div>
            <h1 className="z-page__title">{props.title}</h1>
            {props.description && (
              <p className="z-page__desc">{props.description}</p>
            )}
          </div>
          {props.extra}
        </div>
      )}

      {props.children}
    </div>
  );
}