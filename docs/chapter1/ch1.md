# 1장 리팩터링: 첫 번째 예시

수백줄 짜리 코드를 수정할 때면 먼저 프로그램의 작동 방식을 더 쉽게 파악할 수 있도록 코드를 여러 함수와 프로그램 요소로 재구성한다. 

"프로그램이 새로운 기능을 추가하기에 편한 구조가 아니라면, 먼저 기능을 추가하기 쉬운 형태로 리팩터링하고 나서 원하는 기능을 추가한다."
- 새로운 기능을 추가하기에 편한 구조란 무엇인가? 


```java
public String statement(Invoice invoice, Map<String, Play> plays) throws Exception {
    double totalAmount = 0;
    double volumeCredits = 0;
    String result = String.format("청구 내역 (고객명: %s)\n", invoice.customer());

    for (Performance perf : invoice.performances()) {
        Play play = plays.get(perf.playId());
        int thisAmount = 0;

        switch (play.type()) {
            case "tragedy" -> {
                thisAmount = 40_000;
                if (perf.audience() > 30) {
                    thisAmount += 1_000 * (perf.audience() - 30);
                }
            }
            case "comedy" -> {
                thisAmount = 30_000;
                if (perf.audience() > 20) {
                    thisAmount += 10_000 + 500 * (perf.audience() - 20);
                }
                thisAmount += 300 * perf.audience();
            }
            default -> throw new Exception("알 수 없는 장르: " + play.type());
        }

        volumeCredits += Math.max(perf.audience() - 30, 0);
        // 희극 관객 5명마다 추가 포인트를 제공한다.
        if ("comedy".equals(play.type())) {
            volumeCredits += Math.floor(perf.audience() / 5);
        }

        // 청구 내역을 출력한다.
        result += String.format("  %s: %s (%s석)\n", play.name(), thisAmount / 100, perf.audience());
        totalAmount += thisAmount;
    }

    result += String.format("총액: %s\n", totalAmount / 100);
    result += String.format("적립 포인트: %s점\n", volumeCredits);

    return result;
}
```
기존 코드는 영수증을 출력하는 목적을 가진 코드. 이 코드는 다음과 같은 역할을 가진다. 
1. 공연 요청이 들어오면 연극의 장르와 관객 규모로 비용 계산 
   2. +추후 할인을 포인트 계산
2. 이를 영수증 포맷으로 반환


기존 코드에서 변화가 기대되는 부분들
1. 청구내역 **출력 형식**의 변화
2. **연극 종류**의 변화
   - 이는 공연료 계산과 
   - 적립 포인트 계산법에 영향

출력형식이 변화하거나 추가한다면 기존의 `statement()` 함수의 문자열을 계산하는 부분에 변경이 필요할 것이고, 연극 종류가 변화한다면 이 역시도 `statement()` 함수의 `switch` 문안에 코드가 추가되거나 변경되어야만 한다.

## 리팩터링의 첫 단계 
테스트 코드 생성.

리팩터링할 코드 영역을 꼼꼼하게 검사해줄 테스트 코드들부터 마련해야 함.  
건설하고 있는 집의 구조를 바꾸는 작업을 하는데 구조변경이 기존 집에 미치는 영향을 안정성 검사를 진행하지 않는 것과 동일한 것 아닐까(건축은 잘 모르지만,,) 

```text
리팩터링하기 전에 제대로 된 테스트부터 마련한다. 테스트는 반드시 자가진단하도록 만든다.
```



