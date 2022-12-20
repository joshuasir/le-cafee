package cafe.customer;

public class WaitWaiterState extends CustomerState {
    public WaitWaiterState(Customer customer) {
        super(customer, 4);
    }

    @Override
    public void ordering(String waiter) {

    }

    @Override
    public void waitFood() {

    }

    @Override
    public void waitCook(String cook, int skill) {

    }

    @Override
    public void waitWaiter() {

    }

    @Override
    public void eat() {
        customer.setTimer(6);
        customer.changeState(new EatingState(customer));
    }

    @Override
    public String getCurrentState() {
        if (customer.getWaiter() != null) {
            return "Wait Waiter (" + customer.getWaiter() + ")";
        }
        return "Wait Waiter";
    }

    @Override
    public void cancel() {

    }
}
