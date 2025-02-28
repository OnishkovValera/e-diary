import React, {useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import axiosInstance from '../../../../configure/APIConfigure.ts';
import {CourseDto} from '../../../../types';
import styles from './OrganizationCoursesPage.module.css';

const OrganizationCoursesPage: React.FC = () => {
    const {id} = useParams<{ id: string }>(); // organization id
    const [courses, setCourses] = useState<CourseDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [requestMessage, setRequestMessage] = useState<string | null>(null);

    useEffect(() => {
        const fetchCourses = async () => {
            try {
                // GET /organizations/:orgId/courses – возвращает список курсов данной организации
                const response = await axiosInstance.get(`/course/getAllFromOrganization/${id}`);
                setCourses(response.data);
            } catch (err) {
                console.error('Error fetching courses', err);
            } finally {
                setLoading(false);
            }
        };
        fetchCourses();
    }, [id]);

    const handleCourseRequest = async (courseId: number) => {
        try {
            // POST /requests – отправляем { course: { id: courseId }, organization: Number(id), role: 'STUDENT' }
            await axiosInstance.post('/request', {
                course: {
                    id: courseId
                },
                organization: {
                    id: Number(id)
                },
                role: 'STUDENT'
            });
            setRequestMessage('Enrollment request submitted for course.');
        } catch (err) {
            console.error('Error submitting course request', err);
            setRequestMessage('Failed to submit enrollment request.');
        }
    };

    if (loading) return <div>Loading...</div>;

    return (
        <div className={styles.container}>
            <h2>Available Courses for Enrollment</h2>
            <ul className={styles.courseList}>
                {courses.map(course => (
                    <li key={course.id} className={styles.courseItem}>
                        <div>
                            <strong>{course.name}</strong> –
                            Teacher: {course.teacher.firstName} {course.teacher.lastName}
                        </div>
                        <button onClick={() => handleCourseRequest(course.id)}>Apply for Course</button>
                    </li>
                ))}
            </ul>
            {requestMessage && <p>{requestMessage}</p>}
        </div>
    );
};

export default OrganizationCoursesPage;
