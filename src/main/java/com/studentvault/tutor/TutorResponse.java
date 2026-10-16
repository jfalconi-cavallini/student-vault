package com.studentvault.tutor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

// What the API sends back. No setters; records are immutable.
public record TutorResponse(
        UUID id,
        String name,
        String email,
        List<String> subjects,
        boolean archived,
        Instant createdAt
) {
    public static TutorResponse from(Tutor tutor) {
        return new TutorResponse(
                tutor.getId(),
                tutor.getName(),
                tutor.getEmail(),
                List.copyOf(tutor.getSubjects()),
                tutor.isArchived(),
                tutor.getCreatedAt());
    }
}