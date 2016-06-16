package riskanalizer.service.repository;

import java.util.Map;

/**
 * @author german.rivas
 */
public class DefaultBalanceRepository implements BalanceRepository {
    private final Map<String, Integer> balanceMap;

    public DefaultBalanceRepository(Map<String, Integer> balanceMap) {
        this.balanceMap = balanceMap;
    }

    @Override
    public int getBalanceOf(String email) {
        balanceMap.putIfAbsent(email, 0);
        return balanceMap.get(email);
    }

    @Override
    public boolean updateBalanceOf(String email, Integer previousBalance, Integer newBalance) {
        return balanceMap.replace(email, previousBalance, newBalance);
    }
}
