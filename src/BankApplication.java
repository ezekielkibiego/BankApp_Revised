import java.sql.SQLException;
import java.util.Scanner;

public class BankApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankDatabase bankDatabase = new BankDatabase();

        System.out.println("Welcome to the Bank Application!");
        System.out.println("1. Enter Existing Account");
        System.out.println("2. Create New Account");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                enterExistingAccount(scanner, bankDatabase);
                break;
            case 2:
                createNewAccount(scanner, bankDatabase);
                break;
            default:
                System.out.println("Invalid choice. Exiting application.");
        }

        bankDatabase.closeConnection();
    }

    private static void enterExistingAccount(Scanner scanner, BankDatabase bankDatabase) {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.next();

        if (accountExists(bankDatabase, accountNumber)) {
            manageAccount(scanner, bankDatabase, accountNumber);
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void createNewAccount(Scanner scanner, BankDatabase bankDatabase) {
        System.out.print("Enter new account number: ");
        String newAccountNumber = scanner.next();

        try {
            bankDatabase.createAccount(newAccountNumber);
            System.out.println("Account created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }

    private static void manageAccount(Scanner scanner, BankDatabase bankDatabase, String accountNumber) {
        try {
            double initialBalance = bankDatabase.getBalance(accountNumber);
            BankAccount bankAccount = new BankAccount(accountNumber, initialBalance);

            int choice;
            do {
                System.out.println("1. Deposit");
                System.out.println("2. Withdraw");
                System.out.println("3. Check Balance");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        // Deposit handling
                        System.out.print("Enter deposit amount: ");
                        double depositAmount = scanner.nextDouble();
                        bankAccount.deposit(depositAmount);
                        bankDatabase.updateBalance(accountNumber, bankAccount.getBalance());
                        System.out.println("Deposit successful.");
                        break;
                    case 2:
                        // Withdrawal handling
                        System.out.print("Enter withdrawal amount: ");
                        double withdrawalAmount = scanner.nextDouble();
                        if (bankAccount.withdraw(withdrawalAmount)) {
                            bankDatabase.updateBalance(accountNumber, bankAccount.getBalance());
                            System.out.println("Withdrawal successful.");
                        } else {
                            System.out.println("Insufficient balance.");
                        }
                        break;
                    case 3:
                        // Check balance
                        double balance = bankDatabase.getBalance(accountNumber);
                        System.out.println("Current balance: " + balance);
                        break;
                }
            } while (choice != 0);
        } catch (SQLException e) {
            System.out.println("Error accessing account: " + e.getMessage());
        }
    }

    private static boolean accountExists(BankDatabase bankDatabase, String accountNumber) {
        try {
            double balance = bankDatabase.getBalance(accountNumber);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
