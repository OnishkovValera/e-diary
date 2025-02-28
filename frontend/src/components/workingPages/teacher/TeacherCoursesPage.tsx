import React, { useEffect, useState } from 'react';
import axiosInstance from '../../../configure/APIConfigure';
import { CourseDto } from '../../../types';
import styles from './TeacherCoursesPage.module.css';
import { useNavigate } from 'react-router-dom';

const TeacherCoursesPage: React.FC = () => {
    const [courses, setCourses] = useState<CourseDto[]>([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchCourses = async () => {
            try {
                // GET /courses/teacher – возвращает курсы, где пользователь учитель
                const response = await axiosInstance.get('/user/getTeachingCourse');
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
            <h2>Your Courses (Teacher)</h2>
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

export default TeacherCoursesPage;
