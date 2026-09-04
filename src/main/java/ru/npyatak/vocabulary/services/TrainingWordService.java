package ru.npyatak.vocabulary.services;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public TrainingWordService(WordTrainingLinkRepository wordTrainingLinkRepository, TrainingService trainingService)
    {
        this.wordTrainingLinkRepository = wordTrainingLinkRepository;
        this.trainingService = trainingService;
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

    public List<WordTrainingLink> addStudyStatus(Word word, Training training, LocalDate lastStudyDate, int repeatDays)
    {
        List<WordTrainingLink> linkByWordAndTraining = wordTrainingLinkRepository.findByWordAndTraining(word, training);
        linkByWordAndTraining.forEach(link ->
        {
                link.setRepeatDays(repeatDays);
                link.setLastStudyDate(lastStudyDate);
        });
        return linkByWordAndTraining;
    }

    public List<Word> getWordsForStudyForDate(LocalDate date, Long trainingId)
    {
        Training training = trainingService.getById(trainingId);
        List<WordTrainingLink> wordTrainingLinks = wordTrainingLinkRepository.findByTraining(training);
        List<Word> words = wordTrainingLinks.stream()
                .filter(wtl ->wtl.getLastStudyDate() == null ||
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

    public List<Word> getEnglishWordsByTrainingId(Long trainingId)
    {
        return getWordsByTrainingIdAndLanguage(trainingId, "english");
    }

    public List<Word> getSpainWordsByTrainingId(Long trainingId)
    {
        return getWordsByTrainingIdAndLanguage(trainingId, "spain");
    }
}
