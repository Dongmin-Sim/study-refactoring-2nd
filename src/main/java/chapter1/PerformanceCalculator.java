package chapter1;

import chapter1.calculator.ComedyCalculator;
import chapter1.calculator.TragedyCalculator;
import chapter1.data.Performance;
import chapter1.data.Play;

public class PerformanceCalculator {
    protected Performance performance;
    protected Play play;

    public PerformanceCalculator(Performance performance, Play play) {
        this.performance = performance;
        this.play = play;
    }

    public static PerformanceCalculator createPerformanceCalculator(Performance performance, Play play) {
        switch (play.type()) {
            case "tragedy" -> {
                return new TragedyCalculator(performance, play);
            }
            case "comedy" -> {
                return new ComedyCalculator(performance, play);
            }
            default -> throw new IllegalArgumentException("알 수 없는 장르: " + play.type());
        }
    }

    public int amount() {
        int result;
        switch (play.type()) {
            case "tragedy" -> {
                throw new IllegalArgumentException("오류 발생");
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

    public int volumeCredits() {
        int result = 0;
        result += Math.max(performance.audience() - 30, 0);
        if ("comedy".equals(play.type())) {
            result += (int) Math.floor((double) performance.audience() / 5);
        }
        return result;
    }
}
