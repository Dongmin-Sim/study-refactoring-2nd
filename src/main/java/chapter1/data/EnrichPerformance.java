package chapter1.data;

import java.util.Map;

public record EnrichPerformance(
        Play play,
        Integer audience
) {
    public static EnrichPerformance of(Performance performance, Map<String, Play> plays) {
        Play play = playFor(performance, plays);
        return new EnrichPerformance(play, performance.audience());
    }

    private static Play playFor(Performance performance, Map<String, Play> plays) {
        return plays.get(performance.playID());
    }
}
