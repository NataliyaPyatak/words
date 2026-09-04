package ru.npyatak.vocabulary.services;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ru.npyatak.vocabulary.exceptions.LanguageNotSupportedException;
import ru.npyatak.vocabulary.models.Training;
import ru.npyatak.vocabulary.models.Word;
import ru.npyatak.vocabulary.models.WordTrainingLink;
import ru.npyatak.vocabulary.repositories.WordTrainingLinkRepository;

/**
 *
 * @author npyatak
 * @since 06.10.2023
 */
@Service
public class TrainingWordService
{
    private final WordTrainingLinkRepository wordTrainingLinkRepository;
    private final TrainingService trainingService;
    private final Map<String, WordService> wordServicesMap;

    @Autowired
    public TrainingWordService(WordTrainingLinkRepository wordTrainingLinkRepository,
            TrainingService trainingService,
            List<WordService> wordServices,
            @Value("${language}") String defaultLanguage)
    {
        this.wordTrainingLinkRepository = wordTrainingLinkRepository;
        this.trainingService = trainingService;
        this.wordServicesMap = new HashMap<>();
        wordServices.forEach(service ->
                wordServicesMap.put(service.getLanguage(), service));
    }

    public Training getTrainingById(Long trainingId)
    {
        return trainingService.getById(trainingId);
    }

    public List<Training> getAllTrainings()
    {
        return trainingService.getAllTrainings();
    }

    public WordTrainingLink addWordToTraining(Word word, Training training)
    {
        WordTrainingLink wordTrainingLink = new WordTrainingLink(word, training);
        wordTrainingLinkRepository.save(wordTrainingLink);
        return wordTrainingLink;
    }

    public List<WordTrainingLink> addWordToAllTrainings(Word word, List<Training> trainings)
    {
        List<WordTrainingLink> wordTrainingLinks = trainings.stream()
                .map(training -> new WordTrainingLink(word, training))
                .collect(Collectors.toList());

        wordTrainingLinkRepository.saveAll(wordTrainingLinks);
        return wordTrainingLinks;
    }

    public void removeWordFromTraining(Long wordId, Long trainingId, String language)
            throws LanguageNotSupportedException
    {
        WordTrainingLink link = wordTrainingLinkRepository
                .findByWordAndTraining(getWordService(language).getWordById(wordId), trainingService.getById(trainingId));

        wordTrainingLinkRepository.delete(link);
    }

    public WordTrainingLink addStudyStatus(Word word, Training training, LocalDate lastStudyDate, int repeatDays)
    {
        WordTrainingLink linkByWordAndTraining = wordTrainingLinkRepository.findByWordAndTraining(word, training);
        linkByWordAndTraining.setRepeatDays(repeatDays);
        linkByWordAndTraining.setLastStudyDate(lastStudyDate);
        return linkByWordAndTraining;
    }

    public List<Word> getWordsForStudyForDate(LocalDate date, Long trainingId)
    {
        Training training = trainingService.getById(trainingId);
        List<WordTrainingLink> wordTrainingLinks = wordTrainingLinkRepository.findByTraining(training);
        List<Word> words = wordTrainingLinks.stream()
                .filter(wtl -> wtl.getLastStudyDate() == null ||
                        wtl.getLastStudyDate().plusDays(wtl.getRepeatDays()).isBefore(date))
                .map(wtl -> wtl.getWord()).collect(Collectors.toList());
        return words;
    }

    public List<Word> getWordsByTrainingName(String trainingName)
    {
        Training training = new Training(trainingName);
        List<WordTrainingLink> wordTrainingLinks = wordTrainingLinkRepository.findByTraining(training);
        return wordTrainingLinks.stream().map(wtl -> wtl.getWord()).collect(Collectors.toList());
    }

    public List<Word> getWordsByTrainingId(Long trainingId)
    {
        Training training = trainingService.getById(trainingId);
        List<WordTrainingLink> wordTrainingLinks = wordTrainingLinkRepository.findByTraining(training);

        return wordTrainingLinks.stream().map(wtl -> wtl.getWord()).collect(Collectors.toList());
    }

    public List<Word> getWordsByTrainingIdAndLanguage(Long trainingId, String language)
    {
        Training training = trainingService.getById(trainingId);
        List<WordTrainingLink> wordTrainingLinks = wordTrainingLinkRepository.findByTraining(training);

        return wordTrainingLinks.stream().map(wtl -> wtl.getWord())
                .filter(word -> word.getLanguage().equals(language))
                .collect(Collectors.toList());
    }

    private WordService getWordService(String language) throws LanguageNotSupportedException
    {
        WordService service = wordServicesMap.get(language);
        if (service == null) {
            throw new LanguageNotSupportedException("Language not supported: " + language);
        }
        return service;
    }

    public List<Word> getEnglishWordsByTrainingId(Long trainingId)
    {
        return getWordsByTrainingIdAndLanguage(trainingId, "english");
    }

    public List<Word> getSpainWordsByTrainingId(Long trainingId)
    {
        return getWordsByTrainingIdAndLanguage(trainingId, "spain");
    }
}
