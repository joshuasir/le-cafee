package cafe.cook;

public class IdleState extends CookState {
    public IdleState(Cook cook) {
        super(cook);
    }

    @Override
    public void cook(String customer) {
        cook.setCustomer(customer);
        cook.setTimer(6 - cook.getSpeed());
        cook.changeState(new CookingState(cook));
    }

    @Override
    public void done() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public String getCurrentState() {
        return "Idle";
    }
}
