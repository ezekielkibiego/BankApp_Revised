import java.sql.*;

public class Database {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/banking_db";
    static final String USER = "ezekiel";
    static final String PASS = "Kibiego22@";
    static final String QUERY = "SELECT * FROM accounts";

    public static void main(String[] args) {
        // Open a connection
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);) {
            // Extract data from result set
            while (rs.next()) {
                // Retrieve by column name
                System.out.print("ID: " + rs.getInt("id"));
                System.out.print(", Account Number : " + rs.getInt("account_number"));
                System.out.print(", Balance: " + rs.getString("balance"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}