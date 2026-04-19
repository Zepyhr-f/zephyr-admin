import { Icon } from "@/components/icon";
import type { NavProps } from "@/components/nav";
import { Badge } from "@/ui/badge";

export const frontendNavData: NavProps["data"] = [
	{
		items: [
			{
				title: "sys.nav.workbench",
				path: "/workbench",
				icon: <Icon icon="local:ic-workbench" size="24" />,
			},
			{
				title: "sys.nav.analysis",
				path: "/analysis",
				icon: <Icon icon="local:ic-analysis" size="24" />,
			},
			{
				title: "sys.nav.user.index",
				path: "/management/user",
				icon: <Icon icon="solar:users-group-rounded-bold-duotone" size="24" />,
				children: [
					{
						title: "sys.nav.user.profile",
						path: "/management/user/profile",
					},
					{
						title: "sys.nav.user.account",
						path: "/management/user/account",
					},
				],
			},
			{
				title: "sys.nav.system.index",
				path: "/management/system",
				icon: <Icon icon="solar:settings-bold-duotone" size="24" />,
				children: [
					{
						title: "sys.nav.system.permission",
						path: "/management/system/permission",
					},
					{
						title: "sys.nav.system.role",
						path: "/management/system/role",
					},
					{
						title: "sys.nav.system.user",
						path: "/management/system/user",
					},
				],
			},
			{
				title: "sys.nav.components",
				path: "/components",
				icon: <Icon icon="solar:widget-5-bold-duotone" size="24" />,
				caption: "sys.nav.custom_ui_components",
				children: [
					{
						title: "sys.nav.icon",
						path: "/components/icon",
					},
					{
						title: "sys.nav.animate",
						path: "/components/animate",
					},
					{
						title: "sys.nav.scroll",
						path: "/components/scroll",
					},
					{
						title: "sys.nav.i18n",
						path: "/components/multi-language",
					},
					{
						title: "sys.nav.upload",
						path: "/components/upload",
					},
					{
						title: "sys.nav.chart",
						path: "/components/chart",
					},
					{
						title: "sys.nav.toast",
						path: "/components/toast",
					},
				],
			},
			{
				title: "sys.nav.functions",
				path: "/functions",
				icon: <Icon icon="solar:plain-2-bold-duotone" size="24" />,
				children: [
					{
						title: "sys.nav.clipboard",
						path: "/functions/clipboard",
					},
					{
						title: "sys.nav.token_expired",
						path: "/functions/token_expired",
					},
				],
			},
		],
	},
];


