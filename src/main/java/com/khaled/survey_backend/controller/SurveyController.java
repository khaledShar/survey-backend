package com.khaled.survey_backend.controller;

import com.khaled.survey_backend.dto.ResultsDTO;
import com.khaled.survey_backend.dto.SubmitResponseDTO;
import com.khaled.survey_backend.dto.SurveyDTO;
import com.khaled.survey_backend.service.SurveyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    private final SurveyService service;

    public SurveyController(SurveyService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public SurveyDTO get(@PathVariable Long id) {
        return service.getSurvey(id);
    }

    @PostMapping("/{id}/responses")
    @ResponseStatus(HttpStatus.CREATED)
    public void submit(@PathVariable Long id, @Valid @RequestBody SubmitResponseDTO dto) {
        if (!id.equals(dto.surveyId())) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "surveyId mismatch");
        }
        service.submit(dto);
    }

    @GetMapping("/{id}/results")
    public ResultsDTO results(@PathVariable Long id) {
        return service.results(id);
    }
}
