package com.lista.avgcursbank.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lista.avgcursbank.model.converters.LocalDateAttributeConverter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;


@Entity
@Table(name = "ao_avg_rate")
@EntityListeners(AuditingEntityListener.class)

public class AO_avg_rate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /*@Column(name = "date_trade", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime date_trade;
    */
    @Column(name = "date_trade")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate date_trade;

    @Column(name = "id_currency", nullable = false)
    private int id_currency;
    @Column(name = "name_currency", nullable = false)
    private String name_currency;

    @Column(name = "rate_buy", nullable = false)
    private BigDecimal rate_bay;

    @Column(name = "rate_sell", nullable = false)
    private BigDecimal rate_sell;

    public AO_avg_rate() {
    }

    public AO_avg_rate(int id, LocalDate date_trade, int id_currency, String name_currency, BigDecimal rate_bay, BigDecimal rate_sell) {
        this.id = id;
        this.date_trade = date_trade;
        this.id_currency = id_currency;
        this.name_currency = name_currency;
        this.rate_bay = rate_bay;
        this.rate_sell = rate_sell;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate_trade() {
        return date_trade;
    }

    public void setDate_trade(LocalDate date_trade) {
        this.date_trade = date_trade;
    }

    public int getId_currency() {
        return id_currency;
    }

    public void setId_currency(int id_currency) {
        this.id_currency = id_currency;
    }

    public String getName_currency() {
        return name_currency;
    }

    public void setName_currency(String name_currency) {
        this.name_currency = name_currency;
    }

    public BigDecimal getRateBuy() {
        return rate_bay;
    }

    public void setRateBuy(BigDecimal rate_bay) {
        this.rate_bay = rate_bay;
    }

    public BigDecimal getRateSell() {
        return rate_sell;
    }

    public void setRateSell(BigDecimal rate_sell) {
        this.rate_sell = rate_sell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AO_avg_rate that = (AO_avg_rate) o;
        return id == that.id &&
                id_currency == that.id_currency &&
                rate_bay == that.rate_bay &&
                rate_sell == that.rate_sell &&
                Objects.equals(date_trade, that.date_trade) &&
                Objects.equals(name_currency, that.name_currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date_trade, id_currency, name_currency, rate_bay, rate_sell);
    }

    @Override
    public String toString() {
        return "AO_avg_rate{" +
                "id=" + id +
                ", date_trade=" + date_trade +
                ", id_currency=" + id_currency +
                ", name_currency='" + name_currency + '\'' +
                ", rate_bay=" + rate_bay +
                ", rate_sell=" + rate_sell +
                '}';
    }
}
