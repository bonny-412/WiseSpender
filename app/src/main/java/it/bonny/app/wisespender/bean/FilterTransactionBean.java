package it.bonny.app.wisespender.bean;

import java.util.ArrayList;
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
        setTypeFilter(bean.getTypeFilter());
        setIdCategories(new ArrayList<>(bean.getIdCategories()));
        setIdAccounts(new ArrayList<>(bean.getIdAccounts()));
        setDateFrom(bean.getDateFrom());
        setDateA(bean.getDateA());
    }

    public boolean isChanged(FilterTransactionBean filterTransactionBean) {
        if(!this.dateFrom.equals(filterTransactionBean.getDateFrom()))
            return true;

        if(!this.dateA.equals(filterTransactionBean.getDateA()))
            return true;

        if(this.typeFilter != filterTransactionBean.getTypeFilter())
            return true;

        if(!this.idCategories.equals(filterTransactionBean.getIdCategories()))
            return true;

        return !this.idAccounts.equals(filterTransactionBean.getIdAccounts());
    }

    public String getIdCategoriesToQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Long aLong: idCategories) {
            stringBuilder.append("").append(aLong).append(", ");
        }
        String result = stringBuilder.toString().trim();
        result = result.substring(0, result.length() - 1);
        return result;
    }

    public String getIdAccountsToQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Long aLong: idAccounts) {
            stringBuilder.append("").append(aLong).append(", ");
        }
        String result = stringBuilder.toString().trim();
        result = result.substring(0, result.length() - 1);
        return result;
    }

    public String getDateFromToQuery() {
        String result;
        String[] strings = dateFrom.split("/");
        result = strings[2] + "-" + strings[1] + "-" + strings[0] + " 00:00 ";
        return result;
    }

    public String getDateAToQuery() {
        String result;
        String[] strings = dateA.split("/");
        result = strings[2] + "-" + strings[1] + "-" + strings[0] + " 23:59 ";
        return result;
    }

}
