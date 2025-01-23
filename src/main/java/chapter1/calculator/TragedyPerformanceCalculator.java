package chapter1.calculator;

import chapter1.PerformanceCalculator;
import chapter1.data.Performance;
import chapter1.data.Play;

public class TragedyPerformanceCalculator extends PerformanceCalculator {

    public TragedyPerformanceCalculator(Performance performance, Play play) {
        super(performance, play);
    }

    @Override
    public int amount() {
        int result = 40_000;
        if (audience() > 30) {
            result += 1_000 * (audience() - 30);
        }
        return result;
    }
}
