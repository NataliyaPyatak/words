package ru.npyatak.vocabulary.services.audio;

/**
 *
 *
 * @author natalapatak
 * @since 04.09.2026
 */
public interface AudioService
{
    byte[] getWordAudio(String word, String language);
}
