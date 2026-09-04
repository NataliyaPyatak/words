package ru.npyatak.vocabulary.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ru.npyatak.vocabulary.dto.TrainingDTO;
import ru.npyatak.vocabulary.dto.TrainingRequest;
import ru.npyatak.vocabulary.models.Training;
import ru.npyatak.vocabulary.services.TrainingService;

/**
 *
 *
 * @author natalapatak
 * @since 04.09.2026
 */
@RestController
@RequestMapping("/api/trainings")
public class TrainingController
{

    private final TrainingService trainingService;

    @Autowired
    public TrainingController(TrainingService trainingService)
    {
        this.trainingService = trainingService;
    }

    /**
     * Получить все тренировки
     * GET /api/trainings
     */
    @GetMapping
    public ResponseEntity<List<TrainingDTO>> getAllTrainings()
    {
        List<TrainingDTO> trainings = trainingService.getAllTrainings().stream()
                .map(TrainingDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(trainings);
    }

    /**
     * Создать новую тренировку
     * POST /api/trainings
     */
    @PostMapping
    public ResponseEntity<TrainingDTO> createTraining(@RequestBody @Valid TrainingRequest request)
    {

        Training training = trainingService.saveTraining(request.getName());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TrainingDTO.fromEntity(training));
    }

    /**
     * Получить тренировку по ID
     * GET /api/trainings/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrainingDTO> getTraining(@PathVariable Long id)
    {
        Training training = trainingService.getById(id);
        return ResponseEntity.ok(TrainingDTO.fromEntity(training));
    }

    /**
     * Удалить тренировку
     * DELETE /api/trainings/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable Long id)
    {
        trainingService.deleteTraining(id);
        return ResponseEntity.noContent().build();
    }
}
