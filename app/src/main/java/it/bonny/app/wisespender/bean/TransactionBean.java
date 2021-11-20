package it.bonny.app.wisespender.bean;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionBean {

    public static final String TABLE = "transaction_table";
    public static final String KEY_ID = "id";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DATE_INSERT = "date_insert";
    public static final String KEY_NOTE = "note";
    public static final String KEY_TYPE_TRANSACTION = "type_transaction";
    public static final String KEY_ID_CATEGORY = "id_category";
    public static final String KEY_ID_ACCOUNT = "id_account";
    public static final String KEY_TITLE = "title";

    private long id;
    private String title;
    private int amount;
    private String dateInsert;
    private String note;
    private int typeTransaction;
    private long idCategory;
    private long idAccount;

    public TransactionBean() {}

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDateInsert() {
        return dateInsert;
    }
    public void setDateInsert(String dateInsert) {
        this.dateInsert = dateInsert;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public int getTypeTransaction() {
        return typeTransaction;
    }
    public void setTypeTransaction(int typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public long getIdAccount() {
        return idAccount;
    }
    public void setIdAccount(long idAccount) {
        this.idAccount = idAccount;
    }

    public long getIdCategory() {
        return idCategory;
    }
    public void setIdCategory(long idCategory) {
        this.idCategory = idCategory;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

}
