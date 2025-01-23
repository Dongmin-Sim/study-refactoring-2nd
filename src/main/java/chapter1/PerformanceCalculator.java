package chapter1;

import chapter1.calculator.ComedyPerformanceCalculator;
import chapter1.calculator.TragedyPerformanceCalculator;
import chapter1.data.Performance;
import chapter1.data.Play;

public abstract class PerformanceCalculator {

    private Performance performance;
    private Play play;

    public PerformanceCalculator(Performance performance, Play play) {
        this.performance = performance;
        this.play = play;
    }

    public static PerformanceCalculator createPerformanceCalculator(Performance performance,
        Play play) {
        return switch (play.type()) {
            case "tragedy" -> new TragedyPerformanceCalculator(performance, play);
            case "comedy" -> new ComedyPerformanceCalculator(performance, play);
            default -> throw new IllegalArgumentException("알 수 없는 장르: " + play.type());
        };
    }

    public int amount() {
        throw new UnsupportedOperationException("이 메서드는 서브 클래스에서 구현되어야 합니다.");
    }

    public int volumeCredits() {
        return Math.max(performance.audience() - 30, 0);
    }

    public Integer audience() {
        return performance.audience();
    }
}
