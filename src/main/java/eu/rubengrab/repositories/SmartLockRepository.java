package eu.rubengrab.repositories;

import eu.rubengrab.model.Beacon;
import eu.rubengrab.model.Location;
import eu.rubengrab.model.SmartLockDescriptionBundle;
import eu.rubengrab.model.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by Ruben on 11.05.2017.
 */
@Repository
public class SmartLockRepository {

    public static final int DEFAULT_ID_VALUE = -1;
    private static final int NULL_VALUE = 0;

    private Connection connection = null;

    public SmartLockRepository() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/lighthouse";
            connection = DriverManager.getConnection(url, "root", "root");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getUnlockCode(Long major, Long minor, String uuid) {

        String pepper = String.valueOf(Calendar.getInstance().get(Calendar.SECOND));


        String query = "SELECT beacon_password, beacon_separator  FROM houses WHERE beacon_major LIKE ? AND beacon_minor LIKE ? AND beacon_uuid LIKE ?;";

        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);

            preparedStmt.setString(1, String.valueOf(major));
            preparedStmt.setString(2, String.valueOf(minor));
            preparedStmt.setString(3, uuid);

            ResultSet resultSet = preparedStmt.executeQuery();

            resultSet.next();

            String beacon_password = resultSet.getString(1);
            String separator = resultSet.getString(2);

