package chapter1;

import chapter1.data.Invoice;
import chapter1.data.Performance;
import chapter1.data.Play;

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
        String result = String.format("청구 내역 (고객명: %s)\n", invoice.customer());

        for (Performance perf : invoice.performances()) {
            // 청구 내역을 출력한다.
            result += String.format("  %s: %s (%d석)\n", playFor(perf).name(), usd(amountFor(perf)), perf.audience());
        }

        double totalAmount = appleSauce();

        result += String.format("총액: %s\n", usd(totalAmount));
        result += String.format("적립 포인트: %d점", totalVolumeCredits());

        return result;
    }

    private double appleSauce() {
        double totalAmount = 0;
        for (Performance perf : invoice.performances()) {
            totalAmount += amountFor(perf);
        }
        return totalAmount;
    }

    private int totalVolumeCredits() {
        int result = 0;
        for (Performance perf : invoice.performances()) {
            result += volumeCreditsFor(perf);
        }
        return result;
    }

    private String usd(double number) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(number / 100);
    }

    private int volumeCreditsFor(Performance performance) {
        int result = 0;
        result += Math.max(performance.audience() - 30, 0);
        if ("comedy".equals(playFor(performance).type())) {
            result += (int) Math.floor((double) performance.audience() / 5);
        }
        return result;
    }

    private double amountFor(Performance performance) {
        double result;
        switch (playFor(performance).type()) {
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
            default -> throw new IllegalArgumentException("알 수 없는 장르: " + playFor(performance).type());
        }
        return result;
    }

    private Play playFor(Performance performance) {
        return plays.get(performance.playID());
    }
}
