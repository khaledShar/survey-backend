package com.khaled.survey_backend.repository;

import com.khaled.survey_backend.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySurveyIdOrderByPositionAsc(Long surveyId);
}
