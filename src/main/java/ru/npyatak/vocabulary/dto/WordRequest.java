package ru.npyatak.vocabulary.dto;

import jakarta.validation.constraints.NotBlank;

/**
 *
 *
 * @author natalapatak
 * @since 04.09.2026
 */
public class WordRequest
{
    @NotBlank(message = "Word is required")
    private String word;

    @NotBlank(message = "Translation is required")
    private String translation;

    private String language;

    public String getWord()
    {
        return word;
    }

    public void setWord(String word)
    {
        this.word = word;
    }

    public String getTranslation()
    {
        return translation;
    }

    public void setTranslation(String translation)
    {
        this.translation = translation;
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