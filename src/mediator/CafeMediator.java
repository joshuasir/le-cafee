package mediator;

import Observer.CafeEventListener;
import Observer.CafeEventPublisher;
import cafe.cook.Cook;
import cafe.customer.Customer;
import cafe.waiter.Waiter;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class CafeMediator implements Mediator, CafeEventPublisher {
    private final BlockingQueue<String> cookQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> waiterQueue = new LinkedBlockingQueue<>();
    private final ArrayList<Cook> cooks = new ArrayList<>();
    private final ArrayList<Waiter> waiters = new ArrayList<>();
    private final ArrayList<Customer> customers = new ArrayList<>();
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final ArrayList<CafeEventListener> listeners = new ArrayList<>();
    @Override
    public void notify(Object sender, String event, String data) {
        if (sender instanceof Waiter) {
            switch (event) {
                case "ordered": {
                    String[] datas = data.split(";");
                    for (Waiter waiter : waiters) {
                        if (sender == waiter) {
                            waiterQueue.add(waiter.getName());
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
                    Customer leaving = null;
                    for (Customer customer : customers) {
                        if (customer == sender) {
                            leaving = customer;
                            for (Cook cook : cooks) {
                                if (cook.getCustomer().equals(customer.getName())) {
                                    cook.cancel();
                                    if (!cookQueue.contains(cook.getName())) {
                                        cookQueue.add(cook.getName());
                                    }
                                }
                            }
                            for (Waiter waiter : waiters) {
                                if (waiter.getCustomer().equals(customer.getName())) {
                                    waiter.cancel();
                                    if (!waiterQueue.contains(waiter.getName())) {
                                        waiterQueue.add(waiter.getName());
                                    }
                                }
                            }
                            break;
                        }
                    }
                    if (leaving != null) {
                        customers.remove(leaving);
                    }
                    for (CafeEventListener listener : listeners) {
                        listener.onEvent("score", "-300");
                    }
                    break;
                }
                case "finish": {
                    for (CafeEventListener listener : listeners) {
                        customers.remove(sender);
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
        }
    }

    @Override
    public void listen(CafeEventListener listener) {
        listeners.add(listener);
    }

    public void addCook(String name) {
        Cook cook = new Cook(this, waiterQueue, name);
        cooks.add(cook);
        pool.execute(cook);
        cookQueue.add(cook.getName());
    }

    public void addWaiter(String name) {
        Waiter waiter = new Waiter(this, cookQueue, name);
        waiters.add(waiter);
        pool.execute(waiter);
        waiterQueue.add(waiter.getName());
    }

    public void addCustomer(String name, int tolerance) {
        Customer customer = new Customer(this, waiterQueue, name, tolerance);
        customers.add(customer);
        pool.execute(customer);
    }

    public void pause() {
        for (Waiter waiter : waiters) {
            waiter.pause();
        }
        for (Cook cook : cooks) {
            cook.pause();
        }
        for (Customer customer : customers) {
            customer.pause();
        }
    }

    public void resume() {
        for (Waiter waiter : waiters) {
            waiter.resume();
        }
        for (Cook cook : cooks) {
            cook.resume();
        }
        for (Customer customer : customers) {
            customer.resume();
        }
    }

    public void printCurrentState() {
        System.out.println("=============");
        System.out.println("Customer");
        System.out.println("=============");
        for (Customer customer : customers) {
            System.out.println(customer.getName() + " | " + customer.getCurrentState());
        }
        System.out.println("=============");
        System.out.println("Waiter");
        System.out.println("=============");
        for (Waiter waiter : waiters) {
            System.out.println(waiter.getName() + " | " + waiter.getCurrentState());
        }
        System.out.println("=============");
        System.out.println("Cook");
        System.out.println("=============");
        for (Cook cook : cooks) {
            System.out.println(cook.getName() + " | " + cook.getCurrentState());
        }
        System.out.println("Queue Condition");
        System.out.println(String.join(", ", cookQueue));
        System.out.println(String.join(", ", waiterQueue));
        System.out.println("=============");
    }
}
