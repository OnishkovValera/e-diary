import React, { useEffect, useState } from 'react';
import axiosInstance from '../../../configure/APIConfigure';
import { CourseDto } from '../../../types';
import styles from './StudentCoursesPage.module.css';
import { useNavigate } from 'react-router-dom';

const StudentCoursesPage: React.FC = () => {
    const [courses, setCourses] = useState<CourseDto[]>([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchCourses = async () => {
            try {
                // GET /courses/student – возвращает курсы, где пользователь студент
                const response = await axiosInstance.get('/user/getCourses');
                setCourses(response.data);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchCourses();
    }, []);

    if (loading) return <div>Loading...</div>;

    return (
        <div className={styles.container}>
            <h2>Your Courses (Student)</h2>
            <ul className={styles.list}>
                {courses.map((course) => (
                    <li key={course.id} onClick={() => navigate(`/courses/${course.id}`)}>
                        {course.name}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default StudentCoursesPage;
