package chapter1;

import chapter1.data.Invoice;
import chapter1.data.Play;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.TestDataFactory;

import java.util.List;
import java.util.Map;

class StatementTest {
    Statement sut;
    TestDataFactory testDataFactory;

    List<Invoice> invoices;
    Map<String, Play> plays;

    @BeforeEach
    void setUp() {
        testDataFactory = new TestDataFactory();
        invoices = testDataFactory.createInvoices();
        plays = testDataFactory.createPlays();

        sut = new Statement(invoices.get(0), plays);
    }

    @Test
    void testStatement() {
        String result = sut.statement();

        Assertions.assertThat(result).isEqualTo(
                """
                        청구 내역 (고객명: BigCo)
                          Hamlet: $650.00 (55석)
                          As You Like It: $580.00 (35석)
                          Othello: $500.00 (40석)
                        총액: $1,730.00
                        적립 포인트: 47점"""
        );
    }
}