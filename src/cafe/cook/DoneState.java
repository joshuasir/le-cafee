package cafe.cook;

public class DoneState extends CookState {
    public DoneState(Cook cook) {
        super(cook);
    }

    @Override
    public void cook(String customer) {

    }

    @Override
    public void done() {

    }

    @Override
    public String getCurrentState() {
        return "Done (" + cook.getCustomer() + ")";
    }

    @Override
    public void cancel() {
        cook.changeState(new IdleState(cook));
    }
}
