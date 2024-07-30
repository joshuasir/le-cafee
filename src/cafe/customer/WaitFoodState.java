package cafe.customer;

public class WaitFoodState extends CustomerState {
    public WaitFoodState(Customer customer) {
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
        customer.setWaiter(null);
        customer.setCook(cook);
        customer.setCookSkill(skill);
        customer.changeState(new WaitCookState(customer));
    }

    @Override
    public void waitWaiter() {

    }

    @Override
    public void eat() {

    }

    @Override
    public String getCurrentState() {
        return "Wait Food";
    }

    @Override
    public void cancel() {

    }
}
