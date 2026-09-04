package ru.npyatak.vocabulary.services.audio;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 *
 * @author natalapatak
 * @since 04.09.2026
 */
@Service
public class GoogleTTSService implements AudioSevice
{
    private final RestTemplate restTemplate;
    private final Path audioCachePath;

    public GoogleTTSService()
    {
        this.restTemplate = new RestTemplate();
        this.audioCachePath = Paths.get("audio-cache");
        try
        {
            Files.createDirectories(audioCachePath);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to create audio cache directory", e);
        }
    }

    /**
     * Получить аудио для слова
     */
    public byte[] getWordAudio(String word, String language)
    {
        // Проверяем кэш
        Optional<byte[]> cachedAudio = getFromCache(word, language);
        if (cachedAudio.isPresent())
        {
            return cachedAudio.get();
        }

        // Генерируем через Google TTS
        byte[] audioData = generateFromGoogleTTS(word, language);

        // Сохраняем в кэш
        saveToCache(word, language, audioData);

        return audioData;
    }

    /**
     * Генерация аудио через Google TTS
     */
    private byte[] generateFromGoogleTTS(String word, String language)
    {
        try
        {
            String url = "https://translate.google.com/translate_tts" +
                    "?ie=UTF-8" +
                    "&q=" + URLEncoder.encode(word, StandardCharsets.UTF_8) +
                    "&tl=" + language +
                    "&client=tw-ob";

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    byte[].class
            );

            return response.getBody();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to generate audio for word: " + word, e);
        }
    }

    /**
     * Сохранение в файловый кэш
     */
    private void saveToCache(String word, String language, byte[] audioData)
    {
        try
        {
            String fileName = generateFileName(word, language);
            Path filePath = audioCachePath.resolve(fileName);
            Files.write(filePath, audioData);
        }
        catch (IOException e)
        {
            // Логируем, но не прерываем выполнение
            System.err.println("Failed to cache audio: " + e.getMessage());
        }
    }

    /**
     * Получение из кэша
     */
    private Optional<byte[]> getFromCache(String word, String language)
    {
        try
        {
            String fileName = generateFileName(word, language);
            Path filePath = audioCachePath.resolve(fileName);

            if (Files.exists(filePath))
            {
                return Optional.of(Files.readAllBytes(filePath));
            }
        }
        catch (IOException e)
        {
            System.err.println("Failed to read from cache: " + e.getMessage());
        }

        return Optional.empty();
    }

    private String generateFileName(String word, String language)
    {
        return language + "_" + word.toLowerCase().replaceAll("[^a-z0-9]", "_") + ".mp3";
    }
}
