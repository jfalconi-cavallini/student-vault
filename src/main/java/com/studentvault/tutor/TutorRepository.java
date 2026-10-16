package com.studentvault.tutor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TutorRepository extends JpaRepository<Tutor, UUID> {
    boolean existsByEmail(String email);
}