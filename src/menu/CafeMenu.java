package menu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cafe.Cafe;
import generator.GeneratorManager;


public class CafeMenu extends Menu {
	
	private final Cafe cafe = new Cafe();
	private final GeneratorManager customerGenerator = new GeneratorManager(cafe);
    private final ExecutorService pool = Executors.newCachedThreadPool();
//    private boolean inGame = true;
   
    @Override
    public void show() {
        // TODO
    	
    	System.out.print("Input Restaurant Name: ");
    	cafe.setName(scanner.nextLine());
    	
    	pool.execute(cafe);
    	pool.execute(customerGenerator);
    	 do{
    		
    		
    		customerGenerator.pause();
    		cafe.pauseGame();
    		showPause();
    		if(cafe.isClose()) return;
    		scanner.nextLine();
    	}while(!cafe.isClose());
    	
    	
    }
    
    public void showPause() {
		// TODO Auto-generated method stub
		while(true) {
			System.out.println("---------------PAUSE MENU---------------");
	    	System.out.println(cafe.getStatus());
	    	System.out.println("1. Continue Business");
	    	System.out.println("2. Upgrade Restaurant");
	    	System.out.println("3. Close Business");
	        
        	switch(getIntInput(">>",1,3)) {
		        case 1:
		        	cafe.resumeGame();
		        	customerGenerator.resume();
		        	
		        	return;
		        case 2:
		        	showUpgrade();
		        	break;
		        case 3:
		        	cafe.closeBusiness();
		        	return;
		        	
		    }
        }
	}
    public void showUpgrade() {
		// TODO Auto-generated method stub
		while(true) {
			System.out.println(cafe.getStatus());
			System.out.println("1. Increase Restaurant's Seat (Rp. "+cafe.getCurrentSeatsSize()*100+")");
	    	System.out.println("2. Hire New Employee");
	    	System.out.println("3. Upgrade Waiter (Rp. 150)");
	    	System.out.println("4. Upgrade Cook (Rp. 150)");
	    	System.out.println("5. Back to Pause Menu");
	    	
        	switch(getIntInput(">>",1,5)) {
		        case 1:
		        	if(cafe.increaseSeat()) System.out.println("Successfully Added!");;
		        	break;
		        case 2:
		        	showHireNewEmployee();
		        	break;
		        case 3:
		        	showUpgradeWaiter();
		        	break;
		        case 4:
		        	showUpgradeCook();
		        	break;
		        case 5:
		        	
		        	return;
		        	
		    }
        }
	}
    
    public void showHireNewEmployee() {
		// TODO Auto-generated method stub
    	
        
		while(true) {
			System.out.println(cafe.getStatus());
			System.out.println("HIRE NEW EMPLOYEE");
	    	System.out.println("1. Hire New Waiter (Rp. "+150* cafe.getCurrentWaiterSize()+")");
	    	System.out.println("2. Hire New Cook (Rp. "+200* cafe.getCurrentCookSize()+")");
	    	System.out.println("3. Back to Upgrade Menu");
	    	
        	switch(getIntInput(">>",1,3)) {
		        case 1:
		        	if(cafe.addWaiter()) System.out.println("Waiter Successfully Added");;
		        	break;
		        case 2:
		        	if(cafe.addCook()) System.out.println("Cook Successfully Added!");;
		        	break;
		        case 3:
		        	return;
		        	
		    }
        }
	}
    
    public void showUpgradeWaiter() {
		// TODO Auto-generated method stub
		System.out.println("          UPGRADE WAITER (Rp. 150)");
		
    	if(cafe.upgradeWaiter(getIntInput("Input Employee Number to Upgrade : ",0,cafe.showWaiter()-1)-1)) {
    		System.out.println("Successfully Upgraded!");
    	}
	}
    private boolean isCookSkill(String skill) {
    	return skill.toLowerCase().equals("0") ||skill.toLowerCase().equals("speed") || skill.toLowerCase().equals("skill");
    }
    public void showUpgradeCook() {
		// TODO Auto-generated method stub
    	System.out.println("          UPGRADE COOK (Rp. 150)");
		
		int curr = 1;
		String skill="";
    	
    	curr = getIntInput("Input Employee Number to Upgrade : ",0,cafe.showCook()-1);
    	if(curr == 0) return;
    	do {
    		System.out.print("Input Skill to Upgrade : ");
    		skill = scanner.nextLine();
    	}while(!isCookSkill(skill));
    	
    	if(skill.equals("0")) return;
    	
    	if(cafe.upgradeCook(curr-1,skill)) {
    		System.out.println("Cook Successfully Upgraded!");
    	}
    	
	}

}
