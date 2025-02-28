import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../../configure/APIConfigure';
import { useUserStore } from '../../../globalStore/GlobalStore';
import styles from './LoginPage.module.css';
import { z } from 'zod';

const LoginSchema = z.object({
    email: z.string().email(),
    password: z.string().min(6),
});

const LoginPage: React.FC = () => {
    const navigate = useNavigate();
    const { setAuthorized, setUser } = useUserStore();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        const result = LoginSchema.safeParse({ email, password });
        if (!result.success) {
            setError('Invalid email or password');
            return;
        }
        setLoading(true);
        try {
            // POST /auth/login возвращает { token, user: UserDto }
            const response = await axiosInstance.post('/auth/login', {
                login: email,
                password: password
            });
            const { token, user } = response.data;
            localStorage.setItem('token', token);
            setUser(user);
            setAuthorized(true);
            navigate('/');
        } catch (err) {
            console.log(err);
            setError('Login failed. Please check your credentials.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.box}>
                <h2 className={styles.title}>Login</h2>
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
                        <label>Password:</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className={styles.button} disabled={loading}>
                        {loading ? 'Logging in...' : 'Login'}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default LoginPage;
