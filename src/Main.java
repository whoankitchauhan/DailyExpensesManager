import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Daily Expenses Manager");

        // User login or sign-up
        boolean isAuthenticated = false;
        while (!isAuthenticated) {
            System.out.println("Enter 1 to Login or 2 to Sign Up:");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            if (choice == 1) {
                // Login
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                isAuthenticated = AuthManager.authenticateUser(username, password);
                if (isAuthenticated) {
                    System.out.println("Login successful!");
                    // Proceed to the expense management
                    // Call ExpenseManager or any other features
                    ExpenseManager expenseManager = new ExpenseManager(username);
                    expenseManager.showMenu();

                } else {
                    System.out.println("Invalid username or password. Try again.");
                }

            } else if (choice == 2) {
                // Sign Up
                System.out.print("Enter a new username: ");
                String newUsername = scanner.nextLine();
                System.out.print("Enter a password: ");
                String newPassword = scanner.nextLine();

                boolean isRegistered = AuthManager.registerUser(newUsername, newPassword);
                if (isRegistered) {
                    System.out.println("Sign Up successful! You can now log in.");
                } else {
                    System.out.println("Username already exists. Try a different one.");
                }
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }
}
