package eu.rubengrab.repositories;

import eu.rubengrab.model.Beacon;
import eu.rubengrab.model.LocationPOJO;
import eu.rubengrab.model.SmartLockDescriptionBundle;
import eu.rubengrab.model.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ruben on 11.05.2017.
 */
@Repository
public class SmartLockRepository {

    private static final int NULL_VALUE = 0;

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

    public SmartLockRepository() {
    }

    public String getUnlockCode(User user, Long major, Long minor, String uuid) {
        createConnection();

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
        closeConnection();
        return "" + pepper;
    }

    public String getMacAddress(User user, Long major, Long minor, String uuid) {
        createConnection();

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
        closeConnection();

        return "";
    }

    public List<String> getImagesForHouse(int houseId) {
        createConnection();

        List<String> images = new ArrayList<>();

        String query = "Select image_name from images " +
                "inner join image_house on images.id = image_house.id_image " +
                "where image_house.id_house = ?";

        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);

            preparedStmt.setInt(1, houseId);

            ResultSet resultSet = preparedStmt.executeQuery();

            while (resultSet.next()) {
                String imageName = resultSet.getString(1);

                images.add(imageName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();

        return images;
    }

    public List<Integer> getBookedHousesId() {
        createConnection();

        List<Integer> ids = new ArrayList<>();


        String query = "SELECT uh.id_house FROM user_house AS uh WHERE (DATE(uh.date_start) <= DATE(NOW()) AND DATE(uh.date_end) >= DATE(NOW()))";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);

            ResultSet resultSet = preparedStmt.executeQuery();

            resultSet.next();

            int id = resultSet.getInt(1);
            ids.add(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();

        return ids;
    }

    public List<SmartLockDescriptionBundle> getAllDescriptions(User userForToken) {
        createConnection();

        Set<SmartLockDescriptionBundle> smartLockDescriptionBundleSet = new HashSet<>(getActiveBookingsByUser(userForToken));

        String query = "SELECT id, address, name, gps_latitude, gps_longitude, price_per_night, facilities, description FROM houses";

        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);

            ResultSet resultSet = preparedStmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String address = resultSet.getString(2);
                String name = resultSet.getString(3);
                String gps_latitude = resultSet.getString(4);
                String gps_longitude = resultSet.getString(5);
                Double pricePPerNight = resultSet.getDouble(6);
                String facilities = resultSet.getString(7);
                String description = resultSet.getString(8);
                smartLockDescriptionBundleSet.add(new SmartLockDescriptionBundle.Builder()
                        .id(id)
                        .name(name)
                        .address(address)
                        .location(new LocationPOJO(gps_latitude, gps_longitude))
                        .pricePerNight(pricePPerNight)
                        .facilities(facilities)
                        .description(description)
                        .imageNameList(getImagesForHouse(id))
                        .build());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();

        return new ArrayList<>(smartLockDescriptionBundleSet);
    }

    public SmartLockDescriptionBundle getDescription(User userForToken, String smartLockId) {
        createConnection();

        SmartLockDescriptionBundle.Builder smartLockDescriptionBundleBuilder = new SmartLockDescriptionBundle.Builder();


        String query = "SELECT h.id, h.address, h.name, h.price_per_night, h.facilities, h.gps_latitude, h.gps_longitude, h.beacon_major,h.beacon_major, h.beacon_uuid, uh.date_start, uh.date_end, uh.id_user, h.description FROM lighthouse.houses AS h\n" +
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
            String description = resultSet.getString(14);

            smartLockDescriptionBundleBuilder
                    .id(id)
                    .address(address)
                    .name(name)
                    .pricePerNight(pricePerNight)
                    .facilities(facilities)
                    .description(description)
                    .location(new LocationPOJO(gpsLatitude, gpsLongitude))
                    .isMine(false)
                    .isBooked(false)
                    .imageNameList(getImagesForHouse(id));

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
        closeConnection();

        return smartLockDescriptionBundleBuilder.build();
    }

    public void addBooking(int id_user, int house_id, long mFromDateTime, long mToDateTime) {
        createConnection();

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
        closeConnection();

    }

