package riskanalizer.service.repository;

/**
 * @author german.rivas
 */
public interface BalanceRepository {
    /**
     * Gets the balance of the customer identified by <code>email</code>. If customer does not have a balance
     * it sets it to 0 and returns it;
     *
     * @param email Customer email
     * @return customer balance
     */
    int getBalanceOf(String email);

    /**
     * Updates the customer balance with <code>newBalance</code> if and only if current customer balance
     * is equal to <code>previousBalance</code>.
     *
     * @param email           Customer email
     * @param previousBalance Previous customer balance
     * @param newBalance      New customer balance
     * @return if customer's balance was updated or not.
     */
    boolean updateBalanceOf(String email, Integer previousBalance, Integer newBalance);
}
