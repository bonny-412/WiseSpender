package it.bonny.app.wisespender.bean;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private long id;
    private Category category;
    private BigDecimal amount;
    private Date date;
    private String note;

    public Transaction() {}

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

}
