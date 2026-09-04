package ru.npyatak.vocabulary.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.npyatak.vocabulary.models.Training;
import ru.npyatak.vocabulary.models.Word;
import ru.npyatak.vocabulary.models.WordTrainingLink;
import ru.npyatak.vocabulary.services.TrainingService;
import ru.npyatak.vocabulary.services.TrainingWordService;
import ru.npyatak.vocabulary.services.WordService;

/**
 *
 * @author npyatak
 * @since 04.10.2023
 */
@Controller
public class WordController
{
    @Value("${language}")
    private String language;
    private final Map<String, WordService> wordServicesMap = new HashMap<>();

    private final TrainingService trainingService;

    private final TrainingWordService trainingWordService;

    @Autowired
    public WordController(List<WordService> wordServices, TrainingService trainingService,
            TrainingWordService trainingWordService)
    {
        this.trainingService = trainingService;
        this.trainingWordService = trainingWordService;
        wordServices.forEach(wordService -> this.wordServicesMap.put(wordService.getLanguage(), wordService));
    }

    @GetMapping("/allWords")
    @ResponseBody
    public List<Word> getAllWords()
    {
        WordService wordService = wordServicesMap.get(language);
        return wordService.getAllWords();
    }

    @GetMapping("/allTrainings")
    @ResponseBody
    public List<Training> getAllTrainings()
    {
        return trainingService.getAllTrainings();
    }

    @GetMapping("/addWord/{word}/{translation}/{language}")
    @ResponseBody
    public List<Word> addWord(@PathVariable String word, @PathVariable String translation,
            @PathVariable String language)
    {
        Word newWord = new Word(word, translation);
        newWord.setLanguage(language);
        WordService wordService = wordServicesMap.get(language);

        wordService.saveWord(newWord);
        return getAllWords();
    }

    @GetMapping("/addTraining/{training}")
    @ResponseBody
    public List<Training> addTraining(@PathVariable String training)
    {
        trainingService.saveTraining(training);
        return getAllTrainings();
    }

    @GetMapping("/addWordToTraining/{wordId}/{trainingId}")
    @ResponseBody
    public WordTrainingLink addWordToTraining(@PathVariable Long wordId, @PathVariable Long trainingId)
    {
        WordService wordService = wordServicesMap.get(language);
        Word word = wordService.getWordById(wordId);
        Training training = trainingService.getById(trainingId);
        return trainingWordService.addWordToTraining(word, training);
    }

    @GetMapping("/addWordToTrainings/{wordId}")
    @ResponseBody
    public List<WordTrainingLink> addWordToAllTrainings(@PathVariable Long wordId)
    {
        WordService wordService = wordServicesMap.get(language);
        Word word = wordService.getWordById(wordId);
        List<Training> training = trainingService.getAllTrainings();
        return trainingWordService.addWordToAllTrainings(word, training);
    }

    @GetMapping("/getWordsByTrainingId/{trainingId}")
    @ResponseBody
    public List<Word> getWordsByTrainingId(@PathVariable Long trainingId)
    {
        List<Word> wordsByTrainingId = trainingWordService.getWordsByTrainingIdAndLanguage(trainingId, language);
        return wordsByTrainingId;
    }

    @GetMapping("/getWordsForStudyByTrainingId/{trainingId}")
    @ResponseBody
    public List<Word> getWordsForStudyByTrainingId(@PathVariable Long trainingId)
    {
        LocalDate now = LocalDate.now();
        List<Word> wordsByTrainingId = trainingWordService.getWordsForStudyForDate(now, 4L);
        return wordsByTrainingId;
    }

    @GetMapping("/setWordByTrainingStatus/{wordId}/{trainingId}/{repeatDays}")
    @ResponseBody
    public List<WordTrainingLink> setWordByTrainingStatus(@PathVariable Long trainingId, @PathVariable Long wordId, @PathVariable int repeatDays)
    {
        WordService wordService = wordServicesMap.get(language);
        Word word = wordService.getWordById(wordId);
        Training training = trainingService.getById(trainingId);
        LocalDate now = LocalDate.now();
        return trainingWordService.addStudyStatus(word,training, now, repeatDays);
    }

}
