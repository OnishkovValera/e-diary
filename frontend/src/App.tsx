// src/App.tsx
import {useEffect} from 'react';
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import {useUserStore} from './globalStore/GlobalStore';
import ProtectedRoute from './components/security/ProtectedRoute';

import LoginPage from './components/auth/login/LoginPage';
import RegisterPage from './components/auth/register/RegisterPage';
import AccountPage from './components/workingPages/accountPage/AccountPage';
import EditProfilePage from './components/workingPages/editProfilePage/EditProfilePage';
import AdminOrganizationsPage from './components/workingPages/admin/AdminOrganizationsPage';
import OrganizationDetailPage from './components/workingPages/organization/organization/OrganizationDetailPage';
import TeacherCoursesPage from './components/workingPages/teacher/TeacherCoursesPage';
import StudentCoursesPage from './components/workingPages/student/StudentCoursesPage';
import CourseDetailPage from './components/workingPages/details/course/CourseDetailPage';
import SearchOrganizationsPage from './components/workingPages/organization/searchOrganization/SearchOrganizationsPage';
import OrganizationCoursesPage
    from "./components/workingPages/organization/organizationCourse/OrganizationCoursesPage.tsx";
import OrganizationMembersPage
    from "./components/workingPages/organization/organizationMembersPage/OrganizationMembersPage.tsx";
import RequestManagementPage from "./components/workingPages/requestManagement/RequestManagementPage.tsx";
import CreateOrganizationPage
    from "./components/workingPages/organization/createOrganizationPage/CreateOrganizationPage.tsx";
import StudentGradeHistoryPage from "./components/workingPages/studentGradeHistory/StudentGradeHistoryPage.tsx";


const App = () => {
    const checkAuth = useUserStore((state) => state.checkAuth);
    useEffect(() => {
        checkAuth();
    }, [checkAuth]);

    return (
        <BrowserRouter>
            <Routes>

                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/register" element={<RegisterPage/>}/>
                <Route
                    path="/"
                    element={
                        <ProtectedRoute>
                            <AccountPage/>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/edit-profile"
                    element={
                        <ProtectedRoute>
                            <EditProfilePage/>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/organizations"
                    element={
                        <ProtectedRoute>
                            <AdminOrganizationsPage/>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/organizations/:id"
                    element={
                        <ProtectedRoute>
                            <OrganizationDetailPage/>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/courses/teacher"
                    element={
                        <ProtectedRoute>
                            <TeacherCoursesPage/>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/courses/student"
                    element={
                        <ProtectedRoute>
                            <StudentCoursesPage/>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/courses/:id"
                    element={
                        <ProtectedRoute>
                            <CourseDetailPage/>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/search-organizations"
                    element={
                        <ProtectedRoute>
                            <SearchOrganizationsPage/>
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/organizations/:id/courses"
                    element={
                        <ProtectedRoute>
                            <OrganizationCoursesPage/>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/organizations/:id/members"
                    element={
                        <ProtectedRoute>
                            <OrganizationMembersPage/>
                        </ProtectedRoute>
                    }
                />
                <Route path="/requests"
                       element={
                           <ProtectedRoute>
                               <RequestManagementPage/>
                           </ProtectedRoute>
                       }
                />
                <Route
                    path="/create-organization"
                    element={
                        <ProtectedRoute>
                            <CreateOrganizationPage/>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/courses/:courseId/history/:studentId"
                    element={
                        <ProtectedRoute>
                            <StudentGradeHistoryPage/>
                        </ProtectedRoute>
                    }
                />
            </Routes>
        </BrowserRouter>
    );
};

export default App;
