package ru.npyatak.vocabulary.models;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 *
 * @author npyatak
 * @since 06.10.2023
 */
@Entity
public class WordTrainingLink
{
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Word word;

    @ManyToOne
    private  Training training;

    private LocalDate lastStudyDate = null;

    private int repeatDays = -1;

    public WordTrainingLink(Word word, Training training)
    {
        this.word = word;
        this.training = training;
    }

    public WordTrainingLink()
    {

    }

    public LocalDate getLastStudyDate()
    {
        return lastStudyDate;
    }

    public Training getTraining()
    {
        return training;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(word.getId(), training.getId());
    }

    public void setLastStudyDate(LocalDate lastStudyDate)
    {
        this.lastStudyDate = lastStudyDate;
    }

    public void setRepeatDays(int repeatDays)
    {
        this.repeatDays = repeatDays;
    }

    public int getRepeatDays()
    {
        return repeatDays;
    }

    public void setTraining(Training training)
    {
        this.training = training;
    }

    public void setWord(Word word)
    {
        this.word = word;
    }

    public Word getWord()
    {
        return this.word;
    }

    public boolean equals(Object instance)
    {
        if (instance == null)
            return false;

        if (!(instance instanceof WordTrainingLink))
            return false;

        WordTrainingLink other = (WordTrainingLink)instance;
        if (!(word.getId().equals(other.getWord().getId())))
            return false;

        if (!(training.getId().equals(other.getTraining().getId())))
            return false;

        if (!(repeatDays == other.getRepeatDays()))
            return false;

        if(!(lastStudyDate.equals(other.getLastStudyDate())))
            return false;

        return true;
    }
}
