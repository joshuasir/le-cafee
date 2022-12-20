package cafe.waiter;

public class ServingState extends WaiterState {
    public ServingState(Waiter waiter) {
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

    }

    @Override
    public String getCurrentState() {
        return "Serving Food (" + waiter.getCustomer() + ")";
    }

    @Override
    public void cancel() {
        waiter.changeState(new IdleState(waiter));
    }
}
