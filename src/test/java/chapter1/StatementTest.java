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
        sut = new Statement();
        testDataFactory = new TestDataFactory();

        invoices = testDataFactory.createInvoices();
        plays = testDataFactory.createPlays();
    }

    @Test
    void testStatement() {
        String result = sut.statement(invoices.get(0), plays);

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