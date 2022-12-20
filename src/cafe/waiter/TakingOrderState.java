package cafe.waiter;

public class TakingOrderState extends WaiterState {
    public TakingOrderState(Waiter waiter) {
        super(waiter);
    }

    @Override
    public void takeOrder(String customer) {

    }

    @Override
    public void waitCook() {
        waiter.changeState(new WaitCookState(waiter));
    }

    @Override
    public void bringOrder(String cook, String customer) {

    }

    @Override
    public void serveFood() {

    }

    @Override
    public String getCurrentState() {
        return "Take Order (" + waiter.getCustomer() + ")";
    }

    @Override
    public void cancel() {
        waiter.changeState(new IdleState(waiter));
    }
}
