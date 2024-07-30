package cafe.customer;

public class OrderState extends CustomerState {
    public OrderState(Customer customer) {
        super(customer, 2);
    }

    @Override
    public void ordering(String waiter) {
        customer.setWaiter(waiter);
        customer.changeState(new OrderingState(customer));
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

    }

    @Override
    public String getCurrentState() {
        return "Order";
    }

    @Override
    public void cancel() {

    }
}
