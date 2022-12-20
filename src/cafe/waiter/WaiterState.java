package cafe.waiter;

import cafe.state.State;

public abstract class WaiterState implements State {
    protected final Waiter waiter;

    public WaiterState(Waiter waiter) {
        this.waiter = waiter;
    }

    public abstract void takeOrder(String customer);
    public abstract void waitCook();
    public abstract void bringOrder(String cook, String customer);
    public abstract void serveFood();
}
