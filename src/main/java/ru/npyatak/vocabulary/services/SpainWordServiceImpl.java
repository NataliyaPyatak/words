package ru.npyatak.vocabulary.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.npyatak.vocabulary.models.Word;
import ru.npyatak.vocabulary.repositories.WordRepository;

/**
 *
 * @author npyatak
 * @since 04.10.2023
 */
@Service
public class SpainWordServiceImpl extends AbstractWordService
{
    @Autowired
    protected SpainWordServiceImpl(WordRepository repository)
    {
        super(repository);
    }

    @Override
    public String getLanguage()
    {
        return "spain";
    }
}
