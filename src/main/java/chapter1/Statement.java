package chapter1;

import chapter1.data.Invoice;
import chapter1.data.Performance;
import chapter1.data.Play;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Statement {
    public String statement(Invoice invoice, Map<String, Play> plays) {
        double totalAmount = 0;
        int volumeCredits = 0;
        String result = String.format("청구 내역 (고객명: %s)\n", invoice.customer());
        final NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

        for (Performance perf : invoice.performances()) {
            Play play = plays.get(perf.playID());
            double thisAmount;

            switch (play.type()) {
                case "tragedy" -> {
                    thisAmount = 40_000;
                    if (perf.audience() > 30) {
                        thisAmount += 1_000 * (perf.audience() - 30);
                    }
                }
                case "comedy" -> {
                    thisAmount = 30_000;
                    if (perf.audience() > 20) {
                        thisAmount += 10_000 + 500 * (perf.audience() - 20);
                    }
                    thisAmount += 300 * perf.audience();
                }
                default -> throw new IllegalArgumentException("알 수 없는 장르: " + play.type());
            }

            // 포인트를 적립한다.
            volumeCredits += Math.max(perf.audience() - 30, 0);
            // 희극 관객 5명마다 추가 포인트를 제공한다.
            if ("comedy".equals(play.type())) {
                volumeCredits += (int) Math.floor((double) perf.audience() / 5);
            }

            // 청구 내역을 출력한다.
            result += String.format("  %s: %s (%d석)\n", play.name(), format.format(thisAmount / 100.0), perf.audience());
            totalAmount += thisAmount;
        }

        result += String.format("총액: %s\n", format.format(totalAmount / 100));
        result += String.format("적립 포인트: %d점", volumeCredits);

        return result;
    }
}
