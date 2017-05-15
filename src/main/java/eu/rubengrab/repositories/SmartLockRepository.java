package eu.rubengrab.repositories;

import eu.rubengrab.model.SmartLockHouseDescription;
import org.springframework.stereotype.Repository;

import java.util.Calendar;

/**
 * Created by Ruben on 11.05.2017.
 */
@Repository
public class SmartLockRepository {

    private static final String MOCK_ADDRESS = "34:45:53:73:45:23:85:01";
    private static final String MOCK_UNLOCK_CODE = "secretCode";

    public SmartLockRepository() {
    }

    public String getUnlockCode(Long major, Long minor, String uuid) {
        String pepper = String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
        return MOCK_UNLOCK_CODE + pepper;
    }

    public String getAddress(Long major, Long minor, String uuid) {
        return MOCK_ADDRESS;
    }

    public SmartLockHouseDescription getDescription(Long major, Long minor, String uuid) {
        return new SmartLockHouseDescription();
    }
}
