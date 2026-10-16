package com.studentvault.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    Page<Student> findByAssignedTutorId(UUID tutorId, Pageable pageable);
    boolean existsByEmail(String email);
}