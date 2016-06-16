package riskanalizer.service.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import riskanalizer.service.dto.Decision;
import riskanalizer.service.dto.DecisionRequest;
import riskanalizer.service.repository.BalanceRepository;

import java.util.Arrays;
import java.util.Collection;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(Parameterized.class)
public class RiskAssessmentTest {
    public static final String TEST_EMAIL = "test@test.com";
    public static final String ACCEPTED = "accepted";
    public static final String REJECTED = "rejected";
    public static final String AMOUNT = "amount";
    public static final String DEBT = "debt";
    public static final String OK = "ok";
    BalanceRepository balanceRepository = mock(BalanceRepository.class);
    RiskAssessment riskAssessment = new RiskAssessment(balanceRepository);
    private String decision;
    private String reason;
    private int debt;
    private int amount;

    public RiskAssessmentTest(String decision, String reason, int debt, int amount) {
        this.decision = decision;
        this.reason = reason;
        this.debt = debt;
        this.amount = amount;
    }

    @Parameters(name = "Purchase should be {0} with reason {1} when accumulated debt is {2} and current amount is {3}")
    public static Collection sampleRequests() {
        //assessRisk   | reason   | debt | amount
        return Arrays.asList(new Object[][]{
                {ACCEPTED, OK, 0, 10},
                {REJECTED, AMOUNT, 0, 1001},
                {REJECTED, DEBT, 999, 2}
        });
    }

    @Test
    public void assessPurchaseRisk() {
        //given:
        DecisionRequest request = DecisionRequest.newBuilder().
                withEmail(TEST_EMAIL).withAmount(amount).build();
        Decision expectedResponse = Decision.newBuilder().withAccepted(ACCEPTED.equals(decision)).
                withReason(reason).build();
        when(balanceRepository.getBalanceOf(eq(TEST_EMAIL))).thenReturn(debt);
        when(balanceRepository.updateBalanceOf(eq(TEST_EMAIL), anyInt(), anyInt())).thenReturn(true);
        //when:
        Decision decision = riskAssessment.assessRisk(request);
        //then:
        assertEquals(expectedResponse, decision);
        verify(balanceRepository, times(asList("ok","debt").contains(reason) ? 1 : 0)).getBalanceOf(anyString());
        verify(balanceRepository, times("ok".equals(reason) ? 1 : 0)).updateBalanceOf(eq(TEST_EMAIL), anyInt(), anyInt());
    }
}
