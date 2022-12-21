package generator;

import java.util.Random;

import cafe.Cafe;

public class GeneratorManager implements Runnable{
	private final Random rand = new Random();
	private final Object pauseLock = new Object();
	private volatile boolean paused = false;
	private Cafe cafe;
	public GeneratorManager(Cafe cafe) {
		this.cafe = cafe;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
            synchronized (this) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
            if(cafe.isClose()) return;
            if (paused) {
                synchronized (pauseLock) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
            if(rand.nextInt(4)==0) {
            	cafe.addCustomer();
            }
		
		}
	}
	
	public void pause() {
		paused = true;
	}
	public void resume() {
		paused = false;
		synchronized(pauseLock) {
			pauseLock.notify();
		}
	}
}
