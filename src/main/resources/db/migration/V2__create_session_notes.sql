CREATE TABLE session_notes (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id  UUID         NOT NULL REFERENCES students (id),
    tutor_id    UUID         NOT NULL REFERENCES tutors (id),
    topic       VARCHAR(200) NOT NULL,
    notes       TEXT         NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

-- Main query: "this student's notes, newest first" -> composite index matches it exactly.
CREATE INDEX idx_session_notes_student_created ON session_notes (student_id, created_at DESC);