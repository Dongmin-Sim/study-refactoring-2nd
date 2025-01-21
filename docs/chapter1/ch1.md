# 1장 리팩터링: 첫 번째 예시

수백줄 짜리 코드를 수정할 때면 먼저 프로그램의 작동 방식을 더 쉽게 파악할 수 있도록 코드를 여러 함수와 프로그램 요소로 재구성한다. 

"프로그램이 새로운 기능을 추가하기에 편한 구조가 아니라면, 먼저 기능을 추가하기 쉬운 형태로 리팩터링하고 나서 원하는 기능을 추가한다."
- 새로운 기능을 추가하기에 편한 구조란 무엇인가? 

최초 코드
```java
public String statement(Invoice invoice, Map<String, Play> plays) throws Exception {
    double totalAmount = 0;
    double volumeCredits = 0;
    String result = String.format("청구 내역 (고객명: %s)\n", invoice.customer());
    final NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

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

```
💡 
주어진 요구사항을 글에 쓰여있는대로 작성하다보니 원본 같은 글(코드)이 나오게 된다.   
자연어의 글 형식과 구조를 가지고 쓰여진 코드를 쪼개고 나누어가는 과정이 리팩터링일까? 
```

## 리팩터링의 첫 단계 
테스트 코드 생성.

리팩터링할 코드 영역을 꼼꼼하게 검사해줄 테스트 코드들부터 마련해야 함.  
건설하고 있는 집의 구조를 바꾸는 작업을 하는데 구조변경이 기존 집에 미치는 영향을 안정성 검사를 진행하지 않는 것과 동일한 것 아닐까(건축은 잘 모르지만,,) 

```text
리팩터링하기 전에 제대로 된 테스트부터 마련한다. 테스트는 반드시 자가진단하도록 만든다.
```
💡어떻게 보면 테스트 코드를 만드는 것은 자연스러운 것이라는 생각이 들었다. 

## 1.4 statement() 함수 쪼개기 

메우 긴 함수를 리팩터링할 때는 먼저 전체 동작을 각각의 부분으로 나눌 수 있는 지점을 찾는다.  
원본 코드에서 가장 만저 눈에 띄는 부분은 기다란 `switch`문.  

코드 분석을 통해 `switch`문은 한 번의 공연에 대한 요금을 계산하고 있음을 알 수 있다.  
이 switch문을 볼때 코드가 의미하는 동작을 파악하는데 들이는 비용을 줄여야한다.   

이는 이 코드를 다시 보더라도 어떤 일을 했었는지 파악한 정보를 코드로 반영시킬 수 있다.
- 코드가 하는 일을 나타내는 `이름`을 지어주는 방법이 있다.

### 함수 추출하기 - 공연 요금 계산 로직
바로 `switch` 코드 조각을 함수로 추출하는 것이다. 

함수로 추출할 때는 다음과 같은 절차를 사용할 수 있다. 
1. 별도 함수로 추출했을 때, 유효범위를 벗어나는 변수를 파악한다.
   - 위 코드에서는 `perf`, `play`, `thisAmount` 가 속한다.
2. 변수들의 성향을 파악해서 함수의 입력과 출력을 설정한다.
   - `perf`와 `play`는 추출한 새 함수에서도 필요하지만 **값을 변경하지는 않는다**.
     - 매개변수로 입력받도록
   - `thisAmount`의 경우에는 **함수 내부에서 값이 변경된다**. 이런 변수는 조심해서 다뤄야 한다.
     - 내부에서 초기화 후 반환하도록


함수를 추출하는 리팩터링을 수행하고 나면 컴파일하고 테스트하는 습관을 가지는 것이 좋다. 
- 간단한 수정이라도 사람은 실수를 하기 마련. (컴퓨터는 실수하지 않는다..)
- 작은 한 가지를 수정할때에는 오류가 생기더라도 변경 폭이 작기 때문에 살펴볼 범위도 좁다. 
  - 문제 찾고 해결하기가 훨씬 수월.
- 수정하려다 실수를 저지르는 것이 디버깅하기 어려워 작업시간이 늘어날 수도 있다.

> 리팩터링은 프로그램의 수정을 작은 단계로 나눠 진행함. 그래서  
중간에 실수하더라도 버그를 쉽게 찾을 수 있다.

함수를 추출하고 나면 추출된 함수 코드를 살펴보면서 지금보다 **더 명확하게 표현**할 수 있는 방법들은 없는지 검토한다.
- 함수의 이름
- 함수 매개변수의 이름
- 지역변수의 이름
- 반환 값의 이름

> 이름 바꾸는 것이 가치가 있을까?   
> 좋은 코드라면 하는 일이 명확히 드러나야함. 이때, 변수 이름은 커다란 역할을 함. 

### 임시변수 제거 - play, 질의 함수로 바꾸기 

긴 함수를 쪼갤 때마다 임시 변수들을 로컬 범위에서 제거하는 편.  
메서드 내부에서 사용되는 임시 변수를 제거하고, 대신 해당 값을 계산하여 반환하는 별도의 메서드(질의 함수)를 만드는 것을 의미한다.

> 💡 임시변수란 무엇일까?  
> 짧은 수명을 가지는 변수로, 일반적으로 특정 계산 결과나 데이터를 일시적으로 저장하기 위해 사용됨. 이 변수는 보통 로컬 범위를 가지며, 함수나 코드 블록 내부에서만 유효함.   
> 임시 변수는 코드의 가독성을 높이거나, 복잡한 계산 결과를 재사용하기 위해 활용.

리팩터링 전 최초 코드를 확인해보자. 여기서 임시변수란 다음 변수들이 해당될 수 잇다.
1. `totalAmount`:
- 총액을 계산하기 위해 사용되는 변수.
- 반복문을 통해 각 공연의 금액(thisAmount)을 누적하여 최종 청구 금액을 계산.
2. `volumeCredits`:
- 적립 포인트를 계산하기 위해 사용되는 변수.
- 각 공연의 관객 수와 장르에 따라 적립 포인트를 누적.
3. `result`:
- 청구 내역 문자열을 생성하기 위해 사용되는 변수.
- 반복문을 통해 각 공연의 정보를 추가하고, 최종적으로 총액 및 적립 포인트를 포함한 청구 내역을 반환.
4. `play`:
- 각 공연(Performance)에 해당하는 연극 정보를 임시로 저장하는 변수.
- 반복문 내부에서 현재 공연의 playId를 이용해 plays 맵에서 해당 연극 정보를 가져옴.
5. `thisAmount`:
- 현재 공연의 금액을 계산하여 저장하는 변수.
- 공연의 장르와 관객 수에 따라 금액이 계산되고, 이후 totalAmount에 누적.
6. `format`:
- 통화 형식을 지정하기 위해 생성된 변수.
- NumberFormat.getCurrencyInstance(Locale.US)를 통해 통화 형식을 지정(금액 포맷팅에 활용).


```java
private double amountFor(Performance performance) {
   double result;
   switch (playFor(performance).type()) { // Play를 얻기 위한 질의함수 playFor
      // ...
   }
    // ...
}
```
지역변수를 제거해서 얻는 가장 큰 장점은 추출작업이 훨씬 쉬워진다는 것.  
유효 범위를 신경 써야 할 대상이 줄어들기 때문임. 

따라서 추출 작업 전, 지역변수부터 제거하면   
추출할 때 유효범위를 벗어나거나 신경써야하는 변수를 파악하는 것이 보다 쉬워진다.
> play라는 지역변수를 질의 함수로 변경하면 amountFor 함수를 추출할 때, 신경써야하는 변수는 `perf`, `thisAmount` 이므로 간단해진다.

```java
private double amountFor(Performance performance) {
    double result;
    switch (playFor(performance).type()) {
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
        default -> throw new IllegalArgumentException("알 수 없는 장르: " + playFor(performance).type());
    }
    return result;
}

