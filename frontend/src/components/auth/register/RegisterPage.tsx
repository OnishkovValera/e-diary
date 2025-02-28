import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../../configure/APIConfigure';
import { useUserStore } from '../../../globalStore/GlobalStore';
import styles from './RegisterPage.module.css';
import { z } from 'zod';

const RegisterSchema = z
    .object({
        email: z.string().email(),
        firstName: z.string().min(2),
        lastName: z.string().min(2),
        password: z.string().min(6),
        confirmPassword: z.string().min(6),
    })
    .refine((data) => data.password === data.confirmPassword, {
        message: 'Passwords do not match',
        path: ['confirmPassword'],
    });

const RegisterPage: React.FC = () => {
    const navigate = useNavigate();
    const { setAuthorized, setUser } = useUserStore();
    const [email, setEmail] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleRegister = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        const result = RegisterSchema.safeParse({ email, firstName, lastName, password, confirmPassword });
        if (!result.success) {
            setError(result.error.errors[0].message);
            return;
        }
        setLoading(true);
        try {
            await axiosInstance.post('/auth/reg', {
                login: email,
                firstName: firstName,
                lastName: lastName,
                password: password
            });
            // Автоматически выполняем логин: POST /auth/login возвращает { token, user }
            const loginResponse = await axiosInstance.post('/auth/login', {
                login: email,
                password: password
            });
            const { token, user } = loginResponse.data;
            localStorage.setItem('token', token);
            setUser(user);
            setAuthorized(true);
            navigate('/');
        } catch (err) {
            console.log(err);
            setError(err.response.data.detail ? err.response.data.detail : "Произошла ошибка, повторите попытку");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.box}>
                <h2 className={styles.title}>Register</h2>
                {error && <p className={styles.error}>{error}</p>}
                <form onSubmit={handleRegister}>
                    <div className={styles.inputGroup}>
                        <label>Email:</label>
                        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                    </div>
                    <div className={styles.inputGroup}>
                        <label>First Name:</label>
                        <input type="text" value={firstName} onChange={(e) => setFirstName(e.target.value)} required />
                    </div>
                    <div className={styles.inputGroup}>
                        <label>Last Name:</label>
                        <input type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} required />
                    </div>
                    <div className={styles.inputGroup}>
                        <label>Password:</label>
                        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                    </div>
                    <div className={styles.inputGroup}>
                        <label>Confirm Password:</label>
                        <input type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} required />
                    </div>
                    <button type="submit" className={styles.button} disabled={loading}>
                        {loading ? 'Registering...' : 'Register'}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default RegisterPage;
