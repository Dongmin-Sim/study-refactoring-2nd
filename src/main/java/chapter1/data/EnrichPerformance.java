package chapter1.data;

import java.util.Map;

public record EnrichPerformance(
        Play play,
        Integer audience,
        double amount
) {
    public static EnrichPerformance of(Performance performance, Map<String, Play> plays) {
        Play play = playFor(performance, plays);
        double amount = amountFor(performance, plays);
        return new EnrichPerformance(play, performance.audience(), amount);
    }

    private static Play playFor(Performance performance, Map<String, Play> plays) {
        return plays.get(performance.playID());
    }

    private static double amountFor(Performance performance, Map<String, Play> plays) {
        double result;
        switch (playFor(performance, plays).type()) {
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
            default -> throw new IllegalArgumentException("알 수 없는 장르: " + playFor(performance, plays).type());
        }
        return result;
    }
}
