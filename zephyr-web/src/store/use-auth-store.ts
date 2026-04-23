import { create } from "zustand";
import { persist, createJSONStorage } from "zustand/middleware";

interface AuthState {
    token: string | null;
    refreshToken: string | null;
    isAuthenticated: boolean;

    setToken: (accessToken: string, refreshToken: string ) => void;
    clearAuth: () => void;
    logout: () => void;
}

export const useAuthStore = create<AuthState>()(
    persist(
        (set) => ({
            token: null,
            refreshToken: null,
            isAuthenticated: false,

            setToken: (token: string, refreshToken: string ) => set({
                token,
                refreshToken,
                isAuthenticated: true,
            }),

            clearAuth: () => set({
                    token: null,
                    refreshToken: null,
                    isAuthenticated: false
            }),

            logout: () => {
                set({
                    token: null,
                    refreshToken: null,
                    isAuthenticated: false
                });
                localStorage.removeItem("zephyr-auth-storage");
            },
        }),
        {
            name: "zephyr-auth-storage",
            storage: createJSONStorage(() => localStorage),
            partialize: (state: AuthState) => ({
                token: state.token,
                refreshToken: state.refreshToken,
                isAuthenticated: state.isAuthenticated,
            }),
        }
    )
);