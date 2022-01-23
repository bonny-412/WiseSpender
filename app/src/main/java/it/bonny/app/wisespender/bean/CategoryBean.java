package it.bonny.app.wisespender.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class CategoryBean implements Parcelable {

    public static final String TABLE = "category";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_TYPE_CATEGORY = "type_category";
    public static final String KEY_ID_ICON = "id_icon";

    private long id;
    private String name;
    private int typeCategory;
    private int idIcon;

    public CategoryBean() {}

    public CategoryBean(int typeCategory) {
        this.typeCategory = typeCategory;
    }

    protected CategoryBean(Parcel in) {
        id = in.readLong();
        name = in.readString();
        typeCategory = in.readInt();
        idIcon = in.readInt();
    }

    public static final Creator<CategoryBean> CREATOR = new Creator<CategoryBean>() {
        @Override
        public CategoryBean createFromParcel(Parcel in) {
            return new CategoryBean(in);
        }

        @Override
        public CategoryBean[] newArray(int size) {
            return new CategoryBean[size];
        }
    };

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getTypeCategory() {
        return typeCategory;
    }
    public void setTypeCategory(int type) {
        this.typeCategory = type;
    }

    public int getIdIcon() {
        return idIcon;
    }
    public void setIdIcon(int idIcon) {
        this.idIcon = idIcon;
    }

    public static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + CategoryBean.TABLE
            + "("
            + CategoryBean.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + CategoryBean.KEY_NAME + " TEXT,"
            + CategoryBean.KEY_TYPE_CATEGORY + " INTEGER,"
            + CategoryBean.KEY_ID_ICON + " INTEGER"
            + ")";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeInt(typeCategory);
        parcel.writeInt(idIcon);
    }
}
