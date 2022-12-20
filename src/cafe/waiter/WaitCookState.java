package cafe.waiter;

public class WaitCookState extends WaiterState {
    public WaitCookState(Waiter waiter) {
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
        return "Wait Cook";
    }

    @Override
    public void cancel() {
        waiter.changeState(new IdleState(waiter));
    }
}
