package ru.npyatak.vocabulary.services;

import java.util.List;

import ru.npyatak.vocabulary.models.Word;

/**
 *
 * @author npyatak
 * @since 04.10.2023
 */
public interface WordService
{
    List<Word> getAllWords();

    Word getWordById(Long id);

    Word saveWord(Word word);

    String getLanguage();
    void deleteWord(Long id);
}
