package ru.npyatak.vocabulary.repositories;

import org.springframework.data.repository.CrudRepository;

import ru.npyatak.vocabulary.models.Word;

/**
 *
 * @author npyatak
 * @since 06.10.2023
 */
public interface WordRepository extends CrudRepository<Word, Long>
{
}
