import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExpenseManager {

    private String username;
    private String filePath;
    private final String budgetFile = "config/budgets.properties";
    private Properties budgets = new Properties();

    public ExpenseManager(String username) {
        this.username = username;
        String today = LocalDate.now().toString(); // e.g. 2025-04-17
        this.filePath = "data/" + username + "_" + today + ".txt";
        ensureFileExists();
        try {
            File budgetData = new File(budgetFile);
            if (!budgetData.exists()) {
                budgetData.createNewFile();
            }
            FileInputStream in = new FileInputStream(budgetData);
            budgets.load(in);
            in.close();
        } catch (IOException e) {
            System.out.println("Error loading budget file.");
        }

    }

    // Create file if not already present
    private void ensureFileExists() {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile(); // Creates today's file
            }
        } catch (IOException e) {
            System.out.println("Error creating expense file: " + e.getMessage());
        }
    }

    // Menu options
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Add Expense");
            System.out.println("2. Update Expense");
            System.out.println("3. Delete Expense");
            System.out.println("4. Display Expenses");
            System.out.println("5. Calculate Total Expense");
            System.out.println("6. Set/Update Daily Budget");
            System.out.println("7. View Weekly/Monthly History");
            System.out.println("8. Export to CSV");
            System.out.println("9. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addExpense(scanner);
                case 2 -> updateExpense(scanner);
                case 3 -> deleteExpense(scanner);
                case 4 -> displayExpenses();
                case 5 -> calculateTotal();
                case 6 -> setOrUpdateBudget(scanner);
                case 7 -> viewHistoryMenu(scanner);
                case 8 -> exportToCSV();
                case 9 -> {
                    System.out.println("Exiting... Have a good day!");
                    exit = true;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void addExpense(Scanner scanner) {
        try {
            System.out.print("Enter expense category (e.g., Food, Travel): ");
            String category = scanner.nextLine().trim();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter description (optional): ");
            String description = scanner.nextLine().trim();

            // Format the time to "HH:mm:ss"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String time = LocalTime.now().format(formatter); // Time when expense is added

            FileWriter writer = new FileWriter(filePath, true); // append mode
            writer.write(category + " | " + amount + " | " + description + " | " + time + "\n");
            writer.close();

            System.out.println("Expense added successfully!");
        } catch (IOException e) {
            System.out.println("Error adding expense: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format. Please enter a number.");
        }
    }

    private void updateExpense(Scanner scanner) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            if (lines.isEmpty()) {
                System.out.println("No expenses found for today.");
                return;
            }

            System.out.println("Your current expenses:");
            for (int i = 0; i < lines.size(); i++) {
                System.out.println((i + 1) + ". " + lines.get(i));
            }

            System.out.print("Enter the number of the expense you want to update: ");
            int index = Integer.parseInt(scanner.nextLine()) - 1;

            if (index < 0 || index >= lines.size()) {
                System.out.println("Invalid expense number.");
                return;
            }

            System.out.print("Enter new category: ");
            String newCategory = scanner.nextLine();

            System.out.print("Enter new amount: ");
            double newAmount = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter new description: ");
            String newDescription = scanner.nextLine();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String newTime = LocalTime.now().format(formatter); // Time when expense is added
            String updatedLine = newCategory + " | " + newAmount + " | " + newDescription + " | " + newTime;

            lines.set(index, updatedLine);
            Files.write(Paths.get(filePath), lines);

            System.out.println("Expense updated successfully.");

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error updating expense: " + e.getMessage());
        }
    }

    private void deleteExpense(Scanner scanner) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            if (lines.isEmpty()) {
                System.out.println("No expenses found for today.");
                return;
            }

            // Display the expenses
            System.out.println("Your current expenses:");
            for (int i = 0; i < lines.size(); i++) {
                System.out.println((i + 1) + ". " + lines.get(i));
            }

            // Get index of expense to delete
            System.out.print("Enter the number of the expense you want to delete: ");
            int index = Integer.parseInt(scanner.nextLine()) - 1;

            if (index < 0 || index >= lines.size()) {
                System.out.println("Invalid expense number.");
                return;
            }

            // Remove the selected expense from the list
            lines.remove(index);

            // Write the updated list back to the file
            Files.write(Paths.get(filePath), lines);
            System.out.println("Expense deleted successfully.");

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error deleting expense: " + e.getMessage());
        }
    }

    public void displayExpenses() {
        File expenseFile = new File(filePath);
        if (!expenseFile.exists()) {
            System.out.println("No expenses recorded for today.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(expenseFile))) {
            String line;
            int lineNumber = 1;
            System.out.println("Today's Expenses:\n");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                if (parts.length == 4) {
                    String category = parts[0].trim();
                    double amount = Double.parseDouble(parts[1].trim());
                    String description = parts[2].trim();
                    String time = parts[3].trim();

                    // Format and display
                    System.out.printf("%d. Category: %-10s | Amount: %.2f | Description: %-20s | Time: %s%n",
                            lineNumber, category, amount, description, time);
                    lineNumber++;
                }
            }

            if (lineNumber == 1) {
                System.out.println("No expenses found.");
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading expense file: " + e.getMessage());
        }
    }

    private void calculateTotal() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            if (lines.isEmpty()) {
                System.out.println("No expenses recorded for today.");
                return;
            }

            double total = 0;
            for (String line : lines) {
                String[] parts = line.split(" \\| ");
                if (parts.length == 4) {
                    try {
                        double amount = Double.parseDouble(parts[1].trim());
                        total += amount;
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping malformed line: " + line);
                    }
                }
            }

            System.out.printf("Total expense for today: %.2f%n", total);

            // Budget alert
            String budgetStr = budgets.getProperty(username);
            if (budgetStr != null) {
                try {
                    double budget = Double.parseDouble(budgetStr);
                    if (total > budget) {
                        System.out.println("Alert: You have exceeded your daily budget of ₹" + budget);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid budget format.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error calculating total expense: " + e.getMessage());
        }
    }

    private void setOrUpdateBudget(Scanner scanner) {
        try {
            System.out.print("Enter your daily budget amount: ");
            String input = scanner.nextLine().trim();
            double budget = Double.parseDouble(input);

            budgets.setProperty(username, String.valueOf(budget));

            // Save to file
            FileOutputStream out = new FileOutputStream(budgetFile);
            budgets.store(out, "User budgets");
            out.close();

            System.out.println("Daily budget set to " + budget);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (IOException e) {
            System.out.println("Error saving budget: " + e.getMessage());
        }
    }

    private void viewHistoryMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- View Expense History ---");
            System.out.println("1. Weekly History (Last 7 days)");
            System.out.println("2. Monthly History (Last 30 days)");
            System.out.println("3. Back");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> showHistory(7);
                case 2 -> showHistory(30);
                case 3 -> {
                    return; // go back to main menu
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void showHistory(int days) {
        LocalDate today = LocalDate.now();

        for (int i = 0; i < days; i++) {
            LocalDate date = today.minusDays(i);
            String fileName = "data/" + username + "_" + date + ".txt";
            File file = new File(fileName);

            if (file.exists()) {
                System.out.println("\n" + date + " Expenses:");
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    int lineNum = 1;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("  " + lineNum + ". " + line);
                        lineNum++;
                    }
                    if (lineNum == 1) {
                        System.out.println("  No expenses found.");
                    }
                } catch (IOException e) {
                    System.out.println("Error reading file for date " + date + ": " + e.getMessage());
                }
            }
        }
    }

    private void exportToCSV() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            if (lines.isEmpty()) {
                System.out.println("No expenses to export.");
                return;
            }

            // Make sure export/ folder exists
            File exportDir = new File("export");
            if (!exportDir.exists()) {
                exportDir.mkdir();
            }

            // Extract filename only from original path
            String fileName = new File(filePath).getName().replace(".txt", ".csv");
            String csvPath = "export/" + fileName;

            BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath));
            writer.write("Category,Amount,Description,Time\n"); // CSV header

            for (String line : lines) {
                String[] parts = line.split(" \\| ");
                if (parts.length == 4) {
                    writer.write(parts[0].trim() + "," + parts[1].trim() + "," + parts[2].trim() + "," + parts[3].trim()
                            + "\n");
                }
            }
            writer.close();

            System.out.println("Expenses exported successfully to: " + csvPath);
        } catch (IOException e) {
            System.out.println("Error exporting to CSV: " + e.getMessage());
        }
    }

}
