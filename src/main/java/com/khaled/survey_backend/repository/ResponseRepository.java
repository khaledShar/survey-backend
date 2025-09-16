package com.khaled.survey_backend.repository;

import com.khaled.survey_backend.model.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {

    interface ScoreBucket {
        Long getQuestionId();

        String getText();

        Integer getScore();

        Long getCnt();
    }

    @Query("""
            SELECT q.id as questionId, q.text as text, r.score as score, COUNT(r) as cnt
            FROM Response r JOIN r.question q
            WHERE r.survey.id = :surveyId
            GROUP BY q.id, q.text, r.score
            """)
    List<ScoreBucket> aggregate(Long surveyId);
}
