package riskanalizer.service.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import riskanalizer.service.RiskanalizerServiceApplication
import riskanalizer.service.dto.Decision
import riskanalizer.service.dto.DecisionRequest
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.ConcurrentHashMap

import static org.springframework.http.HttpStatus.BAD_REQUEST

@Unroll
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = RiskanalizerServiceApplication.class)
@WebAppConfiguration
@IntegrationTest
class RiskAssessmentScenarioSpecification extends Specification {
    public static final String ACCEPTED = "accepted"
    public static final String REJECTED = "rejected"
    public static final String OK = "ok"
    public static final String AMOUNT = "amount"
    public static final String DEBT = "debt"

    @Value('${local.server.port}')
    int port;
    @Autowired
    ConcurrentHashMap<String, Integer> dataStore
    RestTemplate restTemplate = new RestTemplate();

    def "Purchase should be #decision with reason #reason when accumulated debt is #debt and current amount is #amount"(decision, reason, debt, amount) {
        setup:
        def email = "test@test.com"
        def request = DecisionRequest.newBuilder().withEmail(email).withAmount(amount).build()
        def expectedResponse = Decision.newBuilder().withAccepted(ACCEPTED == decision).withReason(reason).build()
        dataStore.put(email, debt)
        when:
        def response = restTemplate.postForObject("http://localhost:$port/assess_risk", request, Decision.class)
        then:
        expectedResponse == response
        where:
        decision | reason | debt | amount
        ACCEPTED | OK     | 0    | 10
        REJECTED | AMOUNT | 0    | 1001
        REJECTED | DEBT   | 999  | 2
    }

    def "Service should respond with http bad request when:  email = '#email' & amount = #amount"() {
        setup:
        def request = DecisionRequest.newBuilder().withEmail(email).withAmount(amount).build()
        when:
        def response = restTemplate.postForEntity("http://localhost:$port/assess_risk", request, Object.class)
        then:
        def e = thrown(HttpClientErrorException)
        BAD_REQUEST == e.statusCode
        where:
        email                  | amount
        "valid_email@mail.com" | 0
        "valid_email@mail.com" | -1
        "a"                    | 1
        ""                     | 1
        "      "               | 1
        null                   | 1
    }
}