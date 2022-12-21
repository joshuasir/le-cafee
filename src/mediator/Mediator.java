package mediator;

public interface Mediator {
	public void notify(Object sender, String event, String data);
}
