import { User } from "../types/User.ts";
import { create } from "zustand/react";
import axios from "axios";

interface UserStore {
    authorized: boolean;
    user: User | null;
    isLoading: boolean;
    setAuthorized: (authorized: boolean) => void;
    setUser: (user: User | null) => void;
    checkAuth: () => Promise<void>;
}

export const useUserStore = create<UserStore>((set) => ({
    authorized: false,
    user: null,
    isLoading: true, // Начинаем с загрузки

    setAuthorized: (authorized: boolean) => set({ authorized }),
    setUser: (user: User | null) => set({ user }),

    checkAuth: async () => {
        const token = localStorage.getItem("token");

        if (!token) {
            set({ authorized: false, user: null, isLoading: false });
            return;
        }

        try {
            const response = await axios.get<User>("http://localhost:8080/api/v1/auth/", {
                headers: { Authorization: `Bearer ${token}` },
            });

            set({ authorized: true, user: response.data, isLoading: false });
        } catch (error) {
            console.error("Auth check failed:", error);
            localStorage.removeItem("token");
            set({ authorized: false, user: null, isLoading: false });
        }
    },
}));

// Вызываем проверку при загрузке
useUserStore.getState().checkAuth();
