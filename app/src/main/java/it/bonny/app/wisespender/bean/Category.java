package it.bonny.app.wisespender.bean;

public class Category {

    public static final String TABLE = "category";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_TYPE_CATEGORY = "type_category";
    public static final String KEY_LIMIT_CASH = "limit_cash";
    public static final String KEY_ID_CATEGORY_ASSOCIATED = "id_category_associated";
    public static final String KEY_ID_ICON = "id_icon";

    private Integer id;
    private String name;
    private Integer typeCategory;
    private Integer idIcon;
    private Integer limitCash;
    private Integer idCategoryAssociated;

    public Category() {}

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getTypeCategory() {
        return typeCategory;
    }
    public void setTypeCategory(Integer type) {
        this.typeCategory = type;
    }

    public Integer getIdIcon() {
        return idIcon;
    }
    public void setIdIcon(Integer idIcon) {
        this.idIcon = idIcon;
    }

    public Integer getLimitCash() {
        return limitCash;
    }
    public void setLimitCash(Integer limitCash) {
        this.limitCash = limitCash;
    }

    public void setIdCategoryAssociated(Integer idCategoryAssociated) {
        this.idCategoryAssociated = idCategoryAssociated;
    }
    public Integer getIdCategoryAssociated() {
        return idCategoryAssociated;
    }

}
