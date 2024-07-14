package com.magnoliacms.domain.note.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateNewNoteVersionRequest(
        @NotBlank
        @Length(min = 1, max = 5000)
        String content) {
}
