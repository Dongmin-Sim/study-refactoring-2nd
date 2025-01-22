package chapter1.data;

import java.util.List;

public class StatementData {
    private String customer;
    private List<EnrichPerformance> performances;

    public StatementData(String customer, List<EnrichPerformance> performances) {
        this.customer = customer;
        this.performances = performances;
    }

    public String getCustomer() {
        return customer;
    }

    public List<EnrichPerformance> getPerformances() {
        return performances;
    }
}
