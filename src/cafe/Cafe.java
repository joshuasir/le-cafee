package cafe;

import java.awt.Menu;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Observer.CafeEventListener;
import cafe.cook.Cook;
import cafe.waiter.Waiter;
import mediator.CafeMediator;
import menu.CafeMenu;
import menu.HighscoreMenu;



public class Cafe implements CafeEventListener,Runnable {
	private CafeMediator cafeMediator;
	private String name = "";
	private Integer money=1300;
	private Integer score = 0;
	private Integer seat = 3;
	private volatile boolean  paused = false;
	private volatile boolean close = false;
	private Integer customerInc = 4;
	private Integer waiterInc = 3;
	private Integer cookInc = 3;
	private final Object pauseLock = new Object();
    
	public String getName() {
		return name;
	}
	public boolean isClose() {
		return close;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Cafe() {
		this.cafeMediator = new CafeMediator();
		cafeMediator.listen(this);
    	openBusiness();
    	
	}
    public String getStatus() {
        // TODO
    	return "                Status                  \n"+
    		   "          Money : Rp. "+money+"\n"+
    		   "          Score : "+score+" Points\n"+
    		   "          Size  : "+seat+" Seats\n";
    	
    }

    public int getCurrentSeatsSize() {
        // TODO
        return seat;
    }
    
    public int getCurrentCustomerSize() {
        // TODO
        return cafeMediator.getCustomers().size();
    }
    public int getCurrentCookSize() {
        // TODO
        return cafeMediator.getCooks().size();
    }
    public int getCurrentWaiterSize() {
        // TODO
        return cafeMediator.getWaiters().size();
    }

    
//    public void startGame() {
//        // TODO
//    	
//    	
//    	run();
//    }

    public void pauseGame() {
        // TODO
    	synchronized (pauseLock) {
            
	    	cafeMediator.pause();
	    	paused=true;
    	}
    }
    
    public void resumeGame() {
        // TODO
    	synchronized (pauseLock) {
            
	    	
	    	paused=false;
            pauseLock.notifyAll();
            cafeMediator.resume();
            
        }
    	
    }
    public void closeBusiness() {
        // TODO
    	
    	try {
  	      FileWriter myWriter = new FileWriter("highscore.txt",true);
  	      myWriter.write(name+","+score+"\n");
  	      myWriter.close();
  	    } catch (IOException e) {
  	      System.out.println("An error occurred.");
  	      e.printStackTrace();
  	    }
    	synchronized (pauseLock) {
    		close=true;
    		cafeMediator.close();
            pauseLock.notifyAll();
            new HighscoreMenu().show();
        }
    }
    public void openBusiness() {
        // TODO
    	close=false;
    	cafeMediator.addCook("C1");
    	cafeMediator.addCook("C2");
    	cafeMediator.addWaiter("W1");
    	cafeMediator.addWaiter("W2");
    	cafeMediator.addCustomer("P1", 4);
    	cafeMediator.addCustomer("P2", 4);
    	cafeMediator.addCustomer("P3", 4);
    }

    public boolean increaseSeat() {
        // TODO
    	if(seat==13 || money<100*seat) return false;
    	money-=100*seat++;
        return true;
    }

    public boolean addWaiter() {
        // TODO
    	if(getCurrentWaiterSize()>=7|| money<150*getCurrentWaiterSize()) return false;
    	money-=150*getCurrentWaiterSize();
    	cafeMediator.addWaiter("W"+waiterInc);
    	waiterInc++;
        return true;
    }

    public boolean addCook() {
        // TODO
    	if(getCurrentCookSize()>=7|| money<200*getCurrentCookSize()) return false;
    	money-=200*getCurrentCookSize();
    	cafeMediator.addCook("C"+cookInc);
    	cookInc++;
        return true;
    }
 
    public boolean addCustomer() {
        // TODO
    	if(getCurrentCustomerSize()>=seat) return false;
    	cafeMediator.addCustomer("P"+customerInc, 4);
    	customerInc++;
        return true;
    }
    public int showWaiter() {
    	int idx = 1;
    	for (Waiter waiter : cafeMediator.getWaiters()) {
			System.out.println(idx++);
			System.out.println("Initial : "+waiter.getName());
			System.out.println("Speed   : "+waiter.getSpeed()+"\n");
		}
    	return idx;
    }

    public boolean upgradeWaiter(int index) {
    	
        // TODO
    	if(index<0) return false;
    	Waiter toUpd = cafeMediator.getWaiters().get(index);
    	if(toUpd.getSpeed()==5 || money<150) return false;
    	cafeMediator.upgradeWaiter(index);
    	
    	money-=150;
  
        return true;
    }

    public int showCook() {
    	int idx = 1;
		for (Cook cook : cafeMediator.getCooks()) {
			System.out.println(idx++);
			System.out.println("Initial : "+cook.getName());
			System.out.println("Speed   : "+cook.getSpeed());
			System.out.println("Skill   : "+cook.getSkill()+"\n");
		}
		
		return idx;
    }


    public boolean upgradeCook(int index, String skill) {
        // TODO
    	System.out.println(index+" "+cafeMediator.getCooks().size());
    	if(index<0|| money<150) return false;
    	Cook toUpd = cafeMediator.getCooks().get(index);
    	
    	if(skill.toLowerCase().equals("speed")) {
    		if(toUpd.getSpeed()==5) return false;
    		cafeMediator.upgradeCookSpeed(index);
    	}else {
    		if(toUpd.getSkill()==5) return false;
    		cafeMediator.upgradeCookSkill(index);
    	}	
    	money-=150;
        return true;
    }
	@Override
	public void onEvent(String event, String data) {
		money+=Integer.parseInt(data);
		score+=Integer.parseInt(data);
        System.out.println("Event : " + event + " | " + data);
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
            if (paused) {
                synchronized (pauseLock) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
            
            System.out.println("\n".repeat(100));
            
            if(close) {
            	return;
            }
            System.out.println("Restaurant "+name);
            cafeMediator.printCurrentState();
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            	System.out.println(ignored.getMessage());
            	
            }
//            if (i == 3) {
//            	cafeMediator.pause();
//                paused = true;
//            }
//            if (i == 10) {
//            	cafeMediator.resume();
//                paused = false;
//            }
        }
//        try {
//  	      FileWriter myWriter = new FileWriter("highscore.txt",true);
//  	      myWriter.write(name+","+score+"\n");
//  	      myWriter.close();
//  	    } catch (IOException e) {
//  	      System.out.println("An error occurred.");
//  	      e.printStackTrace();
//  	    }
//      	highScoreMenu.show();
//        System.out.println("Final Score : "+score);
        
	}
}

