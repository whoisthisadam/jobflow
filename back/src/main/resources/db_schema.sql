-- JobFlow Database Schema

-- Existing tables (commented for reference)
/*
CREATE TABLE app_user (
    id BIGINT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    fio VARCHAR(255) DEFAULT 'ФИО',
    date VARCHAR(255),
    exp INTEGER DEFAULT 0,
    img VARCHAR(1000) DEFAULT '/img/avatar.png',
    category_id BIGINT
);

CREATE TABLE category (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    sum FLOAT
);

CREATE TABLE task (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    date VARCHAR(255),
    intensity INTEGER,
    address VARCHAR(255),
    date_end VARCHAR(255),
    time VARCHAR(255),
    description VARCHAR(5000),
    status VARCHAR(50),
    user_id BIGINT
);

CREATE TABLE task_report (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    date VARCHAR(255),
    img VARCHAR(1000) DEFAULT '',
    file VARCHAR(1000) DEFAULT '',
    task_id BIGINT
);
*/

-- New tables to add to the schema

-- Roles table - Expanded role management beyond the basic enum
CREATE TABLE roles (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    permission_level INTEGER NOT NULL DEFAULT 1,
    can_create_tasks BOOLEAN NOT NULL DEFAULT FALSE,
    can_assign_tasks BOOLEAN NOT NULL DEFAULT FALSE,
    can_view_reports BOOLEAN NOT NULL DEFAULT TRUE,
    can_manage_users BOOLEAN NOT NULL DEFAULT FALSE,
    can_manage_categories BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Credentials table - Enhanced security and authentication
CREATE TABLE credentials (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    password_salt VARCHAR(255) NOT NULL,
    password_reset_token VARCHAR(255),
    password_reset_expires TIMESTAMP WITH TIME ZONE,
    account_locked BOOLEAN NOT NULL DEFAULT FALSE,
    account_expired BOOLEAN NOT NULL DEFAULT FALSE,
    credentials_expired BOOLEAN NOT NULL DEFAULT FALSE,
    last_login TIMESTAMP WITH TIME ZONE,
    failed_login_attempts INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_credentials_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
);

-- Grades table - Track user performance and skill levels
CREATE TABLE grades (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL,
    category_id BIGINT,
    skill_level INTEGER NOT NULL DEFAULT 1,
    performance_score FLOAT NOT NULL DEFAULT 0.0,
    reliability_score FLOAT NOT NULL DEFAULT 0.0,
    quality_score FLOAT NOT NULL DEFAULT 0.0,
    speed_score FLOAT NOT NULL DEFAULT 0.0,
    total_tasks_completed INTEGER NOT NULL DEFAULT 0,
    total_tasks_failed INTEGER NOT NULL DEFAULT 0,
    last_evaluation_date TIMESTAMP WITH TIME ZONE,
    evaluator_id BIGINT,
    comments VARCHAR(1000),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_grades_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_grades_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL,
    CONSTRAINT fk_grades_evaluator FOREIGN KEY (evaluator_id) REFERENCES app_user(id) ON DELETE SET NULL
);

-- Limits table - Set constraints for different user categories or roles
CREATE TABLE limits (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    category_id BIGINT,
    role_id BIGINT,
    max_tasks_per_day INTEGER NOT NULL DEFAULT 5,
    max_tasks_per_week INTEGER NOT NULL DEFAULT 25,
    max_task_intensity INTEGER NOT NULL DEFAULT 10,
    min_experience_required INTEGER NOT NULL DEFAULT 0,
    max_payment_amount FLOAT,
    max_file_size_mb INTEGER NOT NULL DEFAULT 10,
    cooldown_between_tasks_minutes INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_limits_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
    CONSTRAINT fk_limits_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    CONSTRAINT unique_category_role UNIQUE (category_id, role_id)
);

-- Insert default roles
INSERT INTO roles (name, description, permission_level, can_create_tasks, can_assign_tasks, can_view_reports, can_manage_users, can_manage_categories)
VALUES
('ADMIN', 'Full system administrator with all permissions', 100, TRUE, TRUE, TRUE, TRUE, TRUE),
('MANAGER', 'Task manager who can create and assign tasks', 50, TRUE, TRUE, TRUE, FALSE, TRUE),
('USER', 'Regular user who can complete assigned tasks', 10, FALSE, FALSE, TRUE, FALSE, FALSE);

-- Insert default limits for each role
INSERT INTO limits (role_id, max_tasks_per_day, max_tasks_per_week, max_task_intensity, min_experience_required)
VALUES
((SELECT id FROM roles WHERE name = 'ADMIN'), 999, 999, 10, 0),
((SELECT id FROM roles WHERE name = 'MANAGER'), 20, 100, 8, 100),
((SELECT id FROM roles WHERE name = 'USER'), 5, 25, 5, 0);

-- User Salary table - Store salary data for users by month
CREATE TABLE user_salary (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL,
    year INTEGER NOT NULL,
    month INTEGER NOT NULL,
    base_salary FLOAT NOT NULL,
    task_intensity_bonus FLOAT NOT NULL,
    experience_bonus FLOAT NOT NULL,
    income_tax FLOAT NOT NULL,
    cpp_tax FLOAT NOT NULL,
    other_deductions FLOAT NOT NULL,
    total_salary FLOAT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_salary_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_salary_creator FOREIGN KEY (created_by) REFERENCES app_user(id),
    CONSTRAINT unique_user_year_month UNIQUE (user_id, year, month)
);
