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
            String url = "jdbc:mysql://localhost:3306/lighthouse";
            connection = DriverManager.getConnection(url, "root", "root");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public User getByUsername(String userName) {
        User user = new User();

        String query = "SELECT id, username, password FROM users WHERE username LIKE ?;";

        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, userName);

            ResultSet resultSet = preparedStmt.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                String username = resultSet.getString(2);
                String password = resultSet.getString(3);
                return new User(id, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

}
