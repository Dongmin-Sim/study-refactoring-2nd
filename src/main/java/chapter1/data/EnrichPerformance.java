package chapter1.data;

public record EnrichPerformance(
        String playID,
        Integer audience
) {
    public EnrichPerformance(String playID, Integer audience) {
        this.playID = playID;
        this.audience = audience;
    }
}
