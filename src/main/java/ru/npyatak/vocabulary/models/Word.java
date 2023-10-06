package ru.npyatak.vocabulary.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
}
