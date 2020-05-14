package pl.lodz.p.ftims.model.finance.Model;

import java.util.Calendar;

public class Cashflow {
    private String id = "";
    private Calendar date;
    private double amount;
    private String name;

    public Cashflow(String id, Calendar date, double amount, String name) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.name = name;
    }

    public Cashflow(int id, Calendar date, double amount, String name) {
        this.id = Integer.toString(id);
        this.date = date;
        this.amount = amount;
        this.name = name;
    }

    public Cashflow(Calendar date, double amount, String name) {
        this.date = date;
        this.amount = amount;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public Calendar getDate() {
        return this.date;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Cashflow{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                ", name='" + name + '\'' +
                '}';
    }
}
