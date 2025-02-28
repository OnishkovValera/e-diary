// src/types/index.ts

export interface UserDto {
    id: number;
    firstName: string;
    lastName: string;
    createdAt: string; // ISO строка даты
}

export interface CourseDto {
    id: number;
    name: string;
    teacher: UserDto;
    createdAt: string;
    students: UserDto[];
}

export interface GradeDto {
    id: number;
    course: CourseDto;
    student: UserDto;
    teacher: UserDto;
    grade: number; // от 0 до 100
    gradeDateTime: string;
    comment: string;
}

export interface MemberDto {
    id: number;
    member: UserDto;
    roleInOrganization: string; // например: 'admin', 'teacher'
    joinedAt: string;
}

export interface OrganizationDto {
    id: number;
    name: string;
    owner: UserDto;
    createdAt: string;
    memberOrganizations: MemberDto[];
}

export interface RequestDto {
    id: number;
    user: UserDto;
    organization: OrganizationDto;
    course?: CourseDto;
    role: string; // роль, которую пользователь хочет получить
}
