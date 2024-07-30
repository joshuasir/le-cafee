package cafe.customer;

public class WaitCookState extends CustomerState {
    public WaitCookState(Customer customer) {
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
        customer.changeState(new WaitWaiterState(customer));
    }

    @Override
    public void eat() {

    }

    @Override
    public String getCurrentState() {
        return "Wait Food (" + customer.getCook() + ")";
    }

    @Override
    public void cancel() {

    }
}
