package ru.npyatak.vocabulary.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ru.npyatak.vocabulary.models.Training;
import ru.npyatak.vocabulary.repositories.TrainingRepository;

/**
 *
 * @author npyatak
 * @since 06.10.2023
 */
@Service
public class TrainingService
{
    private final TrainingRepository repository;

    @Autowired
    public TrainingService(TrainingRepository repository)
    {
        this.repository = repository;
    }

    public Training saveTraining(String trainingName)
    {
        Training training = new Training(trainingName);
        Training saved = repository.save(training);
        return saved;
    }

    public void deleteTraining(Long id)
    {
        repository.deleteById(id);
    }

    public Training getById(Long id)
    {
        return repository.findById(id).get();
    }

    public List<Training> getAllTrainings()
    {
        List<Training> result = new ArrayList<>();
        repository.findAll().forEach(result::add);
        return result;
    }
}
