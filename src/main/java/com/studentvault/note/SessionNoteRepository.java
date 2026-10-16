package com.studentvault.note;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SessionNoteRepository extends JpaRepository<SessionNote, UUID> {

    Page<SessionNote> findByStudentId(UUID studentId, Pageable pageable);

    // Finds the note only if it belongs to this student, preventing IDOR.
    Optional<SessionNote> findByIdAndStudentId(UUID id, UUID studentId);
}