import axios from "axios";
import { useUserStore } from "../globalStore/GlobalStore.ts";
import { useNavigate } from "react-router-dom";

const axiosInstance = axios.create({
    baseURL: "http://localhost:8080/api/v1",
    timeout: 5000,
});

// Функция выхода пользователя (удаление токена и сброс состояния)
const logoutUser = () => {
    localStorage.removeItem("token");
    useUserStore.getState().setAuthorized(false);
    useUserStore.getState().setUser(null);
};

// Перехватываем запрос перед отправкой
axiosInstance.interceptors.request.use(
    (config) => {
        // Если запрос к публичным эндпоинтам (логин, регистрация), пропускаем проверку токена
        const publicPaths = ["/auth/login", "/auth/reg"];
        if (config.url && publicPaths.some((path) => config.url?.includes(path))) {
            return config;
        }

        const token = localStorage.getItem("token");
        if (!token) {
            logoutUser();
            return Promise.reject(new Error("Unauthorized: No token found"));
        }

        config.headers.Authorization = `Bearer ${token}`;
        return config;
    },
    (error) => Promise.reject(error)
);

// Перехватываем ответ
axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            console.warn("Unauthorized response, logging out...");
            logoutUser();
            // Используем useNavigate для редиректа на страницу логина
            const navigate = useNavigate();
            navigate("/login");
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;
