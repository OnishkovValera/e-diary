import { ReactNode } from 'react';
import { Navigate } from 'react-router-dom';
import { useUserStore } from '../../globalStore/GlobalStore.ts';

interface ProtectedRouteProps {
    children: ReactNode;
}

const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
    const { authorized, loading } = useUserStore();
    if (loading) return <div>Loading...</div>;
    return authorized ? <>{children}</> : <Navigate to="/login" replace />;
};

export default ProtectedRoute;