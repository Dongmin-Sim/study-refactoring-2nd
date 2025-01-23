package chapter1;

import chapter1.data.Performance;
import chapter1.data.Play;

public class PerformanceCalculator {

    private Performance performance;
    private Play play;

    public PerformanceCalculator(Performance performance, Play play) {
        this.performance = performance;
        this.play = play;
    }

    public int amountFor() {
        int result;
        switch (play.type()) {
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
            default -> throw new IllegalArgumentException("알 수 없는 장르: " + play.type());
        }
        return result;
    }

    public int volumeCreditsFor() {
        int result = 0;
        result += Math.max(performance.audience() - 30, 0);
        if ("comedy".equals(play.type())) {
            result += (int) Math.floor((double) performance.audience() / 5);
        }
        return result;
    }
}
