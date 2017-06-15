package eu.rubengrab.repositories;

import eu.rubengrab.model.User;
import org.springframework.stereotype.Repository;

import java.sql.*;

/**
 * Created by Ruben on 11.05.2017.
 */
@Repository
public class UserRepository {

    private Connection connection;

    public UserRepository() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://eu-cdbr-west-01.cleardb.com:3306/heroku_7ef72774c1bedea";
            connection = DriverManager.getConnection(url, "ba8b030fb31019", "8dac5c1e");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public User getByUsername(String userName) {
        User user = new User();

        String query = "SELECT id, username, password, firstName, lastName, email, creditcard FROM users WHERE username LIKE ?;";

        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, userName);

            ResultSet resultSet = preparedStmt.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                String username = resultSet.getString(2);
                String password = resultSet.getString(3);
                String firstName = resultSet.getString(4);
                String lastName = resultSet.getString(5);
                String email = resultSet.getString(6);
                String creditCard = resultSet.getString(7);
                return new User(id, username, password, firstName, lastName, email, creditCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User updateUser(User user) {
        String query = "UPDATE users SET firstName = ?, lastName = ?, email = ?, creditcard = ? WHERE id =?;";

        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, user.getFirstName());
            preparedStmt.setString(2, user.getLastName());
            preparedStmt.setString(3, user.getEmail());
            preparedStmt.setString(4, user.getCreditCard());
            preparedStmt.setInt(5, user.getId());

            preparedStmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
