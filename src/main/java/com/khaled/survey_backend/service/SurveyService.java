package com.khaled.survey_backend.service;

import com.khaled.survey_backend.dto.*;
import com.khaled.survey_backend.model.Question;
import com.khaled.survey_backend.model.Response;
import com.khaled.survey_backend.model.Survey;
import com.khaled.survey_backend.repository.QuestionRepository;
import com.khaled.survey_backend.repository.ResponseRepository;
import com.khaled.survey_backend.repository.SurveyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SurveyService {

    private final SurveyRepository surveys;
    private final QuestionRepository questions;
    private final ResponseRepository responses;

    public SurveyService(SurveyRepository surveys, QuestionRepository questions, ResponseRepository responses) {
        this.surveys = surveys;
        this.questions = questions;
        this.responses = responses;
    }

    @Transactional(readOnly = true)
    public SurveyDTO getSurvey(Long id) {
        Survey survey = surveys.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found"));

        var q = questions.findBySurveyIdOrderByPositionAsc(id).stream()
                .map(x -> new SurveyDTO.QuestionDTO(x.getId(), x.getText(), x.getPosition()))
                .toList();

        return new SurveyDTO(survey.getId(), survey.getTitle(), q);
    }

    public void submit(SubmitResponseDTO dto) {
        Survey survey = surveys.findById(dto.surveyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found"));

        Set<Long> allowed = questions.findBySurveyIdOrderByPositionAsc(survey.getId()).stream()
                .map(Question::getId)
                .collect(Collectors.toSet());

        if (dto.answers().size() != allowed.size()
                || !dto.answers().stream().allMatch(a -> allowed.contains(a.questionId()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid question IDs");
        }

        Instant now = Instant.now();
        for (var a : dto.answers()) {
            Question qRef = new Question();
            qRef.setId(a.questionId());

            Response r = new Response();
            r.setSurvey(survey);
            r.setQuestion(qRef);
            r.setScore(a.score());
            r.setCreatedAt(now);
            responses.save(r);
        }
    }

    @Transactional(readOnly = true)
    public ResultsDTO results(Long surveyId) {
        surveys.findById(surveyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found"));

        var rows = responses.aggregate(surveyId);

        Map<Long, Map<Integer, Long>> distribution = new HashMap<>();
        Map<Long, String> labels = new HashMap<>();

        for (var row : rows) {
            Long qId = row.getQuestionId();
            String text = row.getText();
            int score = row.getScore();
            long cnt = row.getCnt();

            labels.put(qId, text);
            distribution
                    .computeIfAbsent(qId, k -> new HashMap<>())
                    .merge(score, cnt, Long::sum);
        }

        List<ResultsDTO.QuestionResultDTO> resultList = new ArrayList<>();

        if (distribution.isEmpty()) {
            var qs = questions.findBySurveyIdOrderByPositionAsc(surveyId);
            for (var q : qs) {
                Map<Integer, Long> empty = new TreeMap<>();
                for (int s = 1; s <= 5; s++)
                    empty.put(s, 0L);
                resultList.add(new ResultsDTO.QuestionResultDTO(q.getId(), q.getText(), 0.0, empty));
            }
        } else {
            for (var entry : distribution.entrySet()) {
                Long qId = entry.getKey();
                Map<Integer, Long> map = entry.getValue();

                for (int s = 1; s <= 5; s++)
                    map.putIfAbsent(s, 0L);

                long total = map.values().stream().mapToLong(Long::longValue).sum();
                double avg = (total == 0) ? 0.0
                        : map.entrySet().stream().mapToDouble(e -> e.getKey() * e.getValue()).sum() / total;

                resultList.add(new ResultsDTO.QuestionResultDTO(
                        qId,
                        labels.getOrDefault(qId, ""),
                        avg,
                        new TreeMap<>(map)));
            }
        }

        var order = questions.findBySurveyIdOrderByPositionAsc(surveyId).stream()
                .map(Question::getId).toList();

        resultList.sort(Comparator.comparingInt(o -> order.indexOf(o.questionId())));

        return new ResultsDTO(surveyId, resultList);
    }
}