    public List<SmartLockDescriptionBundle> getPastBookingByUser(User userForToken) {
        createConnection();

        List<SmartLockDescriptionBundle> smartLockDescriptionBundleList = new ArrayList<>();

        List<Integer> activeBookingIds = getActiveBookingsByUser(userForToken).stream().map(SmartLockDescriptionBundle::getId).collect(Collectors.toList());

        String query = "SELECT h.id, h.address, h.name , h.beacon_major, h.beacon_minor, h.beacon_uuid, h.gps_latitude, h.gps_longitude, h.price_per_night, h.facilities, uh.date_start, uh.date_end, h.description " +
                "FROM users AS u " +
                "INNER JOIN user_house AS uh " +
                "   ON u.id = uh.id_user " +
                "INNER JOIN houses AS h " +
                "   ON h.id = uh.id_house " +
                "WHERE u.username LIKE ? AND DATE(uh.date_end) < DATE(NOW())";

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
                String gps_longitude = resultSet.getString(8);
                String gps_latitude = resultSet.getString(7);
                Double pricePerNight = resultSet.getDouble(9);
                String facilities = resultSet.getString(10);
                Date date_start = resultSet.getDate(11);
                Date date_end = resultSet.getDate(12);
                String description = resultSet.getString(13);

                SmartLockDescriptionBundle smartLockDescriptionBundle = new SmartLockDescriptionBundle.Builder()
                        .id(id)
                        .name(name)
                        .address(address)
                        .isMine(false)
                        .isBooked(false)
                        .location(new LocationPOJO(gps_latitude, gps_longitude))
                        .pricePerNight(pricePerNight)
                        .facilities(facilities)
                        .dateStart(date_start)
                        .dateEnd(date_end)
                        .description(description)
                        .imageNameList(getImagesForHouse(id))
                        .build();

                if (activeBookingIds.contains(id)) {
                    smartLockDescriptionBundle.setBeacon(new Beacon(major, minor, uuid));
                    smartLockDescriptionBundle.setIsBooked(true);
                    smartLockDescriptionBundle.setIsMine(true);
                }
                smartLockDescriptionBundleList.add(smartLockDescriptionBundle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();

        return smartLockDescriptionBundleList;
    }

    public List<SmartLockDescriptionBundle> getActiveBookingsByUser(User userForToken) {
        createConnection();

        List<SmartLockDescriptionBundle> smartLockDescriptionBundleList = new ArrayList<>();

        String query = "SELECT h.id, h.address, h.name , h.beacon_major, h.beacon_minor, h.beacon_uuid, h.gps_latitude, h.gps_longitude, h.price_per_night, h.facilities, uh.date_start, uh.date_end, h.description " +
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
                String gps_longitude = resultSet.getString(8);
                String gps_latitude = resultSet.getString(7);
                Double pricePerNight = resultSet.getDouble(9);
                String facilities = resultSet.getString(10);
                Date date_start = resultSet.getDate(11);
                Date date_end = resultSet.getDate(12);
                String description = resultSet.getString(13);
                smartLockDescriptionBundleList.add(
                        new SmartLockDescriptionBundle.Builder()
                                .id(id)
                                .name(name)
                                .address(address)
                                .beacon(new Beacon(major, minor, uuid))
                                .isMine(true)
                                .isBooked(true)
                                .location(new LocationPOJO(gps_latitude, gps_longitude))
                                .pricePerNight(pricePerNight)
                                .facilities(facilities)
                                .dateStart(date_start)
                                .dateEnd(date_end)
                                .description(description)
                                .imageNameList(getImagesForHouse(id))
                                .build());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();

        return smartLockDescriptionBundleList;
    }

    public List<SmartLockDescriptionBundle> getFutureBookingByUser(User userForToken) {
        createConnection();

        List<SmartLockDescriptionBundle> smartLockDescriptionBundleList = new ArrayList<>();

        List<Integer> activeBookingIds = getActiveBookingsByUser(userForToken).stream().map(SmartLockDescriptionBundle::getId).collect(Collectors.toList());

        String query = "SELECT h.id, h.address, h.name , h.beacon_major, h.beacon_minor, h.beacon_uuid, h.gps_latitude, h.gps_longitude, h.price_per_night, h.facilities, uh.date_start, uh.date_end, h.description " +
                "FROM users AS u " +
                "INNER JOIN user_house AS uh " +
                "   ON u.id = uh.id_user " +
                "INNER JOIN houses AS h " +
                "   ON h.id = uh.id_house " +
                "WHERE u.username LIKE ? AND DATE(uh.date_start) > DATE(NOW())";

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
                String gps_longitude = resultSet.getString(8);
                String gps_latitude = resultSet.getString(7);
                Double pricePerNight = resultSet.getDouble(9);
                String facilities = resultSet.getString(10);
                Date date_start = resultSet.getDate(11);
                Date date_end = resultSet.getDate(12);
                String description = resultSet.getString(13);

                SmartLockDescriptionBundle smartLockDescriptionBundle = new SmartLockDescriptionBundle.Builder()
                        .id(id)
                        .name(name)
                        .address(address)
                        .isMine(false)
                        .isBooked(false)
                        .location(new LocationPOJO(gps_latitude, gps_longitude))
                        .pricePerNight(pricePerNight)
                        .facilities(facilities)
                        .dateStart(date_start)
                        .dateEnd(date_end)
                        .description(description)
                        .imageNameList(getImagesForHouse(id))
                        .build();

                if (activeBookingIds.contains(id)) {
                    smartLockDescriptionBundle.setBeacon(new Beacon(major, minor, uuid));
                    smartLockDescriptionBundle.setIsBooked(true);
                    smartLockDescriptionBundle.setIsMine(true);
                }

                smartLockDescriptionBundleList.add(smartLockDescriptionBundle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();

        return smartLockDescriptionBundleList;
    }
}
