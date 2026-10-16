package com.studentvault.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record StudentRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Email @Size(max = 255) String email,
        @Size(max = 20) String grade,
        List<@NotBlank @Size(max = 50) String> subjects,
        UUID assignedTutorId,                       // optional: a student can be unassigned
        @Size(max = 100) String parentName,
        @Email @Size(max = 255) String parentEmail,
        @Size(max = 64) String timezone             // e.g. "America/Chicago"; defaults if omitted
) {}