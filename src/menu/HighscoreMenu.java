package menu;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;


public class HighscoreMenu extends Menu {
    
	private final Scanner scanner = new Scanner(System.in);
	    
    @Override
    public void show() {
        // TODO
    	String format = "|%-5s|%-16s|%-10s|";
    	System.out.println(String.format(format, "Rank","Restaurant Name","Score"));
    	ArrayList<String> lines = new ArrayList<String>();
    	try (BufferedReader br = new BufferedReader(new FileReader("highscore.txt"))) {
    	    String line = br.readLine();
    	    
    	    while (line != null) {
    	    	lines.add(line);
    	      	line = br.readLine();
    	    }
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	if(lines.size()==0) {
    		System.out.println("Leaderboard is empty...");
    	}else {
    		Collections.sort(lines, new Comparator<String>(){
			   public int compare(String o1, String o2){
			      return Integer.parseInt(o2.split(",")[1])-Integer.parseInt(o1.split(",")[1]);
			   }
			});
	    	int idx=1;
	    	for (String line : lines) {
	    		System.out.println(String.format(format, idx++,line.split(",")[0], line.split(",")[1]));
		        
			}
    	}
    	
    	System.out.print("Press enter to continue...");
    	scanner.nextLine();
    }
}
