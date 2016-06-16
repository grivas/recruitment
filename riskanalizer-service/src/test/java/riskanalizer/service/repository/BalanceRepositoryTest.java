package riskanalizer.service.repository;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * @author german.rivas
 */
public class BalanceRepositoryTest {
    private final BalanceRepository balanceRepository = new DefaultBalanceRepository(new ConcurrentHashMap<>());

    @Test
    public void concurrent_balance_updates() {
        IntStream.range(0, 10000).parallel().forEach(x -> {
            boolean repeat;
            do {
                int balance = balanceRepository.getBalanceOf("test");
                repeat = !balanceRepository.updateBalanceOf("test", balance, balance + 1);
            } while (repeat);
        });
        assertEquals(10000, balanceRepository.getBalanceOf("test"));
    }
}
