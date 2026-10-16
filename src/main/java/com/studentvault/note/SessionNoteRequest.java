package com.studentvault.note;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record SessionNoteRequest(
        @NotNull UUID tutorId,                       // TEMPORARY: becomes the logged-in tutor once auth exists
        @NotBlank @Size(max = 200) String topic,
        @NotBlank @Size(max = 10_000) String notes
) {}