package com.yadaniil.fap.db;

import com.orm.SugarRecord;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by daniil on 25.08.16.
 */

public class Record extends SugarRecord {

    private BigDecimal amount;
    private boolean isEarned;
    private Date date;
    private String description;

    public Record() {}

    public Record(BigDecimal amount, boolean isEarned, Date date, String description) {
        this.amount = amount;
        this.isEarned = isEarned;
        this.date = date;
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isEarned() {
        return isEarned;
    }

    public void setEarned(boolean earned) {
        isEarned = earned;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
