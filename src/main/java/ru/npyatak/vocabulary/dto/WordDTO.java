package ru.npyatak.vocabulary.dto;

import ru.npyatak.vocabulary.models.Word;

/**
 *
 *
 * @author natalapatak
 * @since 04.09.2026
 */
public class WordDTO
{
    private Long id;
    private String word;
    private String translation;
    private String language;

    public static WordDTO fromEntity(Word word)
    {
        WordDTO dto = new WordDTO();
        dto.setId(word.getId());
        dto.setWord(word.getWord());
        dto.setTranslation(word.getTranslation());
        dto.setLanguage(word.getLanguage());
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