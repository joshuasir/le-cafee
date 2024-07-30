package mediator;

import cafe.factory.Person;

public interface Mediator {
	public void notify(Person sender, String event, String data);
}
