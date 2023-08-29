import java.sql.*;

public class BankDatabase {
    private Connection connection;

    public BankDatabase() {
        try {
            String url = "jdbc:postgresql://localhost:5432/banking_db";
            String user = "ezekiel";
            String password = "Kibiego22@";
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createAccount(String accountNumber) throws SQLException {
        System.out.println("Creating account: " + accountNumber);
        if (!accountExists(accountNumber)) {
            System.out.println("Account does not exist. Creating...");
            insertAccount(accountNumber);
            System.out.println("Account created.");
            connection.commit();
        } else {
            System.out.println("Account already exists.");
        }
    }


    private boolean accountExists(String accountNumber) {
        try {
            String query = "SELECT account_number FROM accounts WHERE account_number = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, accountNumber);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insertAccount(String accountNumber) throws SQLException {
        String query = "INSERT INTO accounts (account_number, balance) VALUES (?, 0)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, accountNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void updateBalance(String accountNumber, double newBalance) throws SQLException {
        String query = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setString(2, accountNumber);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public double getBalance(String accountNumber) throws SQLException {
        double balance = 0.0;
        String query = "SELECT balance FROM accounts WHERE account_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                balance = resultSet.getDouble("balance");
            }
        }
        return balance;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
