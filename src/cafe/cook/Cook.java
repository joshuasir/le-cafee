package cafe.cook;

import mediator.Mediator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Cook implements Runnable,Cloneable {
    private final Mediator cafe;
    private final BlockingQueue<String> queue;
    private volatile CookState state = new IdleState(this);
    private String customer;
    private final String name;
    private int speed = 1;
    private int skill = 1;
    private final AtomicInteger timer = new AtomicInteger(0);
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();
    private volatile boolean close = false;
    
    public Cook(Mediator cafe, BlockingQueue<String> queue, String name) {
        this.cafe = cafe;
        this.queue = queue;
        this.name = name;
    }

    public void changeState(CookState state) {
        this.state = state;
    }
    @Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
            if (paused) {
                synchronized (pauseLock) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
            if(close) return;
            if (state instanceof CookingState) {
                if (timer.get() > 0) {
                    timer.set(timer.get() - 1);
                } else {
                    try {
                        cafe.notify(this, "wait", customer);
                        String waiter = queue.take();
                        state.done();
                        cafe.notify(this, "done", String.join(";", customer, waiter));
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }
    }
    public void close() {
    	this.close = true;
    }
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public void setTimer(int timer) {
        this.timer.set(timer);
    }

    public void cook(String customer) {
        state.cook(customer);
        cafe.notify(this, "cooking", customer);
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public String getCurrentState() {
        return state.getCurrentState();
    }

    public String getName() {
        return name;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void cancel() {
        state.cancel();
    }
}
