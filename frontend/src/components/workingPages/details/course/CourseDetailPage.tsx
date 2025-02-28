import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import axiosInstance from '../../../../configure/APIConfigure';
import {CourseDto, GradeDto} from '../../../../types';
import styles from './CourseDetailPage.module.css';
import {useUserStore} from '../../../../globalStore/GlobalStore';

const CourseDetailPage: React.FC = () => {
    const navigate = useNavigate();
    const {id} = useParams<{ id: string }>();
    const [course, setCourse] = useState<CourseDto | null>(null);
    const [grades, setGrades] = useState<GradeDto[]>([]);
    const [loading, setLoading] = useState(true);
    const {user} = useUserStore();
    const [courseRequestMessage, setCourseRequestMessage] = useState<string | null>(null);
    // Состояния для управления оценками (для учителя)
    const [selectedStudent, setSelectedStudent] = useState<number | null>(null);
    const [gradeValue, setGradeValue] = useState<number>(0);
    const [comment, setComment] = useState('');
    const [gradeError, setGradeError] = useState('');
    const [gradeSuccess, setGradeSuccess] = useState('');

    useEffect(() => {
        const fetchCourse = async () => {
            try {
                // GET /courses/:id – возвращает CourseDto
                const courseResponse = await axiosInstance.get(`/course/${id}`);
                setCourse(courseResponse.data);
                // GET /courses/:id/grades – возвращает список оценок (GradeDto)
                const gradesResponse = await axiosInstance.get(`/grades?course=${id}`);
                setGrades(gradesResponse.data);
                console.log(gradesResponse);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchCourse();
    }, [id]);

    if (loading) return <div>Loading...</div>;
    if (!course) return <div>Course not found.</div>;

    const isTeacher = course?.teacher?.id === user?.id;
    const isEnrolled = course.students.some(student => student.id === user?.id);

    // Функция подачи заявки на вступление в курс (для студентов)
    const handleCourseRequest = async () => {
        try {
            // POST /requests с полями: { course: { id: course.id }, role: 'STUDENT' }
            await axiosInstance.post('/request', {
                course: {
                    id: course.id
                },
                role: 'STUDENT'
            });
            setCourseRequestMessage('Enrollment request submitted.');
        } catch (err) {
            console.error('Error submitting course request', err);
            setCourseRequestMessage('Failed to submit enrollment request.');
        }
    };

    // Функция для выхода из курса (для студентов)
    const handleLeaveCourse = async () => {
        try {
            // DELETE /courses/:id/enrollments – удаляет зачисление студента
            await axiosInstance.delete(`/course/deleteMember/${course.id}`);
            // Обновляем состояние: удаляем пользователя из списка студентов
            setCourse(prev => prev ? {...prev, students: prev.students.filter(s => s.id !== user?.id)} : prev);
        } catch (err) {
            console.error('Failed to leave course', err);
        }
    };

    // Функция для добавления или обновления оценки (для учителя)
    const handleGradeSubmit = async (studentId: number) => {
        try {
            // Ищем существующую оценку для студента

            // POST /courses/:id/grades для добавления новой оценки
            await axiosInstance.post(`/grades`, {
                course: {
                    id: course.id,
                },
                student: {
                    id: studentId
                },
                grade: gradeValue,
                comment: comment
            });

            setGradeSuccess('Grade submitted successfully.');
            // Обновляем список оценок
            const gradesResponse = await axiosInstance.get(`/grades?course=${id}`);
            setGrades(gradesResponse.data);
        } catch (err) {
            console.error('Error submitting grade', err);
            setGradeError('Failed to submit grade.');
        } finally {
            setGradeValue(0);
            setComment("");
        }
    };

    const handleRemoveStudent = async (studentId: number) => {
        try {
            // DELETE /courses/:courseId/students/:studentId – удаляет студента из курса
            await axiosInstance.delete(`/course/${course.id}/deleteMember/${studentId}`);
            // Обновляем список студентов курса
            setCourse(prev => prev ? {...prev, students: prev.students.filter(s => s.id !== studentId)} : prev);
        } catch (err) {
            console.error('Error removing student from course', err);
        }
    };
    return (
        <div className={styles.container}>
            <h2>{course.name}</h2>
            <p>
                Teacher: {course?.teacher?.id ? course.teacher.firstName + " " + course.teacher.lastName : "Не назначен"}
            </p>
            <p>Created At: {new Date(course.createdAt).toLocaleDateString()}</p>

            {/* Отображение для студента */}
            {!isTeacher && (
                <>
                    {isEnrolled ? (
                        <div className={styles.studentSection}>
                            <button onClick={handleLeaveCourse}>Leave Course</button>
                            <h3>Your Grades</h3>
                            <ul className={styles.gradeList}>
                                {
                                    grades
                                        .filter(g => g.student.id === user?.id)
                                        .map((grade) => (
                                            <li key={grade.id}>
                                                Grade: {grade.grade} ({grade.comment})
                                            </li>
                                        ))}
                            </ul>
                        </div>
                    ) : (
                        <div className={styles.requestSection}>
                            <button onClick={handleCourseRequest}>Apply for Enrollment</button>
                            {courseRequestMessage && <p>{courseRequestMessage}</p>}
                        </div>
                    )}
                </>
            )}

            {isTeacher && (
                <div className={styles.manageEnrollment}>
                    <button onClick={() => navigate(`/requests?type=course&id=${course.id}`)}>
                        Manage Enrollment Requests
                    </button>
                </div>
            )}

            {/* Отображение для учителя */}
            {isTeacher && (
                <div className={styles.gradeManagement}>
                    <h3>Grade Management</h3>
                    <p>Select a student to add/update grade:</p>
                    <ul>
                        {course.students.map((student) => (
                            <li key={student.id}>
                                {student.firstName} {student.lastName}{' '}
                                <button onClick={() => {
                                    setGradeValue(0);
                                    setComment("");
                                    setSelectedStudent(student.id);
                                    setCourseRequestMessage(null)
                                }}>Grade
                                </button>
                                <button
                                    onClick={() => navigate(`/courses/${course.id}/history/${student.id}`)}
                                    className={styles.actionButton}>History
                                </button>
                                <button onClick={() => handleRemoveStudent(student.id)}>Remove</button>
                            </li>
                        ))}
                    </ul>
                    {selectedStudent && (
                        <div className={styles.gradeForm}>
                            <h4>Set Grade for
                                Student: {course.students.find(value => value.id === selectedStudent)?.firstName + " " + course.students.find(value => value.id === selectedStudent)?.lastName}</h4>
                            <div className={styles.inputGroup}>
                                <label>Grade (0-100):</label>
                                <input
                                    type="number"
                                    value={gradeValue}
                                    onChange={(e) => setGradeValue(Number(e.target.value))}
                                    min={0}
                                    max={100}
                                />
                            </div>
                            <div className={styles.inputGroup}>
                                <label>Comment:</label>
                                <input
                                    type="text"
                                    value={comment}
                                    onChange={(e) => setComment(e.target.value)}
                                />
                            </div>
                            <button onClick={() => handleGradeSubmit(selectedStudent)}>Submit Grade</button>
                            {gradeError && <p className={styles.error}>{gradeError}</p>}
                            {gradeSuccess && <p className={styles.success}>{gradeSuccess}</p>}
                            <button onClick={() => {
                                setSelectedStudent(null)
                                setGradeValue(0)
                                setComment("")
                                setCourseRequestMessage(null)
                            }}>Cancel
                            </button>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default CourseDetailPage;
