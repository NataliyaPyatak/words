package ru.npyatak.vocabulary.dto;

import jakarta.validation.constraints.NotBlank;

/**
 *
 *
 * @author natalapatak
 * @since 04.09.2026
 */
public class TrainingRequest
{
    @NotBlank(message = "Training name is required")
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
