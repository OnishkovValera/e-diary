import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axiosInstance from '../../../configure/APIConfigure';
import { GradeDto } from '../../../types';
import styles from './StudentGradeHistoryPage.module.css';

interface EditableGrade extends GradeDto {
    isEditing: boolean;
    newGrade: number;
    newComment: string;
}

const StudentGradeHistoryPage: React.FC = () => {
    const { courseId, studentId } = useParams<{ courseId: string; studentId: string }>();
    const [grades, setGrades] = useState<EditableGrade[]>([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchHistory = async () => {
            try {
                const response = await axiosInstance.get(
                    `/grades/byStudent?course=${courseId}&student=${studentId}`
                );
                const fetchedGrades: EditableGrade[] = response.data.map((grade: GradeDto) => ({
                    ...grade,
                    isEditing: false,
                    newGrade: grade.grade,
                    newComment: grade.comment || ''
                }));
                setGrades(fetchedGrades);
            } catch (err) {
                console.error('Error fetching grade history', err);
            } finally {
                setLoading(false);
            }
        };
        fetchHistory();
    }, [courseId, studentId]);

    const toggleEdit = (gradeId: number) => {
        setGrades(prev =>
            prev.map(g => (g.id === gradeId ? { ...g, isEditing: !g.isEditing } : g))
        );
    };

    const handleInputChange = (gradeId: number, field: 'newGrade' | 'newComment', value: any) => {
        setGrades(prev =>
            prev.map(g => (g.id === gradeId ? { ...g, [field]: value } : g))
        );
    };

    const handleSubmit = async (gradeId: number) => {
        const gradeToUpdate = grades.find(g => g.id === gradeId);
        if (!gradeToUpdate) return;
        try {
            // PUT /grades/:id – обновляем оценку, отправляем { id, grade, comment }
            await axiosInstance.put(`/grades`, {
                id: gradeId,
                grade: gradeToUpdate.newGrade,
                comment: gradeToUpdate.newComment
            });
            setGrades(prev =>
                prev.map(g =>
                    g.id === gradeId
                        ? { ...g, grade: g.newGrade, comment: g.newComment, isEditing: false }
                        : g
                )
            );
        } catch (err) {
            console.error('Error updating grade', err);
        }
    };

    if (loading) return <div className={styles.loading}>Loading...</div>;

    return (
        <div className={styles.container}>
            <h2 className={styles.title}>Grade History</h2>
            <p className={styles.subtitle}>
                Course ID: {courseId} | Student ID: {studentId}
            </p>
            {grades.length === 0 ? (
                <p className={styles.noGrades}>No grade history available.</p>
            ) : (
                <table className={styles.table}>
                    <thead>
                    <tr>
                        <th>Date</th>
                        <th>Grade</th>
                        <th>Comment</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {grades.map(grade => (
                        <tr key={grade.id} className={styles.tableRow}>
                            <td>{new Date(grade.gradeDateTime).toLocaleString()}</td>
                            <td>
                                {grade.isEditing ? (
                                    <input
                                        type="number"
                                        min={0}
                                        max={100}
                                        value={grade.newGrade}
                                        onChange={e => handleInputChange(grade.id, 'newGrade', Number(e.target.value))}
                                        className={styles.input}
                                    />
                                ) : (
                                    grade.grade
                                )}
                            </td>
                            <td>
                                {grade.isEditing ? (
                                    <input
                                        type="text"
                                        value={grade.newComment}
                                        onChange={e => handleInputChange(grade.id, 'newComment', e.target.value)}
                                        className={styles.input}
                                    />
                                ) : (
                                    grade.comment
                                )}
                            </td>
                            <td>
                                {grade.isEditing ? (
                                    <>
                                        <button onClick={() => handleSubmit(grade.id)} className={styles.button}>
                                            Save
                                        </button>
                                        <button onClick={() => toggleEdit(grade.id)} className={styles.button}>
                                            Cancel
                                        </button>
                                    </>
                                ) : (
                                    <button onClick={() => toggleEdit(grade.id)} className={styles.button}>
                                        Change Mark
                                    </button>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
            <button onClick={() => navigate(-1)} className={styles.backButton}>
                Back
            </button>
        </div>
    );
};

export default StudentGradeHistoryPage;
