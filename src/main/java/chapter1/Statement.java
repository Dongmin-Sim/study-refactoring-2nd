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
        return renderPlainText(StatementData.createStatementData(invoice, plays));
    }

    public String htmlStatement() {
        return renderHtml(StatementData.createStatementData(invoice, plays));
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

    private String renderHtml(StatementData statementData) {
        String result = String.format("<h1>청구 내역 (고객명: %s)</h1>\n", statementData.getCustomer());
        result += "<table>\n";
        result += "<tr><th>연극</th><th>좌석 수</th><th>금액</th></tr>";
        for (EnrichPerformance perf : statementData.getPerformances()) {
            // 청구 내역을 출력한다.
            result += String.format("  <tr><td>%s</td><td>(%d석)</td>", perf.play().name(), perf.audience());
            result += String.format("<td>%s</td></tr>\n", usd(perf.amount()));
        }
        result += "</table>\n";
        result += String.format("<p>총액: <em>%s</em></p>\n", usd(statementData.getTotalAmount()));
        result += String.format("<p>적립 포인트: <em>%d</em>점</p>", statementData.getTotalVolumeCredits());
        return result;
    }

    private String usd(double number) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(number / 100);
    }
}
