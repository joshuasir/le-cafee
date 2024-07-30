package cafe.cook;

import cafe.state.State;

public abstract class CookState implements State {
    protected final Cook cook;

    public CookState(Cook cook) {
        this.cook = cook;
    }

    public abstract void cook(String customer);
    public abstract void done();

}
