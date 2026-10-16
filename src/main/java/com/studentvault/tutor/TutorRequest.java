package com.studentvault.tutor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

// What clients send to create or update a tutor.
public record TutorRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Email @Size(max = 255) String email,
        List<@NotBlank @Size(max = 50) String> subjects
) {}