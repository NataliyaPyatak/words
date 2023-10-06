package ru.npyatak.vocabulary.repositories;

import org.springframework.data.repository.CrudRepository;

import ru.npyatak.vocabulary.models.Training;

/**
 *
 * @author npyatak
 * @since 06.10.2023
 */
public interface TrainingRepository extends CrudRepository<Training, Long>
{
}
