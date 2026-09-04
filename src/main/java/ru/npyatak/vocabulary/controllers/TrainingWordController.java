package ru.npyatak.vocabulary.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ru.npyatak.vocabulary.dto.AddWordToTrainingRequest;
import ru.npyatak.vocabulary.dto.TrainingWordLinkDTO;
import ru.npyatak.vocabulary.dto.UpdateStudyStatusRequest;
import ru.npyatak.vocabulary.dto.WordDTO;
import ru.npyatak.vocabulary.exceptions.LanguageNotSupportedException;
import ru.npyatak.vocabulary.models.Training;
import ru.npyatak.vocabulary.models.Word;
import ru.npyatak.vocabulary.models.WordTrainingLink;
import ru.npyatak.vocabulary.services.TrainingWordService;
import ru.npyatak.vocabulary.services.WordService;

/**
 *
 *
 * @author natalapatak
 * @since 04.09.2026
 */
@RestController
@RequestMapping("/api/training-words")
public class TrainingWordController {

    private final TrainingWordService trainingWordService;
    private final Map<String, WordService> wordServicesMap;
    private final String defaultLanguage;

    @Autowired
    public TrainingWordController(TrainingWordService trainingWordService,
            List<WordService> wordServices,
            @Value("${language}") String defaultLanguage) {
        this.trainingWordService = trainingWordService;
        this.defaultLanguage = defaultLanguage;
        this.wordServicesMap = new HashMap<>();
        wordServices.forEach(service ->
                wordServicesMap.put(service.getLanguage(), service));
    }

    /**
     * Добавить слово в тренировку
     * POST /api/training-words
     */
    @PostMapping
    public ResponseEntity<TrainingWordLinkDTO> addWordToTraining(
            @RequestBody @Valid AddWordToTrainingRequest request) throws LanguageNotSupportedException
    {

        WordService wordService = getWordService(request.getLanguage());
        Word word = wordService.getWordById(request.getWordId());
        Training training = trainingWordService.getTrainingById(request.getTrainingId());

        WordTrainingLink link = trainingWordService.addWordToTraining(word, training);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TrainingWordLinkDTO.fromEntity(link));
    }

    /**
     * Добавить слово во все тренировки
     * POST /api/training-words/{wordId}/all-trainings
     */
    @PostMapping("/{wordId}/all-trainings")
    public ResponseEntity<List<TrainingWordLinkDTO>> addWordToAllTrainings(
            @PathVariable Long wordId,
            @RequestParam(required = false) String language) throws LanguageNotSupportedException
    {

        String lang = language != null ? language : defaultLanguage;
        WordService wordService = getWordService(lang);
        Word word = wordService.getWordById(wordId);

        List<Training> allTrainings = trainingWordService.getAllTrainings();
        List<WordTrainingLink> links =
                trainingWordService.addWordToAllTrainings(word, allTrainings);

        List<TrainingWordLinkDTO> dtos = links.stream()
                .map(TrainingWordLinkDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Получить слова для тренировки
     * GET /api/training-words/training/{trainingId}/words
     */
    @GetMapping("/training/{trainingId}/words")
    public ResponseEntity<List<WordDTO>> getWordsForTraining(
            @PathVariable Long trainingId,
            @RequestParam(required = false) String language) {

        String lang = language != null ? language : defaultLanguage;
        List<Word> words =
                trainingWordService.getWordsByTrainingIdAndLanguage(trainingId, lang);

        List<WordDTO> dtos = words.stream()
                .map(WordDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Получить слова для изучения
     * GET /api/training-words/training/{trainingId}/study
     */
    @GetMapping("/training/{trainingId}/study")
    public ResponseEntity<List<WordDTO>> getWordsForStudy(
            @PathVariable Long trainingId,
            @RequestParam(required = false) String language) {

        String lang = language != null ? language : defaultLanguage;
        LocalDate today = LocalDate.now();

        List<Word> words =
                trainingWordService.getWordsForStudyForDate(today, trainingId);

        List<WordDTO> dtos = words.stream()
                .map(WordDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Обновить статус изучения слова
     * PUT /api/training-words/status
     */
    @PutMapping("/status")
    public ResponseEntity<TrainingWordLinkDTO> updateStudyStatus(
            @RequestBody @Valid UpdateStudyStatusRequest request) throws LanguageNotSupportedException
    {

        WordService wordService = getWordService(request.getLanguage());
        Word word = wordService.getWordById(request.getWordId());
        Training training = trainingWordService.getTrainingById(request.getTrainingId());

        WordTrainingLink link = trainingWordService.addStudyStatus(
                word,
                training,
                LocalDate.now(),
                request.getRepeatDays()
        );

        return ResponseEntity.ok(TrainingWordLinkDTO.fromEntity(link));
    }

    /**
     * Удалить слово из тренировки
     * DELETE /api/training-words/{wordId}/training/{trainingId}
     */
    @DeleteMapping("/{wordId}/training/{trainingId}")
    public ResponseEntity<Void> removeWordFromTraining(
            @PathVariable Long wordId,
            @PathVariable Long trainingId,
            @PathVariable String language) throws LanguageNotSupportedException
    {

        trainingWordService.removeWordFromTraining(wordId, trainingId, language);
        return ResponseEntity.noContent().build();
    }

    private WordService getWordService(String language) throws LanguageNotSupportedException
    {
        WordService service = wordServicesMap.get(language);
        if (service == null) {
            throw new LanguageNotSupportedException("Language not supported: " + language);
        }
        return service;
    }
}
