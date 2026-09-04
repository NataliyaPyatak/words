package ru.npyatak.vocabulary.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.npyatak.vocabulary.repositories.WordRepository;

/**
 *
 * @author npyatak
 * @since 04.10.2023
 */
@Service
public class EnglishWordServiceImpl extends AbstractWordService
{
    @Autowired
    protected EnglishWordServiceImpl(WordRepository repository)
    {
        super(repository);
    }

    @Override
    public String getLanguage()
    {
        return "english";
    }
}
