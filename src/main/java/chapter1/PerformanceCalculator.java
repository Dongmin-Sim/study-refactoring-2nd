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
}
