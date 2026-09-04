package ru.npyatak.vocabulary.dto;

import jakarta.validation.constraints.NotNull;

/**
 *
 *
 * @author natalapatak
 * @since 04.09.2026
 */
public class AddWordToTrainingRequest
{
    @NotNull(message = "Word ID is required")
    private Long wordId;

    @NotNull(message = "Training ID is required")
    private Long trainingId;

    private String language;

    public Long getWordId()
    {
        return wordId;
    }

    public void setWordId(Long wordId)
    {
        this.wordId = wordId;
    }

    public Long getTrainingId()
    {
        return trainingId;
    }

    public void setTrainingId(Long trainingId)
    {
        this.trainingId = trainingId;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }
}