package chapter1.data;

import java.util.List;

public class StatementData {
    private String customer;
    private List<EnrichPerformance> performances;
    private double totalAmount;

    public StatementData(String customer, List<EnrichPerformance> performances) {
        this.customer = customer;
        this.performances = performances;
        this.totalAmount = totalAmount(performances);
    }

    private double totalAmount(List<EnrichPerformance> performances) {
        double result = 0;
        for (EnrichPerformance perf : performances) {
            result += perf.amount();
        }
        return result;
    }

    public String getCustomer() {
        return customer;
    }

    public List<EnrichPerformance> getPerformances() {
        return performances;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
