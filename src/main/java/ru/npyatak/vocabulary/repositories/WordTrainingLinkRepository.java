package ru.npyatak.vocabulary.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ru.npyatak.vocabulary.models.Training;
import ru.npyatak.vocabulary.models.Word;
import ru.npyatak.vocabulary.models.WordTrainingLink;

/**
 *
 * @author npyatak
 * @since 06.10.2023
 */
public interface WordTrainingLinkRepository extends CrudRepository<WordTrainingLink, Long>
{
    List<WordTrainingLink> findByTraining(Training training);

    List<WordTrainingLink> findByWordAndTraining(Word word, Training training);

    /*@Query("from WordTrainingLink a where a.lastStudyDate + a.repeatDays < :currentDate")
    public Iterable<WordTrainingLink> findByCategory(@Param("currentDate") Date currentDate);*/
    List<WordTrainingLink> findByLastStudyDateIsNull();


}
