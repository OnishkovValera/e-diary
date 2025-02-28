import React, {useEffect, useState} from 'react';
import {useSearchParams, useNavigate} from 'react-router-dom';
import axiosInstance from '../../../configure/APIConfigure';
import {RequestDto} from '../../../types';
import styles from './RequestManagementPage.module.css';

const RequestManagementPage: React.FC = () => {
    const [searchParams] = useSearchParams();
    // Если заданы параметры (type и id) – фильтруем заявки по организации или курсу
    const filterType = searchParams.get('type');
    const filterId = searchParams.get('id');
    const [requests, setRequests] = useState<RequestDto[]>([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchRequests = async () => {
            try {
                // GET /requests/pending?organizationId=... или /requests/pending?courseId=...
                const endpoint = filterType && filterId
                    ? filterType === 'organization'
                        ? `/request/forAdmin?organization=${filterId}`
                        : `/request/studentOnCourse?course=${filterId}`
                    : '/request';
                const response = await axiosInstance.get(endpoint);
                setRequests(response.data);
            } catch (err) {
                console.error('Error fetching requests', err);
            } finally {
                setLoading(false);
            }
        };
        fetchRequests();
    }, [filterType, filterId]);

    const handleAction = async (requestId: number, action: 'APPROVED' | 'REJECTED') => {
        try {
            // PUT /requests/:id – отправляем { status: action }
            await axiosInstance.put(`/request/handleRequest/${requestId}?status=${action}`);
            setRequests(prev => prev.filter(req => req.id !== requestId));
        } catch (err) {
            console.error(`Failed to ${action} request ${requestId}`, err);
        }
    };

    if (loading) return <div>Loading...</div>;

    return (
        <div className={styles.container}>
            <h2>Manage Requests</h2>
            {requests.length === 0 ? (
                <p>No pending requests.</p>
            ) : (
                <table className={styles.table}>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>User</th>
                        <th>Type</th>
                        <th>Requested Role</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    {requests.map(req => (
                        <tr key={req.id}>
                            <td>{req.id}</td>
                            <td>{req.user.firstName} {req.user.lastName}</td>
                            <td>{req.course ? 'Course Enrollment' : 'Organization Membership'}</td>
                            <td>{req.role}</td>
                            <td>
                                <button onClick={() => handleAction(req.id, 'APPROVED')}>Approve</button>
                                <button onClick={() => handleAction(req.id, 'REJECTED')}>Reject</button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
            <button onClick={() => navigate(-1)} className={styles.backButton}>Back</button>
        </div>
    );
};

export default RequestManagementPage;
