package ru.npyatak.vocabulary.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 *
 * @author npyatak
 * @since 06.10.2023
 */
@Entity
public class Training
{
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Training()
    {

    }

    public Training(String name)
    {
        this.name = name;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
