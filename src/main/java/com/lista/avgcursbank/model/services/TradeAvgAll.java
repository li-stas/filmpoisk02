package com.lista.avgcursbank.model.services;

import com.lista.avgcursbank.model.AO_trade;
import com.lista.avgcursbank.model.Trades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 1) Запрос на список курсов по всем источникам, с величинами
 * средних курсов по рынку
 */
@Component
public class TradeAvgAll {
    private static final Logger log = LoggerFactory.getLogger(TradeAvgAll.class);
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;
    private EntityManager em;
    public TradeAvgAll(EntityManager em) {
        this.em = em;
    }

    public Trades eval() {

        String hql = "SELECT CAST(t.date_trade AS DATE), t.name_currency, "
                + "round(AVG(rate_buy), 4) as rate_buy, ROUND(AVG(rate_sell), 4) as rate_sell "
                + "FROM AO_trade t GROUP BY t.date_trade, t.name_currency";

        Query query = em.createNativeQuery(hql);

        List<Object[]> aResultList = query.getResultList();

        Trades oTrades = new Trades(SUCCESS_STATUS, CODE_SUCCESS); // список курсов банка
        for (Object[] a : aResultList) {
            //System.out.println("Result " + a[0]  + " "  + a[1]);
            AO_trade oTrade = new AO_trade();

            Date dateToConvert = (Date) a[0];
            oTrade.setId_bank(999);
            oTrade.setName_bank("AvgRate");

            LocalDate dt = new java.sql.Date(dateToConvert.getTime()).toLocalDate();
            oTrade.setDate_trade(dt);

            oTrade.setName_currency((String) a[1]);
            oTrade.setRateBuy((BigDecimal) a[2]);
            oTrade.setRateSell((BigDecimal) a[3]);
            oTrades.addTrade(oTrade);
        }

        return oTrades;

    }

}

