package cafe.waiter;

public class BringingOrderState extends WaiterState {
    public BringingOrderState(Waiter waiter) {
        super(waiter);
    }

    @Override
    public void takeOrder(String customer) {

    }

    @Override
    public void waitCook() {

    }

    @Override
    public void bringOrder(String cook, String customer) {

    }

    @Override
    public void serveFood() {
        waiter.changeState(new ServingState(waiter));
    }

    @Override
    public String getCurrentState() {
        return "Bring Order (" + waiter.getCook() + ")";
    }

    @Override
    public void cancel() {
        waiter.changeState(new IdleState(waiter));
    }
}
