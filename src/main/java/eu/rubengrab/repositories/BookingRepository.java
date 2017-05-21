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

    public BookingRepository() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/lighthouse";
            connection = DriverManager.getConnection(url, "root", "root");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Interval> getBookingDatesForHouse(int smartLockDescriptionBundleId) {

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

        return bookingIntervals;
    }
}
