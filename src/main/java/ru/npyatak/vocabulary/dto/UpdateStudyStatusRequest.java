package ru.npyatak.vocabulary.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 *
 *
 * @author natalapatak
 * @since 04.09.2026
 */
public class UpdateStudyStatusRequest
{
    @NotNull(message = "Word ID is required")
    private Long wordId;

    @NotNull(message = "Training ID is required")
    private Long trainingId;

    @Min(value = 0, message = "Repeat days must be positive")
    private int repeatDays;

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

    public int getRepeatDays()
    {
        return repeatDays;
    }

    public void setRepeatDays(int repeatDays)
    {
        this.repeatDays = repeatDays;
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
