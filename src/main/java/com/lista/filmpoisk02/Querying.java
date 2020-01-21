package com.lista.filmpoisk02;

/**
 * Поле id - это уникальный идентификатор приветствия, а content - текстовое представление.
 *
 * Для модели представления приветствия вам необходимо создать класс,
 * представляющего ресурс.
 * Он представляет собой простой java-объект с полями, конструкторами
 * и методами доступа к значениям id и content
 */
public class Querying {
    private final long id;
    private final String content;

    public Querying(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
