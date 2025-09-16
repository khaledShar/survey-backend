package com.khaled.survey_backend.repository;

import com.khaled.survey_backend.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
