package cafe.customer;

import cafe.state.State;

public abstract class CustomerState implements State {
    protected final Customer customer;
    private final int toleranceDrain;

    public CustomerState(Customer customer, int toleranceDrain) {
        this.customer = customer;
        this.toleranceDrain = toleranceDrain;
    }

    public abstract void ordering(String waiter);
    public abstract void waitFood();
    public abstract void waitCook(String cook, int skill);
    public abstract void waitWaiter();
    public abstract void eat();

    public int getToleranceDrain() {
        return toleranceDrain;
    }
}
