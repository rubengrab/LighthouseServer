package eu.rubengrab.repositories;

import eu.rubengrab.model.Beacon;
import eu.rubengrab.model.SmartLockDescriptionBundle;
import eu.rubengrab.model.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ruben on 11.05.2017.
 */
@Repository
public class SmartLockRepository {

    private static final String MOCK_ADDRESS = "34:45:53:73:45:23:85:01";
    private static final String MOCK_UNLOCK_CODE = "secretCode";

    public static final int DEFAULT_ID_VALUE = -1;

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
        return MOCK_UNLOCK_CODE + pepper;
    }

    public String getAddress(Long major, Long minor, String uuid) {
        return MOCK_ADDRESS;
    }

    public SmartLockDescriptionBundle getDescription(Long major, Long minor, String uuid) {
        SmartLockDescriptionBundle smartLockDescriptionBundle = new SmartLockDescriptionBundle();

        String query = "SELECT id, address, name FROM houses WHERE beacon_major LIKE ? AND beacon_minor LIKE ? AND beacon_uuid LIKE ?;";

        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);

            preparedStmt.setString(1, String.valueOf(major));
            preparedStmt.setString(2, String.valueOf(minor));
            preparedStmt.setString(3, uuid);

            ResultSet resultSet = preparedStmt.executeQuery();

            resultSet.next();

            int id = resultSet.getInt(1);
            String address = resultSet.getString(2);
            String name = resultSet.getString(3);
            return new SmartLockDescriptionBundle(id, name, address);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return smartLockDescriptionBundle;
    }

    public List<SmartLockDescriptionBundle> getDescriptions(User userForToken) {
        List<SmartLockDescriptionBundle> smartLockDescriptionBundleList = new ArrayList<>();

        String query = "SELECT h.id, h.address, h.name , h.beacon_major, h.beaocn_minor, h.beaocn_uuid " +
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
                smartLockDescriptionBundleList.add(new SmartLockDescriptionBundle(id, name, address, new Beacon(major, minor, uuid)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return smartLockDescriptionBundleList;
    }

    public List<SmartLockDescriptionBundle> getDescriptions() {
        List<SmartLockDescriptionBundle> smartLockDescriptionBundleList = new ArrayList<>();

        String query = "SELECT id, address, name FROM houses";

        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);

            ResultSet resultSet = preparedStmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String address = resultSet.getString(2);
                String name = resultSet.getString(3);
                smartLockDescriptionBundleList.add(new SmartLockDescriptionBundle(id, name, address));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return smartLockDescriptionBundleList;
    }
}
