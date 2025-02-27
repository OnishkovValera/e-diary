import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../../../configure/APIConfigure.ts";
import { useUserStore } from "../../../globalStore/GlobalStore.ts";
import styles from "./LoginComponent.module.css"; // Подключаем стили

const LoginComponent = () => {
    const navigate = useNavigate();
    const { setAuthorized, setUser } = useUserStore();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError("");

        try {
            const response = await axiosInstance.post("/auth/login", {
                login: email,
                password: password,
            });

            const { token, user } = response.data;

            // Сохраняем токен в localStorage
            localStorage.setItem("token", token);

            // Устанавливаем пользователя в глобальный стейт
            setUser(user);
            setAuthorized(true);

            // Перенаправляем в личный кабинет
            navigate("/dashboard");
        } catch (err) {
            console.log(err);
            setError("Неверный email или пароль");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.loginBox}>
                <h2 className={styles.title}>Вход</h2>

                {error && <p className={styles.error}>{error}</p>}

                <form onSubmit={handleLogin}>
                    <div className={styles.inputGroup}>
                        <label>Email:</label>
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>

                    <div className={styles.inputGroup}>
                        <label>Пароль:</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    <button type="submit" className={styles.button} disabled={loading}>
                        {loading ? "Вход..." : "Войти"}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default LoginComponent;
