import React, { useEffect, useState } from 'react';
import axiosInstance from '../../../configure/APIConfigure';
import { OrganizationDto } from '../../../types';
import styles from './AdminOrganizationsPage.module.css';
import { useNavigate } from 'react-router-dom';

const AdminOrganizationsPage: React.FC = () => {
    const [organizations, setOrganizations] = useState<OrganizationDto[]>([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchOrganizations = async () => {
            try {
                // GET /organizations/admin – возвращает список организаций, где пользователь админ
                const response = await axiosInstance.get('/user/getOrganizations');
                setOrganizations(response.data);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchOrganizations();
    }, []);

    if (loading) return <div>Loading...</div>;

    return (
        <div className={styles.container}>
            <h2>Your Organizations (Admin)</h2>
            <button
                className={styles.createButton}
                onClick={() => navigate('/create-organization')}
            >
                Create New Organization
            </button>
            <ul className={styles.list}>
                {organizations.map((org) => (
                    <li key={org.id} onClick={() => navigate(`/organizations/${org.id}`)}>
                        {org.name}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default AdminOrganizationsPage;
