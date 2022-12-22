package cafe.cook;

import mediator.Mediator;

import java.util.concurrent.BlockingQueue;

import cafe.factory.Person;

public class Cook extends Person {
    private volatile CookState state = new IdleState(this);
    private String customer;
    private int speed = 1;
    private int skill = 1;
    
    public Cook(Mediator cafe, BlockingQueue<String> queue, String name) {
        super(cafe, queue, name);
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
    
    public void changeState(CookState state) {
        this.state = state;
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void upSpeed(){
        speed++;
    }

    public void upSkill(){
        skill++;
    }

    public void cook(String customer) {
        state.cook(customer);
        cafe.notify(this, "cooking", customer);
    }

    public String getCurrentState() {
        return state.getCurrentState();
    }

    public void cancel() {
        state.cancel();
    }
}
