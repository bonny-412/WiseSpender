package it.bonny.app.wisespender.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class TransactionBean implements Parcelable {

    public static final String TABLE = "transaction_table";
    public static final String KEY_ID = "id";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DATE_INSERT = "date_insert";
    public static final String KEY_NOTE = "note";
    public static final String KEY_TYPE_TRANSACTION = "type_transaction";
    public static final String KEY_ID_CATEGORY = "id_category";
    public static final String KEY_ID_ACCOUNT = "id_account";
    public static final String KEY_ID_TRANSACTION_TRANSFER = "id_transaction_transfer";
    public static final String KEY_TITLE = "title";

    private long id;
    private String title;
    private int amount;
    private String dateInsert;
    private String note;
    private int typeTransaction;
    private long idCategory;
    private long idAccount;
    private long idTransactionTransfer;//Id transaction transfer

    public long getIdTransactionTransfer() {
        return idTransactionTransfer;
    }

    public void setIdTransactionTransfer(long idTransactionTransfer) {
        this.idTransactionTransfer = idTransactionTransfer;
    }

    public TransactionBean() {}

    protected TransactionBean(Parcel in) {
        id = in.readLong();
        title = in.readString();
        amount = in.readInt();
        dateInsert = in.readString();
        note = in.readString();
        typeTransaction = in.readInt();
        idCategory = in.readLong();
        idAccount = in.readLong();
        idTransactionTransfer = in.readLong();
    }

    public static final Creator<TransactionBean> CREATOR = new Creator<TransactionBean>() {
        @Override
        public TransactionBean createFromParcel(Parcel in) {
            return new TransactionBean(in);
        }

        @Override
        public TransactionBean[] newArray(int size) {
            return new TransactionBean[size];
        }
    };

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

    public static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TransactionBean.TABLE
            + "("
            + TransactionBean.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + TransactionBean.KEY_AMOUNT + " INTEGER,"
            + TransactionBean.KEY_DATE_INSERT + " DATETIME,"
            + TransactionBean.KEY_NOTE + " TEXT,"
            + TransactionBean.KEY_TYPE_TRANSACTION + " INTEGER,"
            + TransactionBean.KEY_ID_ACCOUNT + " INTEGER,"
            + TransactionBean.KEY_ID_CATEGORY + " INTEGER,"
            + TransactionBean.KEY_ID_TRANSACTION_TRANSFER + " INTEGER,"
            + TransactionBean.KEY_TITLE + " TEXT"
            + ")";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeInt(amount);
        parcel.writeString(dateInsert);
        parcel.writeString(note);
        parcel.writeInt(typeTransaction);
        parcel.writeLong(idCategory);
        parcel.writeLong(idAccount);
        parcel.writeLong(idTransactionTransfer);
    }
}
