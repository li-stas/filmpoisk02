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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * 1) Запрос на список курсов по всем источникам, с величинами
 * средних курсов по рынку
 */
@Component
public class TradeAvgPeriod {
    private static final Logger log = LoggerFactory.getLogger(TradeAvgAll.class);
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;
    private EntityManager em;
    //private DateValidator validator;

    public TradeAvgPeriod(EntityManager em) {
        this.em = em;
    }

    public Trades eval(String cDt1, String cDt2 ) {
        String cStatus ="";
        Trades oTrades = new Trades(SUCCESS_STATUS, CODE_SUCCESS); // список курсов банка
        //DateTimeFormatter dateFormatter = DateTimeFormatter.BASIC_ISO_DATE; '20111203'
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE; //'2011-12-03'
        DateValidator validator = new DateValidatorUsingLocalDate(dateFormatter);
        boolean isValidDt1;
        boolean isValidDt2;

        /*isValidDt1 = validator.isValid("2019-02-28");
        isValidDt2 = validator.isValid("2019-02-30");
        System.out.println("Result isValidDt1=" + isValidDt1  + " isValidDt2="  + isValidDt2);*/

        isValidDt1 = validator.isValid(cDt1);
        isValidDt2 = validator.isValid(cDt2);
        System.out.println("Result isValidDt1=" + isValidDt1  + " isValidDt2="  + isValidDt2);
        if (!isValidDt1) {
            cStatus += "Ошибка ввода первой даты периода " + cDt1 + " формат ввода ГГГГ-ММ-ДД";
        }
        if (!isValidDt2) {
            cStatus += " Ошибка ввода Второй даты периода " + cDt2 + " формат ввода ГГГГ-ММ-ДД";
        }
        if (cStatus.length() > 0) {
            oTrades.setStatus(cStatus);
            oTrades.setCode(AUTH_FAILURE);
            return oTrades;
        }

        LocalDate dt1 = LocalDate.parse(cDt1);
        LocalDate dt2 = LocalDate.parse(cDt2);

        String hql = "SELECT CAST(t.date_trade AS DATE), t.name_currency, "
                + "round(AVG(rate_buy), 4) as rate_buy, ROUND(AVG(rate_sell), 4) as rate_sell "
                + "FROM AO_trade t "
                + "WHERE t.date_trade >= ?1 and t.date_trade <= ?2 "
                + "GROUP BY t.date_trade, t.name_currency";

        Query query = em.createNativeQuery(hql);
        query.setParameter(1, dt1);
        query.setParameter(2, dt2);

        List<Object[]> aResultList = query.getResultList();


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

