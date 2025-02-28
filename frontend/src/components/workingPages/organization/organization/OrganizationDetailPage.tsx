import React, {useEffect, useState} from 'react';
import {useParams, useNavigate} from 'react-router-dom';
import axiosInstance from '../../../../configure/APIConfigure.ts';
import {MemberDto, OrganizationDto, CourseDto} from '../../../../types';
import styles from './OrganizationDetailPage.module.css';
import {useUserStore} from '../../../../globalStore/GlobalStore.ts';

const OrganizationDetailPage: React.FC = () => {
    const {id} = useParams<{ id: string }>(); // organization id
    const navigate = useNavigate();
    const [organization, setOrganization] = useState<OrganizationDto | null>(null);
    const [loading, setLoading] = useState(true);
    const [requestMessage, setRequestMessage] = useState<string | null>(null);
    const [courseCreateMessage, setCourseCreateMessage] = useState<string | null>(null);
    const [courseCreateError, setCourseCreateError] = useState<string | null>(null);
    const [showCreateCourseForm, setShowCreateCourseForm] = useState(false);
    const [courseName, setCourseName] = useState('');
    const [selectedTeacherId, setSelectedTeacherId] = useState<number | ''>('');
    const {user} = useUserStore();

    // Новые состояния для управления курсами (для админа)
    const [courses, setCourses] = useState<CourseDto[]>([]);
    const [loadingCourses, setLoadingCourses] = useState(true);
    const [managingCourseId, setManagingCourseId] = useState<number | null>(null);
    const [updateMessage, setUpdateMessage] = useState<string | null>(null);

    useEffect(() => {
        const fetchOrganization = async () => {
            try {
                // GET /organization/:id – возвращает OrganizationDto с memberOrganizations
                const response = await axiosInstance.get(`/organization/${id}`);
                setOrganization(response.data);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchOrganization();
    }, [id]);

    // После загрузки организации, если пользователь является админ, загружаем список курсов организации
    useEffect(() => {
        if (organization) {
            // Определяем членство для проверки роли
            const member = organization.memberOrganizations.find(m => m.member.id === user?.id);
            const isAdmin = member?.roleInOrganization === 'ADMIN';
            if (isAdmin) {
                const fetchCourses = async () => {
                    try {
                        // GET /organization/:orgId/courses – возвращает список курсов данной организации
                        const response = await axiosInstance.get(`/course/getAllFromOrganization/${organization.id}`);
                        setCourses(response.data);
                    } catch (err) {
                        console.error(err);
                    } finally {
                        setLoadingCourses(false);
                    }
                };
                fetchCourses();
            } else {
                setLoadingCourses(false);
            }
        }
    }, [organization, user]);

    if (loading) return <div>Loading...</div>;
    if (!organization) return <div>Organization not found.</div>;

    // Определяем членство пользователя в организации
    const member = organization.memberOrganizations.find(m => m.member.id === user?.id);
    // Если членство есть и роль равна TEACHER или ADMIN – считаем пользователя привилегированным
    const isPrivileged = member && (member.roleInOrganization === 'TEACHER' || member.roleInOrganization === 'ADMIN');
    const isMember = !!member;
    // Для создания курса показываем форму только для ADMIN
    const isAdmin = member?.roleInOrganization === 'ADMIN';

    // Функция подачи заявки на вступление в организацию (course = null)
    const handleOrgRequest = async (role: string) => {
        try {
            // POST /request – отправляем { organization: { id: organization.id }, role, course: null }
            await axiosInstance.post('/request', {
                organization: {id: organization.id},
                role: role,
                course: null
            });
            setRequestMessage(`Application for ${role} submitted successfully.`);
        } catch (err) {
            console.error('Error submitting organization request', err);
            setRequestMessage('Failed to submit organization request.');
        }
    };

    // Выход из организации
    const handleLeaveOrganization = async () => {
        try {
            // DELETE /organization/:orgId/DeleteMember/:userId – удаляет участника из организации
            await axiosInstance.delete(`/organization/${organization.id}/deleteMember/${user?.id}`);
            // После выхода обновляем данные организации
            const response = await axiosInstance.get(`/organization/${organization.id}`);
            setOrganization(response.data);
        } catch (err) {
            console.error('Error leaving organization', err);
        }
    };

    // Функция создания курса в организации (доступна для админа)
    const handleCreateCourse = async (e: React.FormEvent) => {
        e.preventDefault();
        setCourseCreateMessage(null);
        setCourseCreateError(null);

        if (!courseName.trim()) {
            setCourseCreateError('Course name is required.');
            return;
        }

        try {
            // POST /course?organization={organization.id}
            // Отправляем { name: courseName, teacher: { id: selectedTeacherId } } если выбран учитель
            const payload: any = {name: courseName};
            if (selectedTeacherId !== '') {
                payload.teacher = {id: selectedTeacherId};
            }
            const response = await axiosInstance.post(`/course?organization=${organization.id}`, payload);
            // В ответ получаем созданный объект CourseDto
            setCourseCreateMessage('Course created successfully.');
            // Можно перенаправить на страницу созданного курса:
            navigate(`/courses/${response.data.id}`);
        } catch (err) {
            console.error('Error creating course', err);
            setCourseCreateError('Failed to create course.');
        }
    };

    // Функция для обновления (назначения/изменения/удаления) учителя для курса
    const handleUpdateTeacher = async (e: React.FormEvent) => {
        e.preventDefault();
        if (managingCourseId === null) return;
        try {
            // PUT /courses/:courseId – отправляем { teacher: { id: selectedTeacherId } } или { teacher: null }

            const url = selectedTeacherId === '' ? `/deleteTeacher` : `/setTeacher/${selectedTeacherId}`;
            await axiosInstance.put(`course/${managingCourseId}${url} `);
            setUpdateMessage('Teacher updated successfully.');
            // Обновляем список курсов
            const response = await axiosInstance.get(`/course/getAllFromOrganization/${organization.id}`);
            setCourses(response.data);
            setManagingCourseId(null);
        } catch (err) {
            console.error('Error updating teacher', err);
            setUpdateMessage('Failed to update teacher.');
        }
    };

    // Вычисляем список кандидатов для учителя из участников с ролью TEACHER или ADMIN
    const eligibleTeachers = organization.memberOrganizations.filter(
        (m: MemberDto) => m.roleInOrganization === 'TEACHER' || m.roleInOrganization === 'ADMIN'
    );

    return (
        <div className={styles.container}>
            <h2>{organization.name}</h2>
            <p>
                Owner: {organization.owner.firstName} {organization.owner.lastName}
            </p>
            <p>Created At: {new Date(organization.createdAt).toLocaleDateString()}</p>
            <h3>Members</h3>
            <ul className={styles.memberList}>
                {organization.memberOrganizations.map((member) => (
                    <li key={member.id}>
                        {member.member.firstName} {member.member.lastName} - {member.roleInOrganization}
                    </li>
                ))}
            </ul>
            {/* Если пользователь не является TEACHER или ADMIN, показываем варианты подачи заявки */}
            {!isPrivileged && (
                <div className={styles.requestSection}>
                    <h3>Apply for Membership</h3>
                    <button onClick={() => handleOrgRequest('TEACHER')}>Apply as Teacher</button>
                    <button onClick={() => handleOrgRequest('ADMIN')}>Apply as Admin</button>
                    <p>If you want to enroll as a student, please view available courses.</p>
                    <button onClick={() => navigate(`/organizations/${organization.id}/courses`)}>View Courses for
                        Enrollment
                    </button>
                    {requestMessage && <p>{requestMessage}</p>}
                </div>
            )}
            {/* Если пользователь уже является членом, даём возможность выйти из организации */}
            {isMember && (
                <div className={styles.membershipActions}>
                    <button onClick={handleLeaveOrganization}>Leave Organization</button>
                    {member?.roleInOrganization === 'ADMIN' && (
                        <>
                            <button onClick={() => navigate(`/organizations/${organization.id}/members`)}>Manage
                                Members
                            </button>
                            <button onClick={() => navigate(`/requests?type=organization&id=${organization.id}`)}>Manage
                                Requests
                            </button>

                        </>
                    )}
                </div>
            )}
            {/* Если пользователь является админом, показываем возможность создать курс */}
            {isAdmin && (
                <div className={styles.createCourseSection}>
                    <h3>Create New Course</h3>
                    {!showCreateCourseForm ? (
                        <button onClick={() => setShowCreateCourseForm(true)}>Create Course</button>
                    ) : (
                        <form onSubmit={handleCreateCourse} className={styles.createCourseForm}>
                            <div className={styles.inputGroup}>
                                <label>Course Name:</label>
                                <input
                                    type="text"
                                    value={courseName}
                                    onChange={(e) => setCourseName(e.target.value)}
                                    required
                                />
                            </div>
                            <div className={styles.inputGroup}>
                                <label>Teacher (optional):</label>
                                <select
                                    value={selectedTeacherId}
                                    onChange={(e) => setSelectedTeacherId(e.target.value ? Number(e.target.value) : '')}
                                >
                                    <option value="">-- No Teacher --</option>
                                    {eligibleTeachers.map((m) => (
                                        <option key={m.member.id} value={m.member.id}>
                                            {m.member.firstName} {m.member.lastName} ({m.roleInOrganization})
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <button type="submit">Submit</button>
                            <button type="button" onClick={() => setShowCreateCourseForm(false)}>
                                Cancel
                            </button>
                            {courseCreateError && <p className={styles.error}>{courseCreateError}</p>}
                            {courseCreateMessage && <p className={styles.success}>{courseCreateMessage}</p>}
                        </form>
                    )}
                    {/* Добавляем кнопку для перехода к просмотру всех курсов организации */}
                    <button onClick={() => navigate(`/organizations/${organization.id}/courses`)}
                            className={styles.viewCoursesButton}>
                        View All Courses
                    </button>
                </div>
            )}
            {/* Если пользователь является админ, добавляем секцию управления курсами */}
            {isAdmin && (
                <div className={styles.manageCoursesSection}>
                    <h3>Manage Courses</h3>
                    {loadingCourses ? (
                        <div>Loading courses...</div>
                    ) : (
                        <>
                            {courses.length === 0 ? (
                                <p>No courses found.</p>
                            ) : (
                                <ul className={styles.courseList}>
                                    {courses.map((course) => (
                                        <li key={course.id} className={styles.courseItem}>
                                            <span onClick={() => navigate(`/courses/${course.id}`)}
                                                  className={styles.courseName}>
                                                {course.name}
                                            </span>
                                            <button onClick={() => setManagingCourseId(course.id)}>
                                                Manage Teacher
                                            </button>
                                        </li>
                                    ))}
                                </ul>
                            )}
                        </>
                    )}
                    {managingCourseId && (
                        <form onSubmit={handleUpdateTeacher} className={styles.manageTeacherForm}>
                            <h4>Update Teacher for Course ID: {managingCourseId}</h4>
                            <select
                                value={selectedTeacherId}
                                onChange={(e) => setSelectedTeacherId(e.target.value ? Number(e.target.value) : '')}
                            >
                                <option value="">-- Remove Teacher --</option>
                                {eligibleTeachers.map((m) => (
                                    <option key={m.member.id} value={m.member.id}>
                                        {m.member.firstName} {m.member.lastName} ({m.roleInOrganization})
                                    </option>
                                ))}
                            </select>
                            <button type="submit">Submit</button>
                            <button type="button" onClick={() => setManagingCourseId(null)}>
                                Cancel
                            </button>
                            {updateMessage && <p>{updateMessage}</p>}
                        </form>
                    )}
                </div>
            )}
        </div>
    );
};

export default OrganizationDetailPage;
