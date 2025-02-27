import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../../../configure/APIConfigure.ts";
import { useUserStore } from "../../../globalStore/GlobalStore.ts";
import styles from "./RegisterComponent.module.css";

const RegisterPage = () => {
    const navigate = useNavigate();
    const { setAuthorized, setUser } = useUserStore();
    const [email, setEmail] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const handleRegister = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (password !== confirmPassword) {
            setError("Пароли не совпадают");
            return;
        }

        setLoading(true);
        setError("");

        try {
            // Отправляем запрос на регистрацию (эндпоинт возвращает пользователя без токена)
            await axiosInstance.post("/auth/reg", {
                login: email,
                firstName: firstName,
                lastName: lastName,
                password: password,
            });

            // После успешной регистрации автоматически выполняем запрос на логин,
            // чтобы получить токен и данные пользователя
            const loginResponse = await axiosInstance.post("/auth/login", {
                login: email,
                password: password,
            });

            const { token, user } = loginResponse.data;

            // Сохраняем токен и обновляем глобальное состояние
            localStorage.setItem("token", token);
            setUser(user);
            setAuthorized(true);

            // Перенаправляем пользователя в защищённую область
            navigate("/dashboard");
        } catch (err) {
            console.log(err);
            setError("Ошибка регистрации. Проверьте введённые данные и попробуйте снова.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.registerBox}>
                <h2 className={styles.title}>Регистрация</h2>
                {error && <p className={styles.error}>{error}</p>}
                <form onSubmit={handleRegister}>
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
                        <label>Имя:</label>
                        <input
                            type="text"
                            value={firstName}
                            onChange={(e) => setFirstName(e.target.value)}
                            required
                        />
                    </div>

                    <div className={styles.inputGroup}>
                        <label>Фамилия:</label>
                        <input
                            type="text"
                            value={lastName}
                            onChange={(e) => setLastName(e.target.value)}
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

                    <div className={styles.inputGroup}>
                        <label>Повторите пароль:</label>
                        <input
                            type="password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </div>

                    <button type="submit" className={styles.button} disabled={loading}>
                        {loading ? "Регистрация..." : "Зарегистрироваться"}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default RegisterPage;
