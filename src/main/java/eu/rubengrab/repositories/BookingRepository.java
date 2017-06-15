package eu.rubengrab.repositories;

import org.joda.time.Interval;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruben on 20.05.2017.
 */
@Repository
public class BookingRepository {

    private Connection connection = null;

    private void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://eu-cdbr-west-01.cleardb.com:3306/heroku_7ef72774c1bedea";
            connection = DriverManager.getConnection(url, "ba8b030fb31019", "8dac5c1e");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Eroare!!!");
            e.printStackTrace();
        }
    }

    public BookingRepository() {
    }

    public List<Interval> getBookingDatesForHouse(int smartLockDescriptionBundleId) {
        createConnection();

        List<Interval> bookingIntervals = new ArrayList<>();

        String query = "SELECT lighthouse.user_house.date_start, lighthouse.user_house.date_end FROM lighthouse.user_house " +
                "WHERE lighthouse.user_house.id_house = ?;";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);

            preparedStmt.setString(1, String.valueOf(smartLockDescriptionBundleId));

            ResultSet resultSet = preparedStmt.executeQuery();

            while (resultSet.next()) {
                Date startDate = resultSet.getDate(1);
                Date endDate = resultSet.getDate(2);
                bookingIntervals.add(new Interval(startDate.getTime(), endDate.getTime()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();

        return bookingIntervals;
    }
}
