package ru.npyatak.vocabulary.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ru.npyatak.vocabulary.models.Word;
import ru.npyatak.vocabulary.repositories.WordRepository;

/**
 *
 * @author npyatak
 * @since 04.10.2023
 */
public abstract class AbstractWordService implements WordService
{
    protected final WordRepository repository;

    protected AbstractWordService(WordRepository repository)
    {
        this.repository = repository;
    }

    @Override
    public List<Word> getAllWords()
    {
        List<Word> result = new ArrayList<>();
        repository.findAll().forEach(result::add);
        return result.stream().filter(word ->
                word.getLanguage().equals(getLanguage())).collect(Collectors.toList());
    }

    @Override
    public void saveWord(Word word)
    {
        repository.save(word);
    }

    @Override
    public Word getWordById(Long id)
    {
        return repository.findById(id).get();
    }
}
