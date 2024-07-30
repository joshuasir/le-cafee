package cafe.customer;

public class OrderingState extends CustomerState {
    public OrderingState(Customer customer) {
        super(customer, -1);
    }

    @Override
    public void ordering(String waiter) {

    }

    @Override
    public void waitFood() {
        customer.changeState(new WaitFoodState(customer));
    }

    @Override
    public void waitCook(String cook, int skill) {

    }

    @Override
    public void waitWaiter() {

    }

    @Override
    public void eat() {

    }

    @Override
    public String getCurrentState() {
        return "Order (" + customer.getWaiter() + ")";
    }

    @Override
    public void cancel() {

    }
}
