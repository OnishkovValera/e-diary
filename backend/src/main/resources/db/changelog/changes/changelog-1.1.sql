CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

CREATE INDEX idx_organization_members_user_id ON organization_members(user_id);
CREATE INDEX idx_organization_members_organization_id ON organization_members(organization_id);

CREATE INDEX idx_course_id ON course(name);
CREATE INDEX idx_course_organisation_id ON course(organization_id);
CREATE INDEX idx_course_teacher_id ON course(teacher_id);