package menu;

import java.util.Scanner;

public class MainMenu implements Menu {
    public static MainMenu instance;
    private final Scanner scanner = new Scanner(System.in);

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
            System.out.print(">> ");
            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                continue;
            }
            int choice = scanner.nextInt();
            if (choice < 1 || choice > idx) {
                scanner.nextLine();
                continue;
            }
            switch (choice) {
                case 1:
                    new CafeMenu().show();
                    break;
                case 2:
                    new HighscoreMenu().show();
                    break;
                case 3:
                    return;
            }
        }
    }
}
