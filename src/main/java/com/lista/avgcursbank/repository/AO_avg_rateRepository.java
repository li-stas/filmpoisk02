package com.lista.avgcursbank.repository;

import com.lista.avgcursbank.model.AO_avg_rate;
import com.lista.avgcursbank.model.AO_trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AO_avg_rateRepository extends JpaRepository<AO_avg_rate, Integer> {
    /*@Query("SELECT t.date_trade, t.name_currency, AVG(rate_buy) as rate_buy, AVG(rate_sell) as rate_sell  FROM AO_trade t GROUP BY t.date_trade, t.name_currency")
    List<AO_avg_rate> findAllByDate_trade();*/
    //findByAvgRate();
}
