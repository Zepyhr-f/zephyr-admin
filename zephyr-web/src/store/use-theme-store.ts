import { create } from "zustand";
import { persist } from "zustand/middleware";

type ThemeMode = "light" | "dark" | "auto";

interface ThemeState {
  mode: ThemeMode;
  isDark: boolean;
  primaryColor: string;
  buttonHoverColor: string;
  setMode: (mode: ThemeMode) => void;
  toggleTheme: () => void;
  setPrimaryColor: (color: string) => void;
  setButtonHoverColor: (color: string) => void;
}

function resolveDark(mode: ThemeMode): boolean {
  if (mode === "dark") return true;
  if (mode === "light") return false;
  return window.matchMedia("(prefers-color-scheme: dark)").matches;
}

function applyTheme(isDark: boolean, primaryColor: string, buttonHoverColor: string) {
  const root = document.documentElement;
  if (isDark) {
    root.setAttribute("data-theme", "dark");
  } else {
    root.removeAttribute("data-theme");
  }
  root.style.setProperty("--z-primary", primaryColor);
  root.style.setProperty("--z-button-hover", buttonHoverColor);
}

export const useThemeStore = create<ThemeState>()(
  persist(
    (set, get) => ({
      mode: "light",
      isDark: false,
      primaryColor: "#1E40AF",
      buttonHoverColor: "rgba(30, 64, 175, 0.06)",

      setMode: (mode) => {
        const isDark = resolveDark(mode);
        applyTheme(isDark, get().primaryColor, get().buttonHoverColor);
        set({ mode, isDark });
      },

      toggleTheme: () => {
        const nextMode = get().isDark ? "light" : "dark";
        const isDark = resolveDark(nextMode);
        applyTheme(isDark, get().primaryColor, get().buttonHoverColor);
        set({ mode: nextMode, isDark });
      },
      
      setPrimaryColor: (primaryColor) => {
        applyTheme(get().isDark, primaryColor, get().buttonHoverColor);
        set({ primaryColor });
      },

      setButtonHoverColor: (buttonHoverColor) => {
        applyTheme(get().isDark, get().primaryColor, buttonHoverColor);
        set({ buttonHoverColor });
      }
    }),
    {
      name: "zephyr-theme-storage",
      onRehydrateStorage: () => (state) => {
        if (state) {
          const isDark = resolveDark(state.mode);
          const color = state.primaryColor || "#1E40AF";
          const hoverColor = state.buttonHoverColor || "rgba(30, 64, 175, 0.06)";
          applyTheme(isDark, color, hoverColor);
          state.isDark = isDark;
          state.primaryColor = color;
          state.buttonHoverColor = hoverColor;
        }
      },
    }
  )
);
