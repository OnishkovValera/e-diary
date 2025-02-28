import React, { useState } from 'react';
import { useUserStore } from '../../../globalStore/GlobalStore';
import axiosInstance from '../../../configure/APIConfigure';
import styles from './EditProfilePage.module.css';
import { z } from 'zod';
import {useNavigate} from "react-router-dom";

const ProfileSchema = z.object({
    firstName: z.string().min(2),
    lastName: z.string().min(2),
});

const EditProfilePage: React.FC = () => {
    const { user, setUser } = useUserStore();
    const [firstName, setFirstName] = useState(user?.firstName || '');
    const [lastName, setLastName] = useState(user?.lastName || '');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        const result = ProfileSchema.safeParse({ firstName, lastName });
        if (!result.success) {
            setError(result.error.errors[0].message);
            return;
        }
        setLoading(true);
        try {
            // PUT /user – обновляет профиль и возвращает обновлённого пользователя
            const response = await axiosInstance.put('/user', {
                firstName: firstName,
                lastName: lastName
            });
            setUser(response.data);
            setSuccess('Profile updated successfully.');
        } catch (err) {
            console.error(err);
            setError('Failed to update profile.');
        } finally {
            setLoading(false);
        }
    };



    return (
        <div className={styles.container}>
            <h2>Edit Profile</h2>
            {error && <p className={styles.error}>{error}</p>}
            {success && <p className={styles.success}>{success}</p>}
            <form onSubmit={handleSubmit}>
                <div className={styles.inputGroup}>
                    <label>First Name:</label>
                    <input type="text" value={firstName} onChange={(e) => setFirstName(e.target.value)} required/>
                </div>
                <div className={styles.inputGroup}>
                    <label>Last Name:</label>
                    <input type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} required/>
                </div>
                <button type="submit" className={styles.button} disabled={loading}>
                    {loading ? 'Updating...' : 'Update Profile'}
                </button>
            </form>
        </div>
    );
};

export default EditProfilePage;
