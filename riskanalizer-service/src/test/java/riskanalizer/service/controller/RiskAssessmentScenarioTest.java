package riskanalizer.service.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import riskanalizer.service.RiskanalizerServiceApplication;
import riskanalizer.service.dto.Decision;
import riskanalizer.service.dto.DecisionRequest;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RiskanalizerServiceApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class RiskAssessmentScenarioTest {
    public static final String URL = "http://localhost:%d/assess_risk";

    @Value("${local.server.port}")
    int port;

    RestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void when_bad_request() {
        assertBadRequest(postForEntity(buildRequest(randomEmail(), 0), Object.class));
        assertBadRequest(postForEntity(buildRequest(randomEmail(), -1), Object.class));
        assertBadRequest(postForEntity(buildRequest("a", 1), Object.class));
        assertBadRequest(postForEntity(buildRequest("", 1), Object.class));
        assertBadRequest(postForEntity(buildRequest("      ", 1), Object.class));
        assertBadRequest(postForEntity(buildRequest(null, 1), Object.class));
    }

    @Test
    public void when_amount_lt_eq_1000() {
        Decision response = postForObject(buildRequest(randomEmail(), 1000), Decision.class);
        assertDecision(response, true, "ok");
    }

    @Test
    public void when_amount_gt_1000() {
        Decision response = postForObject(buildRequest(randomEmail(), 1001), Decision.class);
        assertDecision(response, false, "amount");
    }

    @Test
    public void when_debt_gt_1000() {
        String email = randomEmail();
        Decision response = postForObject(buildRequest(email, 500), Decision.class);
        assertDecision(response, true, "ok");
        response = postForObject(buildRequest(email, 600), Decision.class);
        assertDecision(response, false, "debt");
    }

    @Test
    public void concurrency_test() {
        List<DecisionRequest> requests = Collections.nCopies(5000, buildRequest(randomEmail(), 1));
        long start = System.currentTimeMillis();
        long totalApproved = requests.parallelStream().
                map(x -> restTemplate.postForEntity(serverUrl(), x, Decision.class).getBody()).
                filter(Decision::isAccepted).
                count();
        Assert.assertEquals(1000, totalApproved);
        System.out.println(System.currentTimeMillis() - start);
    }

    private <T> ResponseEntity<T> postForEntity(DecisionRequest request, Class<T> type) {
        return restTemplate.postForEntity(serverUrl(), request, type);
    }

    private <T> T postForObject(DecisionRequest request, Class<T> type) {
        return restTemplate.postForObject(serverUrl(), request, type);
    }

    private String serverUrl() {
        return String.format(URL, port);
    }

    private void assertDecision(Decision response, boolean accepted, String reason) {
        Assert.assertEquals(accepted, response.isAccepted());
        Assert.assertEquals(reason, response.getReason());
    }

    private DecisionRequest buildRequest(String email, int amount) {
        return DecisionRequest.newBuilder().withEmail(email).withAmount(amount).build();
    }

    private void assertBadRequest(ResponseEntity response) {
        Assert.assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    private String randomEmail() {
        return UUID.randomUUID().toString() + "@nn.sj";
    }
}
