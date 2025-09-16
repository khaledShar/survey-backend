package com.khaled.survey_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateSurveyDTO(
        @NotBlank String title,
        @Size(min = 5, max = 5) List<@NotBlank String> questions) {
}
