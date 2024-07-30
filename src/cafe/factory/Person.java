package cafe.factory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import mediator.Mediator;

public abstract class Person implements Runnable,Cloneable {
    protected final Mediator cafe;
    protected final BlockingQueue<String> queue;
    protected final String name;
    protected final AtomicInteger timer = new AtomicInteger(0);
    protected volatile boolean paused = false;
    protected volatile boolean close = false;
    protected final Object pauseLock = new Object();

    public Person(Mediator cafe, BlockingQueue<String> queue, String name) {
        this.cafe = cafe;
        this.queue = queue;
        this.name = name;
    }

    @Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

    public String getName() {
        return name;
    }

    public void pause() {
        paused = true;
    }

    public void close() {
        close = true;
    }

    public void setTimer(int timer) {
        this.timer.set(timer);
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }
}
