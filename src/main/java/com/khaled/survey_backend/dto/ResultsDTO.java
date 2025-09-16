package com.khaled.survey_backend.dto;

import java.util.List;
import java.util.Map;

public record ResultsDTO(
                Long surveyId,
                List<QuestionResultDTO> results) {
        public record QuestionResultDTO(
                        Long questionId,
                        String text,
                        double average,
                        Map<Integer, Long> distribution) {
        }
}
