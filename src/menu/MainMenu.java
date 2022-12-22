package menu;


public class MainMenu extends Menu {
    public static MainMenu instance;
    
	
    private MainMenu() {}

    public static MainMenu getInstance() {
        if (instance == null) {
            instance = new MainMenu();
        }
        return instance;
    }


    private void clearScreen() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }

    @Override
    public void show() {
        String[] menus = { "Play New Cafe", "High Score", "Exit" };
        while (true) {
            clearScreen();
            int idx = 1;
            for (String menu : menus) {
                System.out.print((idx++) + ". ");
                System.out.println(menu);
            }
            idx--;

            switch (getIntInput(">> ",1,idx)) {
                case 1:
                    new CafeMenu().show();                    
                    break;
                case 2:
                	new HighscoreMenu().show();
                	
                    break;
                case 3:
//                	System.out.println("tes");
                    return;
            }
        }
    }

}
