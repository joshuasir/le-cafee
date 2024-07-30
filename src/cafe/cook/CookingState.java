package cafe.cook;

public class CookingState extends CookState {
    public CookingState(Cook cook) {
        super(cook);
    }

    @Override
    public void cook(String customer) {

    }

    @Override
    public void done() {
        cook.changeState(new IdleState(cook));
    }

    @Override
    public String getCurrentState() {
        return "Cook (" + cook.getCustomer() + ")";
    }

    @Override
    public void cancel() {
        cook.changeState(new IdleState(cook));
    }
}
