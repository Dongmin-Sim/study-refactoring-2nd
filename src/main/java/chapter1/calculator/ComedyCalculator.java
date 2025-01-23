package chapter1.calculator;

import chapter1.PerformanceCalculator;
import chapter1.data.Performance;
import chapter1.data.Play;

public class ComedyCalculator extends PerformanceCalculator {

    public ComedyCalculator(Performance performance, Play play) {
        super(performance, play);
    }

    @Override
    public int amount() {
        int result = 30_000;
        if (audience() > 20) {
            result += 10_000 + 500 * (audience() - 20);
        }
        result += 300 * audience();
        return result;
    }

    @Override
    public int volumeCredits() {
        return super.volumeCredits() + (int) Math.floor((double) audience() / 5);
    }
}
