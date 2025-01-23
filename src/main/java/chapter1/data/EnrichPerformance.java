package chapter1.data;

import static chapter1.PerformanceCalculator.*;

import chapter1.PerformanceCalculator;
import java.util.Map;

public record EnrichPerformance(
        Play play,
        Integer audience,
        int amount,
        int volumeCredits
) {
    public static EnrichPerformance of(Performance performance, Map<String, Play> plays) {
        PerformanceCalculator calculator = createPerformanceCalculator(performance, playFor(performance, plays));

        return new EnrichPerformance(
                playFor(performance, plays),
                performance.audience(),
                calculator.amount(),
                calculator.volumeCredits()
        );
    }

    private static Play playFor(Performance performance, Map<String, Play> plays) {
        return plays.get(performance.playID());
    }
}
