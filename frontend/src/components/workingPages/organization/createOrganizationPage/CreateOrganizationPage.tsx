import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../../../configure//APIConfigure';
import { z } from 'zod';
import styles from './CreateOrganizationPage.module.css';

// Схема валидации для создания организации
const OrganizationSchema = z.object({
    name: z.string().nonempty('Organization name is required'),
});

const CreateOrganizationPage: React.FC = () => {
    const navigate = useNavigate();
    const [name, setName] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    const handleCreate = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);

        // Проверяем данные через Zod
        const result = OrganizationSchema.safeParse({ name });
        if (!result.success) {
            setError(result.error.errors[0].message);
            return;
        }
        setLoading(true);
        try {
            // POST /organizations
            // Отправляем запрос для создания организации с полем { name }
            // В ответ получаем OrganizationDto
            const response = await axiosInstance.post('/organization', {
                name: name
            });
            const organization = response.data;

            // Перенаправляем на страницу деталей организации (например, /organizations/:id)
            navigate(`/organizations/${organization.id}`);
        } catch (err) {
            console.error(err);
            setError('Failed to create organization.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.box}>
                <h2>Create Your Organization</h2>
                {error && <p className={styles.error}>{error}</p>}
                <form onSubmit={handleCreate}>
                    <div className={styles.inputGroup}>
                        <label>Organization Name:</label>
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" disabled={loading} className={styles.button}>
                        {loading ? 'Creating...' : 'Create Organization'}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default CreateOrganizationPage;
