package it.bonny.app.wisespender.bean;

public class IconBean {
    private Integer drawableInfo;
    private String name;

    public IconBean() {}
    public IconBean(Integer drawableInfo, String name) {
        this.drawableInfo = drawableInfo;
        this.name = name;
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
