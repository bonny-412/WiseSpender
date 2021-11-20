package it.bonny.app.wisespender.bean;

public class IconBean {
    private Integer drawableInfo;
    private String name;
    private int id;

    public IconBean() {}
    public IconBean(int id, Integer drawableInfo, String name) {
        this.id = id;
        this.drawableInfo = drawableInfo;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getDrawableInfo() {
        return drawableInfo;
    }
    public void setDrawableInfo(Integer drawableInfo) {
        this.drawableInfo = drawableInfo;
    }

}
