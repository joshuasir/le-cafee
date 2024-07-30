package mediator;

import Observer.CafeEventListener;
import Observer.CafeEventPublisher;
import cafe.cook.Cook;
import cafe.customer.Customer;
import cafe.factory.Person;
import cafe.factory.PersonFactory;
import cafe.waiter.Waiter;

import java.util.Vector;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class CafeMediator implements Mediator, CafeEventPublisher {
    private final BlockingQueue<String> cookQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> waiterQueue = new LinkedBlockingQueue<>();
    private final CopyOnWriteArrayList<Cook> cooks = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Waiter> waiters = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Customer> customers = new CopyOnWriteArrayList<>();
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final Vector<CafeEventListener> listeners = new Vector<>();
    
    @Override
    public void notify(Object sender, String event, String data) {
    	try {
    		
    	
        if (sender instanceof Waiter) {
            switch (event) {
                case "ordered": {
                    String[] datas = data.split(";");
                    
                    waiterQueue.add(((Waiter) sender).getName());
                    for (Cook cook : cooks) {
                        if (cook.getName().equals(datas[1])) {
                            cook.cook(datas[0]);
                            for (Customer customer : customers) {
                                if (customer.getName().equals(datas[0])) {
                                    customer.waitCook(cook.getName(), cook.getSkill());
                                    break;
                                }
                            }
                            break;
                        }
                    }
                           
                    break;
                }
                case "finish": {
                    for (Customer customer : customers) {
                        if (customer.getName().equals(data)) {
                            customer.waitFood();
                            break;
                        }
                    }
                    break;
                }
                case "give": {
                    for (Customer customer : customers) {
                        if (customer.getName().equals(data)) {
                            waiterQueue.add(((Waiter) sender).getName());
                            customer.eat();
                            break;
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        } else if (sender instanceof Cook) {
            switch (event) {
                case "done": {
                    String[] datas = data.split(";");
                    for (Waiter waiter : waiters) {
                        if (waiter.getName().equals(datas[1])) {
                            cookQueue.add(((Cook) sender).getName());
                            waiter.bringOrder(((Cook) sender).getName(), datas[0]);
                            break;
                        }
                    }
                    break;
                }
                case "wait": {
                    for (Customer customer : customers) {
                        if (customer.getName().equals(data)) {
                            customer.waitWaiter();
                            break;
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        } else if (sender instanceof Customer) {
            switch (event) {
                case "leave": {
                    
                    for (Cook cook : cooks) {
                    	if (cook.getCustomer().equals(((Customer) sender).getName())) {
                            cook.cancel();
                            if (!cookQueue.contains(cook.getName())) {
                                cookQueue.add(cook.getName());
                            }
                        }
                    }
                    for (Waiter waiter : waiters) {
                    	if (waiter.getCustomer().equals(((Customer) sender).getName())) {
                            waiter.cancel();
                            if (!waiterQueue.contains(waiter.getName())) {
                                waiterQueue.add(waiter.getName());
                            }
                        }
                    }
                         
                    if (sender != null) {
                        customers.remove(sender);
                    }
                    for (CafeEventListener listener : listeners) {
                        listener.onEvent("score", "-300");
                    }
                    break;
                }
                case "finish": {
                    for (CafeEventListener listener : listeners) {
                        customers.remove(sender);
                        everyone.remove(sender);
                        listener.onEvent("score", data);
                    }
                    break;
                }
                case "order": {
                    for (Waiter waiter : waiters) {
                    	if (waiter.getName().equals(data)) {
                            waiter.takeOrder(((Customer) sender).getName());
                            break;
                        }
                    }
                }
                default:
                    break;
            }
        }}catch(Exception ex) {
        	
        }
    }

    @Override
    public void listen(CafeEventListener listener) {
        listeners.add(listener);
    }

    public void addCook(String name) {
        Cook cook = new Cook(this, waiterQueue, name);
        cook.pause();
        cooks.add(cook);
        pool.execute(cook);
        cookQueue.add(cook.getName());
    }

    public void addWaiter(String name) {
        Waiter waiter = new Waiter(this, cookQueue, name);
        waiter.pause();
        waiters.add(waiter);
        pool.execute(waiter);
        waiterQueue.add(waiter.getName());
    }
    

    public void addCustomer(String name, int tolerance) {
        Customer customer = new Customer(this, waiterQueue, name, tolerance);
        customers.add(customer);
        pool.execute(customer);
    }
    
    public void upgradeCookSkill(int idx) {
    	Cook toUpd = cooks.get(idx);
    	toUpd.upSkill();
    }

    public void upgradeCookSpeed(int idx) {
    	Cook toUpd = cooks.get(idx);
    	toUpd.upSpeed();
    }

    public void upgradeWaiter(int idx) {
    	Waiter toUpd = waiters.get(idx);
    	toUpd.upSpeed();
    }

    public void pause() {
        for (Person person : everyone) {
            person.pause();
        }
    }
    
    public void close() {
        for(Person person : everyone){
            person.close();
        }
    }

    public void resume() {
        for(Person person : everyone){
            person.resume();
        }
    }
    public CopyOnWriteArrayList<Customer> getCustomers() {
    	CopyOnWriteArrayList<Customer> customerSnapshot = new CopyOnWriteArrayList<Customer>();
//    	Collections.copy(customerSnapshot, customers);
    	Iterator<Customer> itr = customers.iterator();
    	while(itr.hasNext()) {
    		Customer c = itr.next();
    		try {
				customerSnapshot.add((Customer) c.clone());
			} catch (CloneNotSupportedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
    	
    	return customerSnapshot;
    }
    public Customer getCustomer(int index) {
    	try {
			return (Customer) customers.get(index).clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    public Cook getCook(int index) {
    	try {
			return (Cook) cooks.get(index).clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    public Waiter getWaiter(int index) {
    	try {
			return (Waiter) waiters.get(index).clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    public CopyOnWriteArrayList<Cook> getCooks() {
    	CopyOnWriteArrayList<Cook> cookSnapshot = new CopyOnWriteArrayList<Cook>();
//    	Collections.copy(cookSnapshot, cooks);
    	Iterator<Cook> itr = cooks.iterator();
    	while(itr.hasNext()) {
    		Cook c = itr.next();
    		try {
    			cookSnapshot.add((Cook) c.clone());
			} catch (CloneNotSupportedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
    	
    	
    	return cookSnapshot;
    
    }
    public CopyOnWriteArrayList<Waiter> getWaiters() {
    	CopyOnWriteArrayList<Waiter> waiterSnapshot = new CopyOnWriteArrayList<Waiter>();
//    	Collections.copy(waiterSnapshot, waiters);
    	Iterator<Waiter> itr = waiters.iterator();
    	while(itr.hasNext()) {
    		Waiter w = itr.next();
    		try {
    			waiterSnapshot.add((Waiter) w.clone());
			} catch (CloneNotSupportedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
	    
    	return waiterSnapshot;
    }

    public void printCurrentState() {
        System.out.println("=============");
        System.out.println("Customer");
        System.out.println("=============");
        
        Iterator<Customer> itrCustomer = customers.iterator();
        while(itrCustomer.hasNext()) {
        	Customer customer = itrCustomer.next();
            System.out.println(customer.getName() + " | " + customer.getCurrentState());
        }
        System.out.println("=============");
        System.out.println("Waiter");
        System.out.println("=============");
        Iterator<Waiter> itrWaiter = waiters.iterator();
        while(itrWaiter.hasNext()) {
        	Waiter waiter = itrWaiter.next();
            System.out.println(waiter.getName() + " | " + waiter.getCurrentState());
        }
        System.out.println("=============");
        System.out.println("Cook");
        System.out.println("=============");
        
        Iterator<Cook> itrCook = cooks.iterator();
        while(itrCook.hasNext()) {
    		Cook cook = itrCook.next();
            System.out.println(cook.getName() + " | " + cook.getCurrentState());
        }
        System.out.println("Queue Condition");
        System.out.println(String.join(", ", cookQueue));
        System.out.println(String.join(", ", waiterQueue));
        System.out.println("=============");
    }
}
