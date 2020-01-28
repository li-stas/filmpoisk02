package com.lista.filmpoisk02.model;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * Поле id - это уникальный идентификатор приветствия, а content - текстовое представление.
 * <p>
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

    @Override
    public String toString() {
        return MessageFormat.format("Querying'{'id={0}, content=''{1}'''}'", id, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Querying querying = (Querying) o;
        return id == querying.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
