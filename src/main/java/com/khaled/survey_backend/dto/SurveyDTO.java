package com.khaled.survey_backend.dto;

import java.util.List;

public record SurveyDTO(Long id, String title, List<QuestionDTO> questions) {
    public record QuestionDTO(Long id, String text, int position) {
    }
}
