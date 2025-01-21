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
        double totalAmount = 0;
        int volumeCredits = 0;
        String result = String.format("청구 내역 (고객명: %s)\n", invoice.customer());
        final NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

        for (Performance perf : invoice.performances()) {
            // 포인트를 적립한다.
            volumeCredits += Math.max(perf.audience() - 30, 0);
            // 희극 관객 5명마다 추가 포인트를 제공한다.
            if ("comedy".equals(playFor(perf).type())) {
                volumeCredits += (int) Math.floor((double) perf.audience() / 5);
            }

            // 청구 내역을 출력한다.
            result += String.format("  %s: %s (%d석)\n", playFor(perf).name(), format.format(amountFor(perf) / 100.0), perf.audience());
            totalAmount += amountFor(perf);
        }

        result += String.format("총액: %s\n", format.format(totalAmount / 100));
        result += String.format("적립 포인트: %d점", volumeCredits);

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
