package chapter1.data;

import java.util.List;

public class StatementData {
    private String customer;
    private List<EnrichPerformance> performances;
    private double totalAmount;
    private int totalVolumeCredits;

    public StatementData(String customer, List<EnrichPerformance> performances) {
        this.customer = customer;
        this.performances = performances;
        this.totalAmount = totalAmount(performances);
        this.totalVolumeCredits = totalVolumeCredits(performances);
    }

    private double totalAmount(List<EnrichPerformance> performances) {
        return performances.stream()
                .mapToDouble(EnrichPerformance::amount)
                .sum();
    }

    private int totalVolumeCredits(List<EnrichPerformance> performances) {
        int result = 0;
        for (EnrichPerformance perf : performances) {
            result += perf.volumeCredits();
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

    public int getTotalVolumeCredits() {
        return totalVolumeCredits;
    }
}
