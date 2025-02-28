import { create } from 'zustand';
import { UserDto } from '../types';
import axiosInstance from "../configure/APIConfigure.ts";

interface UserStore {
    authorized: boolean;
    user: UserDto | null;
    loading: boolean;
    setAuthorized: (value: boolean) => void;
    setUser: (user: UserDto | null) => void;
    setLoading: (value: boolean) => void;
    // Функция для проверки токена на сервере
    checkAuth: () => Promise<void>;
}

export const useUserStore = create<UserStore>((set) => ({
    authorized: false,
    user: null,
    loading: true,
    setAuthorized: (value: boolean) => set({ authorized: value }),
    setUser: (user: UserDto | null) => set({ user }),
    setLoading: (value: boolean) => set({ loading: value }),
    checkAuth: async () => {
        const token = localStorage.getItem('token');
        if (!token) {
            set({ authorized: false, user: null, loading: false });
            return;
        }
        try {
            // Отправляем запрос для проверки токена на эндпоинте /auth/check
            const response = await axiosInstance.get('/auth/check', {
                headers: { Authorization: `Bearer ${token}` },
            });

            const data = response.data;
            set({ authorized: true, user: data, loading: false });
        } catch (error) {
            console.log(error);
            localStorage.removeItem('token');
            set({ authorized: false, user: null, loading: false });
        }
    },
}));
