package chapter1;

import chapter1.data.Invoice;
import chapter1.data.Play;
import java.util.List;
import java.util.Map;

public class StatementData {

    private String customer;
    private List<EnrichPerformance> performances;
    private int totalAmount;
    private int totalVolumeCredits;

    public StatementData(String customer, List<EnrichPerformance> performances) {
        this.customer = customer;
        this.performances = performances;
        this.totalAmount = totalAmount(performances);
        this.totalVolumeCredits = totalVolumeCredits(performances);
    }

    public static StatementData createStatementData(Invoice invoice, Map<String, Play> plays) {
        List<EnrichPerformance> performances = invoice.performances().stream()
            .map(performance -> EnrichPerformance.of(performance, plays))
            .toList();

        return new StatementData(
            invoice.customer(),
            performances
        );
    }

    private int totalAmount(List<EnrichPerformance> performances) {
        return performances.stream()
            .mapToInt(EnrichPerformance::amount)
            .sum();
    }

    private int totalVolumeCredits(List<EnrichPerformance> performances) {
        return performances.stream()
            .mapToInt(EnrichPerformance::volumeCredits)
            .sum();
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
