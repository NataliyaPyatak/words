package ru.npyatak.vocabulary.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import ru.npyatak.vocabulary.services.audio.GoogleTTSService;

/**
 *
 * @author npyatak
 * @since 04.10.2023
 */
@Entity
public class Word
{
    @Id
    @GeneratedValue
    private Long id;
    private String word;
    private String translation;

    private String language;

    private String audioFilePath;

    private LocalDateTime audioGeneratedAt;

    @Transient
    private byte[] audioData;

    public Word()
    {

    }

    public Word(String word, String translation)
    {
        this.word = word;
        this.translation = translation;
    }

    public String getTranslation()
    {
        return translation;
    }

    public String getWord()
    {
        return word;
    }

    public void setTranslation(String translation)
    {
        this.translation = translation;
    }

    public void setWord(String word)
    {
        this.word = word;
    }

    public Long getId()
    {
        return id;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getOrGenerateAudioPath(GoogleTTSService ttsService) {
        if (!hasAudio()) {
            byte[] audio = ttsService.getWordAudio(this.word, this.language);
            String fileName = this.language + "_" +
                    this.word.toLowerCase().replaceAll("[^a-z0-9]", "_") + ".mp3";

            // Сохраняем в файловую систему
            Path filePath = Paths.get("audio-cache", fileName);
            try {
                Files.write(filePath, audio);
                this.audioFilePath = filePath.toString();
                this.audioGeneratedAt = LocalDateTime.now();
            } catch (IOException e) {
                throw new RuntimeException("Failed to save audio file", e);
            }
        }

        return this.audioFilePath;
    }

    public boolean hasAudio() {
        return audioFilePath != null && !audioFilePath.isEmpty();
    }
}
