package com.khaled.survey_backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SubmitResponseDTO(
                @NotNull Long surveyId,
                @Size(min = 5, max = 5) List<AnswerDTO> answers) {
}
