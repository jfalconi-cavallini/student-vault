CREATE TABLE tutors (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    subjects    TEXT[]       NOT NULL DEFAULT '{}',
    archived    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE students (
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name               VARCHAR(100) NOT NULL,
    email              VARCHAR(255) NOT NULL UNIQUE,
    grade              VARCHAR(20),
    subjects           TEXT[]       NOT NULL DEFAULT '{}',
    assigned_tutor_id  UUID REFERENCES tutors (id),
    archived           BOOLEAN      NOT NULL DEFAULT FALSE,
    parent_name        VARCHAR(100),
    parent_email       VARCHAR(255),
    timezone           VARCHAR(64)  NOT NULL DEFAULT 'America/Chicago',
    created_at         TIMESTAMPTZ  NOT NULL DEFAULT now()
);

-- Postgres doesn't index foreign keys automatically; we'll query
-- "all students for tutor X" constantly, so index it.
CREATE INDEX idx_students_assigned_tutor ON students (assigned_tutor_id);