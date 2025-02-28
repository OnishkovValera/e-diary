import React, { useState } from 'react';
import axiosInstance from '../../../../configure/APIConfigure.ts';
import { OrganizationDto } from '../../../../types';
import styles from './SearchOrganizationsPage.module.css';
import { useNavigate } from 'react-router-dom';

const SearchOrganizationsPage: React.FC = () => {
    const [query, setQuery] = useState('');
    const [results, setResults] = useState<OrganizationDto[]>([]);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSearch = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        try {
            // GET /organizations/search?query=... – возвращает список подходящих организаций
            const response = await axiosInstance.get(`/organization?query=${query}`);
            setResults(response.data);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.container}>
            <h2>Search Organizations</h2>
            <form onSubmit={handleSearch} className={styles.searchForm}>
                <input
                    type="text"
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                    placeholder="Enter organization name"
                />
                <button type="submit">Search</button>
            </form>
            {loading ? (
                <p>Loading...</p>
            ) : (
                <ul className={styles.list}>
                    {results.map((org) => (
                        <li key={org.id} onClick={() => navigate(`/organizations/${org.id}`)}>
                            {org.name}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default SearchOrganizationsPage;
