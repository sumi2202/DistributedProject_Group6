import java.sql.*;

public class Database {
    private Connection myConnection;

    public Database() {
        try {
            // Replace with your database url, username, and password
            String url = "jdbc:mysql://localhost:3306/distributedapp_schema";
            String username = "root@localhost";
            String password = "Lolo@238";

            myConnection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    //function to authenticate the user
    public boolean authenticate(String customerId, String firstName, String lastName, String email) {
        try {
            String query = "SELECT * FROM customers WHERE customer_id = ? AND first_name = ? AND last_name = ? AND email = ?";
            PreparedStatement statement = myConnection.prepareStatement(query);
            statement.setString(1, customerId);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, email);

            ResultSet myResult = statement.executeQuery();
            return myResult.next();
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
            return false;
        }
    }
}
