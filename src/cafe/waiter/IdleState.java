package cafe.waiter;

public class IdleState extends WaiterState {
    public IdleState(Waiter waiter) {
        super(waiter);
    }

    @Override
    public void takeOrder(String customer) {
        waiter.setTimer(6 - waiter.getSpeed());
        waiter.setCustomer(customer);
        waiter.changeState(new TakingOrderState(waiter));
    }

    @Override
    public void waitCook() {

    }

    @Override
    public void bringOrder(String cook, String customer) {
        waiter.setCook(cook);
        waiter.setCustomer(customer);
        waiter.changeState(new BringingOrderState(waiter));
    }

    @Override
    public void serveFood() {

    }

    @Override
    public String getCurrentState() {
        return "Idle";
    }

    @Override
    public void cancel() {

    }
}
