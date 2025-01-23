package chapter1.data;

import chapter1.PerformanceCalculator;
import java.util.Map;

public record EnrichPerformance(
        Play play,
        Integer audience,
        int amount,
        int volumeCredits
) {
    public static EnrichPerformance of(Performance performance, Map<String, Play> plays) {
        PerformanceCalculator calculator = new PerformanceCalculator(performance, playFor(performance, plays));

        return new EnrichPerformance(
                playFor(performance, plays),
                performance.audience(),
                calculator.amountFor(),
                volumeCreditsFor(performance, plays)
        );
    }

    private static Play playFor(Performance performance, Map<String, Play> plays) {
        return plays.get(performance.playID());
    }

    private static int volumeCreditsFor(Performance performance, Map<String, Play> plays) {
        int result = 0;
        result += Math.max(performance.audience() - 30, 0);
        if ("comedy".equals(playFor(performance, plays).type())) {
            result += (int) Math.floor((double) performance.audience() / 5);
        }
        return result;
    }
}
