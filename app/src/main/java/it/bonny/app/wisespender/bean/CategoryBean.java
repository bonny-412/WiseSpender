package it.bonny.app.wisespender.bean;

public class CategoryBean {

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

}
