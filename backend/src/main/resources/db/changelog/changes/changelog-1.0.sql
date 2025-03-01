CREATE TABLE users
(
    user_id       SERIAL PRIMARY KEY,
    first_name    VARCHAR(50)         NOT NULL,
    last_name     VARCHAR(50)         NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    password_hash TEXT                NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE organizations
(
    organization_id SERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    owner_id        INT REFERENCES users (user_id) ON DELETE CASCADE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE organization_members
(
    member_id       SERIAL PRIMARY KEY,
    user_id         INT REFERENCES users (user_id) ON DELETE CASCADE,
    organization_id INT REFERENCES organizations (organization_id) ON DELETE CASCADE,
    role_in_org     VARCHAR(20) CHECK (role_in_org IN ('STUDENT', 'TEACHER', 'ADMIN')) NOT NULL,
    joined_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE course
(
    course_id       SERIAL PRIMARY KEY,
    organization_id INT REFERENCES organizations (organization_id) ON DELETE CASCADE,
    name            VARCHAR(100) NOT NULL,
    teacher_id      INT REFERENCES users (user_id),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE course_students
(
    course_student_id SERIAL PRIMARY KEY,
    course_id         INT REFERENCES course (course_id) ON DELETE CASCADE,
    student_id        INT REFERENCES users (user_id) ON DELETE CASCADE,
    joined_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE grades
(
    grade_id   SERIAL PRIMARY KEY,
    course_id  INT REFERENCES course (course_id) ON DELETE CASCADE,
    student_id INT REFERENCES users (user_id) ON DELETE CASCADE,
    teacher_id INT REFERENCES users (user_id),
    grade numeric(4,1) CHECK (grade >= 0 AND grade <= 100),
    grade_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    comment    TEXT
);

CREATE TABLE requests
(
    request_id      SERIAL PRIMARY KEY,
    user_id         INT REFERENCES users (user_id) ON DELETE CASCADE,
    organization_id INT REFERENCES organizations (organization_id) ON DELETE CASCADE ,
    course_id       INT REFERENCES course (course_id) ON DELETE CASCADE ,
    role_in_org     VARCHAR(20) CHECK (role_in_org IN ('STUDENT', 'TEACHER', 'ADMIN')) NOT NULL,
    status          VARCHAR(20) NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_at    TIMESTAMP
);