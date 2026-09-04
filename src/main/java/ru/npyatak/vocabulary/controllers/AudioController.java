package ru.npyatak.vocabulary.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.npyatak.vocabulary.models.Word;
import ru.npyatak.vocabulary.services.audio.AudioService;
import ru.npyatak.vocabulary.services.WordService;

import java.util.List;

/**
 *
 *
 * @author natalapatak
 * @since 04.09.2026
 */
@RestController
@RequestMapping("/api/audio")
public class AudioController {

    private final AudioService audioService;
    private final List<WordService> wordServices;
    private final String defaultLanguage;

    public AudioController(AudioService audioService,
            List<WordService> wordServices,
            @Value("${language}") String defaultLanguage) {
        this.audioService = audioService;
        this.wordServices = wordServices;
        this.defaultLanguage = defaultLanguage;
    }

    /**
     * Получить аудио для слова по ID
     * GET /api/audio/word/{wordId}
     */
    @GetMapping("/word/{wordId}")
    public ResponseEntity<byte[]> getWordAudio(@PathVariable Long wordId) {
        Word word = findWordById(wordId);

        byte[] audioData = audioService.getWordAudio(
                word.getWord(),
                word.getLanguage() != null ? word.getLanguage() : defaultLanguage
        );

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header("Cache-Control", "public, max-age=86400")
                .body(audioData);
    }

    /**
     * Получить аудио для текста
     * GET /api/audio/text?text=hello&language=en
     */
    @GetMapping("/text")
    public ResponseEntity<byte[]> getTextAudio(
            @RequestParam String text,
            @RequestParam(defaultValue = "en") String language) {

        byte[] audioData = audioService.getWordAudio(text, language);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(audioData);
    }

    /**
     * Проверить работу TTS API
     * GET /api/audio/test
     */
    @GetMapping("/test")
    public ResponseEntity<String> testTTS() {
        try {
            byte[] audioData = audioService.getWordAudio("hello", "en");
            return ResponseEntity.ok(
                    "TTS API works! Audio size: " + audioData.length + " bytes"
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("TTS API failed: " + e.getMessage());
        }
    }

    private Word findWordById(Long wordId) {
        for (WordService wordService : wordServices) {
            try {
                return wordService.getWordById(wordId);
            } catch (Exception e) {
                // Продолжаем поиск в других сервисах
            }
        }
        throw new RuntimeException("Word not found with id: " + wordId);
    }
}
