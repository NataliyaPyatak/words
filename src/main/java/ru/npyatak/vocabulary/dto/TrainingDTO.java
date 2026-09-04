package ru.npyatak.vocabulary.dto;

import ru.npyatak.vocabulary.models.Training;

/**
 *
 *
 * @author natalapatak
 * @since 04.09.2026
 */
public class TrainingDTO
{
    private Long id;
    private String name;

    public TrainingDTO()
    {
    }

    public TrainingDTO(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public static TrainingDTO fromEntity(Training training)
    {
        TrainingDTO dto = new TrainingDTO();
        dto.setId(training.getId());
        dto.setName(training.getName());
        return dto;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
