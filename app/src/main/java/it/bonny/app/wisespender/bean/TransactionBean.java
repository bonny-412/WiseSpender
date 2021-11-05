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

    private long id;
    private int amount;
    private String dateInsert;
    private String note;
    private int typeTransaction;
    private int idCategory;
    private int idAccount;

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

    public int getIdAccount() {
        return idAccount;
    }
    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    public int getIdCategory() {
        return idCategory;
    }
    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

}
