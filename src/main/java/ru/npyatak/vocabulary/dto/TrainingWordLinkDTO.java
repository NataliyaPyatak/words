package ru.npyatak.vocabulary.dto;

import java.time.LocalDate;

import ru.npyatak.vocabulary.models.WordTrainingLink;

/**
 *
 *
 * @author natalapatak
 * @since 04.09.2026
 */
public class TrainingWordLinkDTO
{
    private Long id;
    private Long wordId;
    private Long trainingId;
    private LocalDate lastStudyDate;
    private int repeatDays;

    public TrainingWordLinkDTO()
    {
    }

    public static TrainingWordLinkDTO fromEntity(WordTrainingLink link)
    {
        TrainingWordLinkDTO dto = new TrainingWordLinkDTO();
        dto.setId(link.getId());
        dto.setWordId(link.getWord().getId());
        dto.setTrainingId(link.getTraining().getId());
        dto.setLastStudyDate(link.getLastStudyDate());
        dto.setRepeatDays(link.getRepeatDays());
        return dto;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

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

    public LocalDate getLastStudyDate()
    {
        return lastStudyDate;
    }

    public void setLastStudyDate(LocalDate lastStudyDate)
    {
        this.lastStudyDate = lastStudyDate;
    }

    public int getRepeatDays()
    {
        return repeatDays;
    }

    public void setRepeatDays(int repeatDays)
    {
        this.repeatDays = repeatDays;
    }
}
