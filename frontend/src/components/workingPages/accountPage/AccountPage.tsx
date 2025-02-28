import React, { useEffect, useState } from 'react';
import { useUserStore } from '../../../globalStore/GlobalStore';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../../configure/APIConfigure';
import styles from './AccountPage.module.css';
import { CourseDto, OrganizationDto } from '../../../types';

const AccountPage: React.FC = () => {
    const { user } = useUserStore();
    const navigate = useNavigate();
    const [adminOrganizations, setAdminOrganizations] = useState<OrganizationDto[]>([]);
    const [teacherCourses, setTeacherCourses] = useState<CourseDto[]>([]);
    const [studentCourses, setStudentCourses] = useState<CourseDto[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                // GET /organizations/admin – возвращает список организаций, где пользователь админ
                const orgResponse = await axiosInstance.get('/user/getOrganizations');
                // GET /courses/teacher – курсы, где пользователь является учителем
                const teacherResponse = await axiosInstance.get('/user/getTeachingCourse');
                // GET /courses/student – курсы, где пользователь является студентом
                const studentResponse = await axiosInstance.get('/user/getCourses');
                setAdminOrganizations(orgResponse.data);
                setTeacherCourses(teacherResponse.data);
                setStudentCourses(studentResponse.data);
            } catch (err) {
                console.error('Error fetching account data', err);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    if (!user) return null;
    if (loading) return <div>Loading...</div>;

    return (
        <div className={styles.container}>
            <header className={styles.header}>
                <div className={styles.userInfo}>
                    <h2>
                        {user.firstName} {user.lastName}
                    </h2>
                    <button onClick={() => navigate('/edit-profile')}>Edit Profile</button>
                    <button onClick={() => {
                        localStorage.removeItem('token');
                        useUserStore.getState().setAuthorized(false);
                        useUserStore.getState().setUser(null);
                        navigate('/login');
                    }} className={styles.logoutButton}>
                        Logout
                    </button>
                </div>
            </header>
            <main className={styles.main}>
                <div className={styles.navBlock} onClick={() => navigate('/organizations')}>
                    <h3>Organizations (Admin)</h3>
                    <p>{adminOrganizations.length} found</p>
                    <ul>
                        {adminOrganizations.slice(0, 3).map((org) => (
                            <li key={org.id}>{org.name}</li>
                        ))}
                    </ul>
                </div>
                <div className={styles.navBlock} onClick={() => navigate('/courses/teacher')}>
                    <h3>Courses (Teacher)</h3>
                    <p>{teacherCourses.length} found</p>
                    <ul>
                        {teacherCourses.slice(0, 3).map((course) => (
                            <li key={course.id}>{course.name}</li>
                        ))}
                    </ul>
                </div>
                <div className={styles.navBlock} onClick={() => navigate('/courses/student')}>
                    <h3>Courses (Student)</h3>
                    <p>{studentCourses.length} found</p>
                    <ul>
                        {studentCourses.slice(0, 3).map((course) => (
                            <li key={course.id}>{course.name}</li>
                        ))}
                    </ul>
                </div>
            </main>
            <div className={styles.searchOrg}>
                <button onClick={() => navigate('/search-organizations')}>
                    Search Organizations
                </button>
            </div>
        </div>
    );
};

export default AccountPage;

