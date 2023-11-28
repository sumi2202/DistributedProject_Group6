package src;

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
    public boolean authenticate(String firstName, String password) {
        try {
            String query = "SELECT * FROM customers WHERE firstName = ? AND Password = ?";
            PreparedStatement statement = myConnection.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, password);

            ResultSet myResult = statement.executeQuery();
            return myResult.next();
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
            return false;
        }
    }
}
