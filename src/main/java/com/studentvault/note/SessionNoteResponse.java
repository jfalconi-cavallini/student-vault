package com.studentvault.note;

import java.time.Instant;
import java.util.UUID;

public record SessionNoteResponse(
        UUID id,
        UUID studentId,
        UUID tutorId,
        String topic,
        String notes,
        Instant createdAt
) {
    public static SessionNoteResponse from(SessionNote n) {
        return new SessionNoteResponse(
                n.getId(),
                n.getStudent().getId(),   // proxy id: no extra query
                n.getTutor().getId(),
                n.getTopic(),
                n.getNotes(),
                n.getCreatedAt());
    }
}