package cafe.customer;

import cafe.cook.CookingState;
import mediator.Mediator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Customer implements Runnable {
    private final Mediator cafe;
    private final BlockingQueue<String> waiterQueue;
    private CustomerState state = new OrderState(this);
    private String waiter;
    private String cook;
    private int cookSkill;
    private final AtomicInteger timer = new AtomicInteger(0);
    private final AtomicInteger tolerance;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();
    private final String name;

    public Customer(Mediator cafe, BlockingQueue<String> waiterQueue, String name, int tolerance) {
        this.cafe = cafe;
        this.waiterQueue = waiterQueue;
        this.name = name;
        this.tolerance = new AtomicInteger(tolerance);
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
            if (tolerance.get() == 0) {
                cafe.notify(this, "leave", null);
                break;
            }
            if (state instanceof OrderState) {
                String newWaiter = waiterQueue.poll();
                if (newWaiter != null) {
                    state.ordering(newWaiter);
                    cafe.notify(this, "order", newWaiter);
                }
            }
            if (timer.get() > 0 && !(state instanceof EatingState)) {
                timer.set(timer.get() - 1);
            } else if (timer.get() == 0) {
                timer.set(state.getToleranceDrain());
                tolerance.set(tolerance.get() - 1);
            }
            if (state instanceof EatingState) {
                if (timer.get() > 0) {
                    timer.set(timer.get() - 1);
                } else {
                    cafe.notify(this, "finish", "" + cookSkill*30);
                    break;
                }
            }
        }
    }

    public void changeState(CustomerState state) {
        this.state = state;
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

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public String getCook() {
        return cook;
    }

    public void setCook(String cook) {
        this.cook = cook;
    }

    public int getCookSkill() {
        return cookSkill;
    }

    public void setCookSkill(int cookSkill) {
        this.cookSkill = cookSkill;
    }

    public String getCurrentState() {
        return state.getCurrentState() + "(" + tolerance + ")";
    }

    public String getName() {
        return name;
    }

    public void waitWaiter() {
        state.waitWaiter();
    }

    public void waitFood() {
        state.waitFood();
    }

    public void waitCook(String cook, int cookSkill) {
        state.waitCook(cook, cookSkill);
    }

    public void eat() {
        state.eat();
    }

    public void setTimer(int timer) {
        this.timer.set(timer);
    }
}
