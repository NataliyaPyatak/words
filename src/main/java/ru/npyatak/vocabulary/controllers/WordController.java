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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ru.npyatak.vocabulary.dto.WordDTO;
import ru.npyatak.vocabulary.dto.WordRequest;
import ru.npyatak.vocabulary.exceptions.LanguageNotSupportedException;
import ru.npyatak.vocabulary.exceptions.WordNotFoundException;
import ru.npyatak.vocabulary.models.Training;
import ru.npyatak.vocabulary.models.Word;
import ru.npyatak.vocabulary.models.WordTrainingLink;
import ru.npyatak.vocabulary.services.TrainingService;
import ru.npyatak.vocabulary.services.TrainingWordService;
import ru.npyatak.vocabulary.services.WordService;

@RestController
@RequestMapping("/api/words")
public class WordController
{

    private final Map<String, WordService> wordServicesMap;
    private final String defaultLanguage;

    @Autowired
    public WordController(List<WordService> wordServices,
            @Value("${language}") String defaultLanguage)
    {
        this.defaultLanguage = defaultLanguage;
        this.wordServicesMap = new HashMap<>();
        wordServices.forEach(service ->
                wordServicesMap.put(service.getLanguage(), service));
    }

    /**
     * Получить все слова
     * GET /api/words?language=en
     */
    @GetMapping
    public ResponseEntity<List<WordDTO>> getAllWords(@RequestParam(required = false) String language)
            throws LanguageNotSupportedException
    {
        String lang = language != null ? language : defaultLanguage;
        WordService wordService = getWordService(lang);

        List<WordDTO> words = wordService.getAllWords().stream()
                .map(WordDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(words);
    }

    /**
     * Добавить новое слово
     * POST /api/words
     */
    @PostMapping
    public ResponseEntity<WordDTO> addWord(@RequestBody @Valid WordRequest request) throws LanguageNotSupportedException
    {
        Word newWord = new Word(request.getWord(), request.getTranslation());
        newWord.setLanguage(request.getLanguage() != null ?
                request.getLanguage() : defaultLanguage);

        WordService wordService = getWordService(newWord.getLanguage());
        Word savedWord = wordService.saveWord(newWord);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(WordDTO.fromEntity(savedWord));
    }

    /**
     * Получить слово по ID
     * GET /api/words/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<WordDTO> getWord(@PathVariable Long id)
    {
        // Ищем слово во всех языках
        for (WordService service : wordServicesMap.values())
        {
            Word word = service.getWordById(id);
            return ResponseEntity.ok(WordDTO.fromEntity(word));
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Обновить слово
     * PUT /api/words/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<WordDTO> updateWord(
            @PathVariable Long id,
            @RequestBody @Valid WordRequest request) throws LanguageNotSupportedException
    {

        WordService wordService = getWordService(request.getLanguage());
        Word word = wordService.getWordById(id);

        word.setWord(request.getWord());
        word.setTranslation(request.getTranslation());
        word.setLanguage(request.getLanguage());

        Word updatedWord = wordService.saveWord(word);
        return ResponseEntity.ok(WordDTO.fromEntity(updatedWord));
    }

    /**
     * Удалить слово
     * DELETE /api/words/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWord(@PathVariable Long id)
    {
        for (WordService service : wordServicesMap.values())
        {
            service.deleteWord(id);
        }
        return ResponseEntity.noContent().build();
    }

    private WordService getWordService(String language) throws LanguageNotSupportedException
    {
        WordService service = wordServicesMap.get(language);
        if (service == null)
        {
            throw new LanguageNotSupportedException("Language not supported: " + language);
        }
        return service;
    }
}