package it.bonny.app.wisespender.bean;

import java.util.List;

public class FilterTransactionBean {
    private int typeBottomSheet;
    private int typeFilter;
    private List<Long> idCategories;
    private List<Long> idAccounts;
    private String dateFrom;
    private String dateA;

    public FilterTransactionBean() {}

    public int getTypeFilter() {
        return typeFilter;
    }
    public void setTypeFilter(int typeFilter) {
        this.typeFilter = typeFilter;
    }

    public int getTypeBottomSheet() {
        return typeBottomSheet;
    }
    public void setTypeBottomSheet(int typeBottomSheet) {
        this.typeBottomSheet = typeBottomSheet;
    }

    public List<Long> getIdCategories() {
        return idCategories;
    }
    public void setIdCategories(List<Long> idCategories) {
        this.idCategories = idCategories;
    }

    public List<Long> getIdAccounts() {
        return idAccounts;
    }
    public void setIdAccounts(List<Long> idAccounts) {
        this.idAccounts = idAccounts;
    }

    public String getDateFrom() {
        return dateFrom;
    }
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateA() {
        return dateA;
    }
    public void setDateA(String dateA) {
        this.dateA = dateA;
    }

    public void copyElement(FilterTransactionBean bean) {
        this.setTypeFilter(bean.getTypeFilter());
        this.setIdCategories(bean.getIdCategories());
        this.setIdAccounts(bean.getIdAccounts());
        this.setDateFrom(bean.getDateFrom());
        this.setDateA(bean.getDateA());
    }

}
