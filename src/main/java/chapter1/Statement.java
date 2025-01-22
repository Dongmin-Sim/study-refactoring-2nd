package chapter1;

import chapter1.data.*;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Statement {
    private Invoice invoice;
    private Map<String, Play> plays;

    public Statement(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    public String statement() {
        StatementData statementData = new StatementData(
                invoice.customer(),
                invoice.performances().stream()
                        .map(performance -> EnrichPerformance.of(performance, plays))
                        .toList()
        );

        return renderPlainText(statementData);
    }

    private String renderPlainText(StatementData statementData) {
        String result = String.format("청구 내역 (고객명: %s)\n", statementData.getCustomer());

        for (EnrichPerformance perf : statementData.getPerformances()) {
            // 청구 내역을 출력한다.
            result += String.format("  %s: %s (%d석)\n", perf.play().name(), usd(amountFor(perf)), perf.audience());
        }

        result += String.format("총액: %s\n", usd(totalAmount(statementData)));
        result += String.format("적립 포인트: %d점", totalVolumeCredits(statementData));

        return result;
    }

    private double totalAmount(StatementData statementData) {
        double result = 0;
        for (EnrichPerformance perf : statementData.getPerformances()) {
            result += amountFor(perf);
        }
        return result;
    }

    private int totalVolumeCredits(StatementData statementData) {
        int result = 0;
        for (EnrichPerformance perf : statementData.getPerformances()) {
            result += volumeCreditsFor(perf);
        }
        return result;
    }

    private String usd(double number) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(number / 100);
    }

    private int volumeCreditsFor(EnrichPerformance performance) {
        int result = 0;
        result += Math.max(performance.audience() - 30, 0);
        if ("comedy".equals(performance.play().type())) {
            result += (int) Math.floor((double) performance.audience() / 5);
        }
        return result;
    }

    private double amountFor(EnrichPerformance performance) {
        double result;
        switch (performance.play().type()) {
            case "tragedy" -> {
                result = 40_000;
                if (performance.audience() > 30) {
                    result += 1_000 * (performance.audience() - 30);
                }
            }
            case "comedy" -> {
                result = 30_000;
                if (performance.audience() > 20) {
                    result += 10_000 + 500 * (performance.audience() - 20);
                }
                result += 300 * performance.audience();
            }
            default -> throw new IllegalArgumentException("알 수 없는 장르: " + performance.play().type());
        }
        return result;
    }
}
