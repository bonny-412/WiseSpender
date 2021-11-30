package it.bonny.app.wisespender.bean;

public class FilterTransactionBean {
    private int filterDate;
    private int filterTypeTransaction;
    private String dateFrom;
    private String dateA;

    public FilterTransactionBean() {}

    public int getFilterDate() {
        return filterDate;
    }
    public void setFilterDate(int filterDate) {
        this.filterDate = filterDate;
    }

    public int getFilterTypeTransaction() {
        return filterTypeTransaction;
    }
    public void setFilterTypeTransaction(int filterTypeTransaction) {
        this.filterTypeTransaction = filterTypeTransaction;
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

}
