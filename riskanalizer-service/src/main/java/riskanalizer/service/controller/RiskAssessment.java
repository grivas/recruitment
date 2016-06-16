package riskanalizer.service.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import riskanalizer.service.dto.Decision;
import riskanalizer.service.dto.DecisionRequest;
import riskanalizer.service.repository.BalanceRepository;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static riskanalizer.service.dto.Decision.DECISION_ACCEPT_OK;
import static riskanalizer.service.dto.Decision.DECISION_REJECT_AMOUNT;
import static riskanalizer.service.dto.Decision.DECISION_REJECT_DEBT;

/**
 * @author german.rivas
 */
@RestController
@RequestMapping(value = "/assess_risk")
public class RiskAssessment {
    public static final int MAX_BALANCE_UPDATE_RETRIES = 3;
    public static final int DEBT_LIMIT = 1001;

    @Inject
    private final BalanceRepository balanceRepository;

    public RiskAssessment(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    /**
     * Assess the risk of the current request. If request amount is greater than 1000 it is
     * automatically rejected with reason: 'amount'. If the total debt of the customer is greater
     * than 1000 after the current request, it is also rejected with reason 'debt'. If non of
     * the conditions mentioned before are met request is accepted with reason:'ok'
     *
     * @param request Request credit
     * @return Decision regarding the current request.
     */
    @RequestMapping(method = POST)
    public Decision assessRisk(@RequestBody @Valid final DecisionRequest request) {
        int requestedAmount = request.getAmount();
        return isDebtLimitGreaterThan(requestedAmount) ? assessCustomerDebt(request) : DECISION_REJECT_AMOUNT;
    }

    private Decision assessCustomerDebt(DecisionRequest request) {
        return retry(() -> checkTotalDebtAndUpdateBalance(request)) ? DECISION_ACCEPT_OK : DECISION_REJECT_DEBT;
    }

    private Optional<Boolean> checkTotalDebtAndUpdateBalance(DecisionRequest request) {
        String customer = request.getEmail();
        int fromBalance = balanceRepository.getBalanceOf(request.getEmail());
        int toBalance = fromBalance + request.getAmount();
        return isDebtLimitGreaterThan(toBalance) ? of(updateBalanceOf(customer, fromBalance, toBalance)) : empty();
    }

    private boolean retry(Supplier<Optional<Boolean>> updateFunction) {
        return retry(updateFunction, 0, MAX_BALANCE_UPDATE_RETRIES);
    }

    private boolean retry(Supplier<Optional<Boolean>> updateFunction, int tries, int maxTries) {
        if (tries == maxTries) return false;
        return updateFunction.get().
                map(successfullyUpdated -> successfullyUpdated || retry(updateFunction, tries + 1, maxTries)).
                orElse(false);
    }

    private boolean isDebtLimitGreaterThan(int amount) {
        return DEBT_LIMIT > amount;
    }

    private boolean updateBalanceOf(String customer, int fromBalance, int toBalance) {
        return balanceRepository.updateBalanceOf(customer, fromBalance, toBalance);
    }
}
