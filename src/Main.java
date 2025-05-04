import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exitApp = false;

        displayWelcomeBanner();

        while (!exitApp) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleLogin(scanner); // Handle user login
                    break;

                case "2":
                    handleRegistration(scanner); // Handle user registration
                    break;

                case "3":
                    exitApp = true;
                    System.out.println("Exiting Daily Expenses Manager. Have a great day!");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }

        scanner.close();
    }

    // Displays the welcome banner at the start of the program
    private static void displayWelcomeBanner() {
        System.out.println("Welcome to Daily Expenses Manager");
        System.out.println("----------------------------------------");
    }

    // Displays the main menu with available options
    private static void displayMainMenu() {
        System.out.println("\n========== Main Menu ==========");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    // Authenticates the user and launches the expense manager if successful
    private static void handleLogin(Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (AuthManager.authenticateUser(username, password)) {
            System.out.println("\nLogin successful!");
            LocalDate today = LocalDate.now();
            System.out.println("Welcome, " + username + " | Date: " + today);

            ExpenseManager expenseManager = new ExpenseManager(username);
            expenseManager.showMenu();
        } else {
            System.out.println("Invalid username or password. Try again.");
        }
    }

    // Registers a new user by saving their credentials
    private static void handleRegistration(Scanner scanner) {
        System.out.print("Choose a username: ");
        String newUser = scanner.nextLine();
        System.out.print("Choose a password: ");
        String newPass = scanner.nextLine();

        if (AuthManager.registerUser(newUser, newPass)) {
            System.out.println("Registered successfully! Please log in.");
        } else {
            System.out.println("Username already exists. Try a different one.");
        }
    }
}
