package cafe.customer;

public class EatingState extends CustomerState {
    public EatingState(Customer customer) {
        super(customer, 0);
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

    }

    @Override
    public String getCurrentState() {
        return "Eat";
    }

    @Override
    public void cancel() {

    }
}
