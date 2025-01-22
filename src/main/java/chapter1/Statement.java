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
            result += String.format("  %s: %s (%d석)\n", perf.play().name(), usd(perf.amount()), perf.audience());
        }

        result += String.format("총액: %s\n", usd(statementData.getTotalAmount()));
        result += String.format("적립 포인트: %d점", statementData.getTotalVolumeCredits());

        return result;
    }

    private String usd(double number) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(number / 100);
    }
}
