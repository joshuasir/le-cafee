package cafe.state;

public interface State {
    public String getCurrentState();
    public void cancel();
}