            return beacon_password + separator + pepper;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "" + pepper;
    }

    public String getMacAddress(Long major, Long minor, String uuid) {
        String query = "SELECT mac_address  FROM houses WHERE beacon_major LIKE ? AND beacon_minor LIKE ? AND beacon_uuid LIKE ?;";

        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);

            preparedStmt.setString(1, String.valueOf(major));
            preparedStmt.setString(2, String.valueOf(minor));
            preparedStmt.setString(3, uuid);

            ResultSet resultSet = preparedStmt.executeQuery();

            resultSet.next();

            String address = resultSet.getString(1);

            return address;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public List<SmartLockDescriptionBundle> getUserDescriptions(User userForToken) {
        List<SmartLockDescriptionBundle> smartLockDescriptionBundleList = new ArrayList<>();

        String query = "SELECT h.id, h.address, h.name , h.beacon_major, h.beacon_minor, h.beacon_uuid, h.gps_latitude, h.gps_longitude, h.price_per_night, h.facilities, uh.date_start, uh.date_end " +
                "FROM users AS u " +
                "INNER JOIN user_house AS uh " +
                "   ON u.id = uh.id_user " +
                "INNER JOIN houses AS h " +
                "   ON h.id = uh.id_house " +
                "WHERE u.username LIKE ? AND (DATE(uh.date_start) <= DATE(NOW()) AND DATE(uh.date_end) >= DATE(NOW()))";

        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);

            preparedStmt.setString(1, userForToken.getUsername());

            ResultSet resultSet = preparedStmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String address = resultSet.getString(2);
                String name = resultSet.getString(3);
                String major = resultSet.getString(4);
                String minor = resultSet.getString(5);
                String uuid = resultSet.getString(6);
                String gps_longitude = resultSet.getString(7);
                String gps_latitude = resultSet.getString(8);
                Double pricePerNight = resultSet.getDouble(9);
                String facilities = resultSet.getString(10);
                Date date_start = resultSet.getDate(11);
                Date date_end = resultSet.getDate(12);
                smartLockDescriptionBundleList.add(
                        new SmartLockDescriptionBundle.Builder()
                                .id(id)
                                .name(name)
                                .address(address)
                                .beacon(new Beacon(major, minor, uuid))
                                .isMine(true)
                                .isBooked(true)
                                .location(new Location(gps_latitude, gps_longitude))
                                .pricePerNight(pricePerNight)
                                .facilities(facilities)
                                .dateStart(date_start)
                                .dateEnd(date_end)
                                .build());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return smartLockDescriptionBundleList;
    }

    public List<SmartLockDescriptionBundle> getAllDescriptions(User userForToken) {

        Set<SmartLockDescriptionBundle> smartLockDescriptionBundleSet = new HashSet<>(getUserDescriptions(userForToken));

        String query = "SELECT id, address, name, gps_latitude, gps_longitude, price_per_night, facilities FROM houses";

        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);

            ResultSet resultSet = preparedStmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String address = resultSet.getString(2);
                String name = resultSet.getString(3);
                String gps_longitude = resultSet.getString(4);
                String gps_latitude = resultSet.getString(5);
                Double pricePPerNight = resultSet.getDouble(6);
                String facilities = resultSet.getString(7);
                smartLockDescriptionBundleSet.add(new SmartLockDescriptionBundle.Builder()
                        .id(id)
                        .name(name)
                        .address(address)
                        .location(new Location(gps_latitude, gps_longitude))
                        .pricePerNight(pricePPerNight)
                        .facilities(facilities)
                        .build());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(smartLockDescriptionBundleSet);
    }

    public SmartLockDescriptionBundle getDescription(User userForToken, String smartLockId) {
        SmartLockDescriptionBundle.Builder smartLockDescriptionBundleBuilder = new SmartLockDescriptionBundle.Builder();


        String query = "SELECT h.id, h.address, h.name, h.price_per_night, h.facilities, h.gps_latitude, h.gps_longitude, h.beacon_major,h.beacon_major, h.beacon_uuid, uh.date_start, uh.date_end, uh.id_user FROM lighthouse.houses AS h\n" +
                "LEFT JOIN lighthouse.user_house AS uh ON uh.id_house = h.id AND uh.id_user = ?  AND (DATE(uh.date_start) <= DATE(NOW()) AND DATE(uh.date_end) >= DATE(NOW()))\n" +
                "WHERE h.id = ?";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);

            preparedStmt.setString(1, String.valueOf(userForToken.getId()));
            preparedStmt.setString(2, String.valueOf(smartLockId));

            ResultSet resultSet = preparedStmt.executeQuery();

            resultSet.next();

            int id = resultSet.getInt(1);
            String address = resultSet.getString(2);
            String name = resultSet.getString(3);
            Double pricePerNight = resultSet.getDouble(4);
            String facilities = resultSet.getString(5);
            String gpsLatitude = resultSet.getString(6);
            String gpsLongitude = resultSet.getString(7);
            String beaconMajor = resultSet.getString(8);
            String beaconMinor = resultSet.getString(9);
            String beaconUUID = resultSet.getString(10);
            Date startDate = resultSet.getDate(11);
            Date endDate = resultSet.getDate(12);
            int userId = resultSet.getInt(13);

            smartLockDescriptionBundleBuilder
                    .id(id)
                    .address(address)
                    .name(name)
                    .pricePerNight(pricePerNight)
                    .facilities(facilities)
                    .location(new Location(gpsLatitude, gpsLongitude))
                    .isMine(false)
                    .isBooked(false);

            if (userId != NULL_VALUE) {
                smartLockDescriptionBundleBuilder
                        .beacon(new Beacon(beaconMajor, beaconMinor, beaconUUID))
                        .dateStart(startDate)
                        .dateEnd(endDate)
                        .isMine(true)
                        .isBooked(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return smartLockDescriptionBundleBuilder.build();
    }

    public void addBooking(int id_user, int house_id, long mFromDateTime, long mToDateTime) {
        SmartLockDescriptionBundle.Builder smartLockDescriptionBundleBuilder = new SmartLockDescriptionBundle.Builder();

        String query = "INSERT INTO user_house (id_user, id_house, date_start, date_end)"
                + " VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, id_user);
            preparedStmt.setInt(2, house_id);
            preparedStmt.setDate(3, new Date(mFromDateTime));
            preparedStmt.setDate(4, new Date(mToDateTime));

            // execute the preparedstatement
            preparedStmt.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<SmartLockDescriptionBundle> getUserHistoryDescriptions(User userForToken) {
        List<SmartLockDescriptionBundle> smartLockDescriptionBundleList = new ArrayList<>();

        String query = "SELECT h.id, h.address, h.name , h.beacon_major, h.beacon_minor, h.beacon_uuid, h.gps_latitude, h.gps_longitude, h.price_per_night, h.facilities, uh.date_start, uh.date_end " +
                "FROM users AS u " +
                "INNER JOIN user_house AS uh " +
                "   ON u.id = uh.id_user " +
                "INNER JOIN houses AS h " +
                "   ON h.id = uh.id_house " +
                "WHERE u.username LIKE ?";

        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);

            preparedStmt.setString(1, userForToken.getUsername());

            ResultSet resultSet = preparedStmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String address = resultSet.getString(2);
                String name = resultSet.getString(3);
                String major = resultSet.getString(4);
                String minor = resultSet.getString(5);
                String uuid = resultSet.getString(6);
                String gps_longitude = resultSet.getString(7);
                String gps_latitude = resultSet.getString(8);
                Double pricePerNight = resultSet.getDouble(9);
                String facilities = resultSet.getString(10);
                Date date_start = resultSet.getDate(11);
                Date date_end = resultSet.getDate(12);

                SmartLockDescriptionBundle smartLockDescriptionBundle = new SmartLockDescriptionBundle.Builder()
                        .id(id)
                        .name(name)
                        .address(address)
                        .isMine(false)
                        .isBooked(false)
                        .location(new Location(gps_latitude, gps_longitude))
                        .pricePerNight(pricePerNight)
                        .facilities(facilities)
                        .dateStart(date_start)
                        .dateEnd(date_end)
                        .build();

//                 if we now actually have the current house rented.
                SmartLockDescriptionBundle description = getDescription(userForToken, String.valueOf(id));
                if (description.getDate_end() != null) {
                    smartLockDescriptionBundle.setIsMine(true);
                    smartLockDescriptionBundle.setIsBooked(true);

                    smartLockDescriptionBundle.setBeacon(new Beacon(major, minor, uuid));
                }

                  smartLockDescriptionBundleList.add(smartLockDescriptionBundle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return smartLockDescriptionBundleList;
    }
}
