package com.studentvault.student;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record StudentResponse(
        UUID id,
        String name,
        String email,
        String grade,
        List<String> subjects,
        UUID assignedTutorId,
        boolean archived,
        String parentName,
        String parentEmail,
        String timezone,
        Instant createdAt
) {
    public static StudentResponse from(Student s) {
        return new StudentResponse(
                s.getId(),
                s.getName(),
                s.getEmail(),
                s.getGrade(),
                List.copyOf(s.getSubjects()),
                s.getAssignedTutor() == null ? null : s.getAssignedTutor().getId(),
                s.isArchived(),
                s.getParentName(),
                s.getParentEmail(),
                s.getTimezone(),
                s.getCreatedAt());
    }
}