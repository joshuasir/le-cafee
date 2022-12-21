package menu;

import java.util.Scanner;

public abstract class Menu {
	protected abstract void show();
	protected final Scanner scanner = new Scanner(System.in);
    
    protected Integer getIntInput(String text,Integer lower,Integer upper) {
		
		Integer temp=lower-1;
		do {
		try {
			System.out.print(text);
			temp = scanner.nextInt();
		} catch (Exception e) {
			System.out.println("Please input a valid number!");
			temp=lower-1;
		}
		scanner.nextLine();
		}while(!isInRange(temp,lower,upper));
		
		return temp;
	}
    protected Boolean isInRange(Integer num, Integer lower,Integer upper) {
		return (num >=lower && num <=upper);
	}
}
