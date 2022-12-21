package cafe.waiter;

import mediator.Mediator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Waiter implements Runnable,Cloneable {
    private final Mediator cafe;
    private final BlockingQueue<String> queue;
    private WaiterState state = new IdleState(this);
    private String customer;
    private String cook;
    private final AtomicInteger timer = new AtomicInteger(0);
    private volatile boolean paused = false;
    private volatile boolean close = false;
    private final Object pauseLock = new Object();
    private final String name;
    private int speed = 1;
    private String event;
    private String data;

    public Waiter(Mediator cafe, BlockingQueue<String> queue, String name) {
        this.cafe = cafe;
        this.queue = queue;
        this.name = name;
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
            if (event != null) {
                cafe.notify(this, event, data);
                setNotify(null, null);
            }
            if (state instanceof TakingOrderState) {
                if (timer.get() > 0) {
                    timer.set(timer.get() - 1);
                } else {
                    state.waitCook();
                    setNotify("finish", customer);
                }
            } else if (state instanceof WaitCookState) {
                try {
                    cook = queue.take();
                    state = new IdleState(this);
                    setNotify("ordered", String.join(";", customer, cook));
                } catch (InterruptedException e) {
                    break;
                }
            } else if (state instanceof BringingOrderState) {
                state.serveFood();
            } else if (state instanceof ServingState) {
                state = new IdleState(this);
                setNotify("give", customer);
            }
        }
    }

    public void changeState(WaiterState state) {
        this.state = state;
    }

    public void setNotify(String event, String data) {
        this.event = event;
        this.data = data;
    }

    public void pause() {
        paused = true;
    }
    public void close() {
        close = true;
    }
    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCook() {
        return cook;
    }

    public void setCook(String cook) {
        this.cook = cook;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setTimer(int timer) {
        this.timer.set(timer);
    }

    public String getName() {
        return name;
    }

    public String getCurrentState() {
        return state.getCurrentState();
    }

    public void takeOrder(String customer) {
        state.takeOrder(customer);
    }

    public void bringOrder(String cook, String customer) {
        state.bringOrder(cook, customer);
    }

    public void cancel() {
        state.cancel();
    }
}
