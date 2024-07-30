package cafe.factory;

import java.util.concurrent.BlockingQueue;

import cafe.cook.Cook;
import cafe.customer.Customer;
import cafe.waiter.Waiter;
import mediator.Mediator;

public class PersonFactory {
    
    public static Person getPerson(String type, Mediator cafe, BlockingQueue<String> queue, String name){
        switch(type){
            case "cook":
            return new Cook(cafe, queue, name);
            case "waiter":
            return new Waiter(cafe, queue, name);
            case "customer":
            return new Customer(cafe, queue, name);
        }
        return null;
    }
}
