package it.bonny.app.wisespender.bean;

public class IconBean {
    private Integer drawableInfo;
    private int id;

    public IconBean() {}

    public IconBean(int id, Integer drawableInfo) {
        this.id = id;
        this.drawableInfo = drawableInfo;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Integer getDrawableInfo() {
        return drawableInfo;
    }
    public void setDrawableInfo(Integer drawableInfo) {
        this.drawableInfo = drawableInfo;
    }
}
