import { create } from "zustand";
import { persist } from "zustand/middleware";

type ThemeMode = "light" | "dark" | "auto";

interface ThemeState {
  mode: ThemeMode;
  isDark: boolean;
  setMode: (mode: ThemeMode) => void;
  toggleTheme: () => void;
}

function resolveDark(mode: ThemeMode): boolean {
  if (mode === "dark") return true;
  if (mode === "light") return false;
  return window.matchMedia("(prefers-color-scheme: dark)").matches;
}

function applyTheme(isDark: boolean) {
  const root = document.documentElement;
  if (isDark) {
    root.setAttribute("data-theme", "dark");
  } else {
    root.removeAttribute("data-theme");
  }
}

export const useThemeStore = create<ThemeState>()(
  persist(
    (set, get) => ({
      mode: "light",
      isDark: false,

      setMode: (mode) => {
        const isDark = resolveDark(mode);
        applyTheme(isDark);
        set({ mode, isDark });
      },

      toggleTheme: () => {
        const nextMode = get().isDark ? "light" : "dark";
        const isDark = resolveDark(nextMode);
        applyTheme(isDark);
        set({ mode: nextMode, isDark });
      },
    }),
    {
      name: "zephyr-theme-storage",
      onRehydrateStorage: () => (state) => {
        if (state) {
          const isDark = resolveDark(state.mode);
          applyTheme(isDark);
          state.isDark = isDark;
        }
      },
    }
  )
);
