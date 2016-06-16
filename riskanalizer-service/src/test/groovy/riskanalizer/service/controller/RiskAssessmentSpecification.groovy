package riskanalizer.service.controller

import riskanalizer.service.dto.Decision
import riskanalizer.service.dto.DecisionRequest
import riskanalizer.service.repository.BalanceRepository
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class RiskAssessmentSpecification extends Specification {
    public static final String ACCEPTED = "accepted"
    public static final String REJECTED = "rejected"
    public static final String OK = "ok"
    public static final String AMOUNT = "amount"
    public static final String DEBT = "debt"
    def repository = Mock(BalanceRepository)
    def decisionController = new RiskAssessment(repository);

    def "Purchase should be #decision with reason #reason when accumulated debt is #debt and current amount is #amount"(decision, reason, debt, amount) {
        setup:
        def customer = "test@test.com"
        def request = DecisionRequest.newBuilder().withEmail(customer).withAmount(amount).build()
        def expectedResponse = Decision.newBuilder().withAccepted(ACCEPTED == decision).withReason(reason).build()
        when:
        def result = decisionController.assessRisk(request)
        then:
        expectedResponse == result
        (reason in [OK, DEBT] ? 1 : 0) * repository.getBalanceOf(customer) >> debt
        (OK == reason ? 1 : 0) * repository.updateBalanceOf(customer, _, _) >> true
        where:
        decision | reason | debt | amount
        ACCEPTED | OK     | 0    | 10
        REJECTED | AMOUNT | 0    | 1001
        REJECTED | DEBT   | 999  | 2
    }
}