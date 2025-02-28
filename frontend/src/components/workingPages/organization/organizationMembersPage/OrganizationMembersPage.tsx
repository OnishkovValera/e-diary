import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axiosInstance from '../../../../configure/APIConfigure';
import { MemberDto } from '../../../../types';
import styles from './OrganizationMembersPage.module.css';

const OrganizationMembersPage: React.FC = () => {
    const { id } = useParams<{ id: string }>(); // organization id
    const [members, setMembers] = useState<MemberDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [actionMessage, setActionMessage] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchMembers = async () => {
            try {
                // GET /organizations/:id – возвращает OrganizationDto с memberOrganizations
                const response = await axiosInstance.get(`/organization/${id}`);
                const org = response.data;
                setMembers(org.memberOrganizations);
            } catch (err) {
                console.error('Error fetching members', err);
            } finally {
                setLoading(false);
            }
        };
        fetchMembers();
    }, [id]);

    const handleRemoveMember = async (memberId: number) => {
        try {
            // DELETE /organizations/:orgId/members/:memberId – удаляет участника из организации
            await axiosInstance.delete(`/organization/${id}/deleteMember/${memberId}`);
            setMembers(prev => prev.filter(m => m.member.id !== memberId));
            setActionMessage('Member removed successfully.');
        } catch (err) {
            console.error('Error removing member', err);
            setActionMessage('Failed to remove member.');
        }
    };

    if (loading) return <div>Loading...</div>;

    return (
        <div className={styles.container}>
            <h2>Organization Members</h2>
            <ul className={styles.memberList}>
                {members.map(member => (
                    <li key={member.id} className={styles.memberItem}>
            <span>
              {member.member.firstName} {member.member.lastName} - {member.roleInOrganization}
            </span>
                        {/* Не даём возможность удалить владельца */}
                        {member.roleInOrganization !== 'OWNER' && (
                            <button onClick={() => handleRemoveMember(member.member.id)}>Remove</button>
                        )}
                    </li>
                ))}
            </ul>
            {actionMessage && <p>{actionMessage}</p>}
            <button onClick={() => navigate(-1)} className={styles.backButton}>Back</button>
        </div>
    );
};

export default OrganizationMembersPage;
