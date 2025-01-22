package chapter1.data;

import java.util.List;

public class StatementData {
    private String customer;
    private List<Performance> performances;

    public StatementData(String customer, List<Performance> performances) {
        this.customer = customer;
        this.performances = performances;
    }

    public String getCustomer() {
        return customer;
    }

    public List<Performance> getPerformances() {
        return performances;
    }
}
