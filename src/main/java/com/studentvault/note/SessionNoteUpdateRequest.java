package com.studentvault.note;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SessionNoteUpdateRequest(
        @NotBlank @Size(max = 200) String topic,
        @NotBlank @Size(max = 10_000) String notes
) {}