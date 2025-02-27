import { ReactNode } from "react";
import { Navigate } from "react-router-dom";
import { useUserStore } from "../../globalStore/GlobalStore.ts";

interface ProtectedRouteProps {
    children: ReactNode;
}

const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
    const { authorized, loading } = useUserStore((state) => ({
        authorized: state.authorized,
        loading: state.isLoading,
    }));

    // Пока идёт загрузка данных, ничего не показываем
    if (loading) return <div>Загрузка...</div>;

    // Если пользователь не авторизован, отправляем на логин
    return authorized ? children : <Navigate to="/login"/>;
};

export default ProtectedRoute;