private Play playFor(Performance performance) {
    return plays.get(performance.playID());
}
```
하지만 임시변수를 사용했을 때와 비교해서 play 가져오는 연산의 횟수는 증가했다.  
임시 변수를 제거하고 질의 함수를 통해 값을 얻으면, 연산결과를 재사용하지 않으므로 연산량은 증가한다. (성능은 코드와 로직에 따라 달라진다. 성능적인 측면은 리팩터링된 베이스 코드에서 더 쉽게 개선 가능하다고 함.)

### 변수 인라인화
임시변수를 제거할 수 있으면 제거하고 변수 인라인화를 진행.
```java
thisAmount = amountFor(perf);
// ...
totalAmount += thisAmount;
---
totalAmount += amountFor(perf);
```
컴파일-테스트-커밋 단계를 수행한다.

### 함수 추출하기(적립 포인트 계산 로직)
앞서 공연 요금 계산 로직을 별도 함수로 추출한 것과 동일한 단계로 리팩터링을 수행한다.

1. 함수를 추출하기전 유효범위의 변수들을 파악하고 
2. 함수를 추출한다. 
3. 추출된 함수를 살펴보면서 명확한 이름으로 변경할 부분이 없는지 검토한다.

그리고 이 과정속에서 컴파일-테스트-커밋 단계를 수행 계속 수행한다.


### 임시 변수 제거 - format 변수
임시 변수는 자신이 속한 루틴에서만 의미가 있어 루틴이 길고 복잡해지기 쉬움. 때문에 나중에 문제를 일으킬 수 있다.

```java
final NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
```
format은 임시 변수에 함수를 대입한 형태인데, 함수를 직접 선언해서 사용하도록 바꾸는 방법을 사용할 수도 있음.

```java
private String format(double number) {
    return NumberFormat.getCurrencyInstance(Locale.US).format(number / 100);
}
```
> 함수 변수를 일반 함수로 변경하는 것도 리팩터링이다. 굉장히 간단한데다 드물게 사용되어 이 책에서 별도로 이름을 부여하지는 않음.

함수가 하는 일을 함수명을 통해 충분히 설명해주어야 한다. 현재 `format`은 이 함수가 하는 일을 충분하게 설명해주지 못함.   

이 함수의 핵심 역할은 **"화폐 단위 맞추기"** 이다.

```java
private String usd(double number) {
    return NumberFormat.getCurrencyInstance(Locale.US).format(number / 100);
}
```
이름짓기는 중요하면서도 쉽지 않은 작업임. 긴 함수를 잘개 쪼개는 리팩터링은 이름을 잘지어야만 효과가 있음. 
이름이 좋으면 함수 본문을 읽지 않고도 무슨 일을 하는지 유추할 수 있음.
- 한번에 좋은 이름을 짓기는 쉽지 않음. 
- 따라서 처음에는 당장 떠오르는 최선의 이름을 사용하다가 
- 나중에 더 좋은 이름이 떠오를 때 바꾸는 식이 좋음. 

### 임시 변수 제거 - volumeCredits 변수
1. 반복문 쪼개기::Split
   - 변수 값을 누적시키는 부분을 분리함.
2. 문장 슬라이스하기::Slice
    - 변수 초기화 문장을 변수 값 누적 코드 바로 앞으로 옮김.
3. 함수 추출하기::Extract
   - 적립 포인트 계산로직을 함수로 추출함. 
   - 임시변수를 질의 함수로 바꾸기를 사용한다. 
4. 변수 인라인화::Inline

SSEL

> 💡  
> 반복문을 쪼개서 중복된다면 성능에 영향을 미치는 것 아닐까?  
> 위의 코드 정도로는 중복이 성능에 영향을 미치지는 않음. 이는 컴파일러가 최신 캐싱기법이나, 최적화를 통해서 결과를 내기 때문에, 성능차이를 바로 체감하기는 어려움. 하지만 때로는 리팩터링이 성능에 상당항 영향을 주기도 하지만, 그런 경우에도 개의치 않고 리팩터링한다고 함.  
> 잘 다듬어진 코드라야 성능 개선 작업도 훨씬 수월. 리팩터링 과정에서 성능이 크게 떨어지면, 이후 성능 개선하면 됨

### 임시 변수 제거 - totalAmount 변수
이 역시도 위에서 진행했던 방법과 동일한 방법으로 리팩터링을 수행한다. 

`Split - Slice - Extract - Inline`