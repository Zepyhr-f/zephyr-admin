import { create } from "zustand";

interface UserInfo {
    userCode: string;
    username: string;
}

interface AuthState {
    token: string | null;
    isAuthenticated: boolean;
    user: UserInfo | null;
    roles: string[];
    permissions: string[];
    menus: MenuItem[] | null;

    setToken: (token: string) => void;
    setUserInfo: (user: UserInfo, roles: string[], permissions: string[]) => void;
    setMenus: (menus: MenuItem[]) => void;
    clearAuth: () => void;
    logout: () => void;
}

export interface MenuItem {
    path: string;
    component?: string;
    name?: string;
    meta?: {
        title?: string;
        icon?: string;
    };
    children?: MenuItem[];
}

export const useAuthStore = create<AuthState>()((set) => ({
    token: null,
    isAuthenticated: false,
    user: null,
    roles: [],
    permissions: [],
    menus: null,

    setToken: (token: string) => set({
        token,
        isAuthenticated: true,
    }),

    setUserInfo: (user: UserInfo, roles: string[], permissions: string[]) => set({
        user,
        roles,
        permissions,
    }),

    setMenus: (menus: MenuItem[]) => set({
        menus,
    }),

    clearAuth: () => set({
        token: null,
        isAuthenticated: false,
        user: null,
        roles: [],
        permissions: [],
        menus: null,
    }),

    logout: () => {
        set({
            token: null,
            isAuthenticated: false,
            user: null,
            roles: [],
            permissions: [],
            menus: null,
        });
    },
}));
