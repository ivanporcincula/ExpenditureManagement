package MainApp;

import java.sql.Timestamp;

public class Money {

    private Timestamp datetime;
    private String category;
    private double amount;



    public Money(Timestamp datetime, String category, double amount){
        this.datetime = datetime;
        this.category = category;
        this.amount = amount;
    }

    public Money(Timestamp datetime, double amount){
        this.datetime = datetime;
        this.amount = amount;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


}
