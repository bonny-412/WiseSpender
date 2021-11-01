package it.bonny.app.wisespender.bean;

public class Account {
    private long id;
    private String name;
    private int checked;
    private Double amount;

    public Account() {}


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

    public int getChecked() {
        return checked;
    }
    public void setChecked(int checked) {
        this.checked = checked;
    }

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
