package com.khaled.survey_backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AnswerDTO(
                @NotNull Long questionId,
                @Min(1) @Max(5) int score) {
}
