package mediator;

public interface Mediator {
    void notify(Object sender, String event, String data);
}
