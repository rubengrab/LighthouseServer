package eu.rubengrab.services;

import eu.rubengrab.model.User;
import eu.rubengrab.repositories.BookingRepository;
import eu.rubengrab.repositories.SmartLockRepository;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Ruben on 20.05.2017.
 */
@Service
public class BookingService {

    @Autowired
    private SmartLockRepository smartLockRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public synchronized Boolean bookNow(User userForToken, int smartLockDescriptionBundleId, long mFromDateTime, long mToDateTime) {
        if (checkAvailability(userForToken, smartLockDescriptionBundleId, mFromDateTime, mToDateTime)) {
            smartLockRepository.addBooking(userForToken.getId(), smartLockDescriptionBundleId, mFromDateTime, mToDateTime);
            return true;
        } else {
            return false;
        }
    }

    public SmartLockRepository getSmartLockRepository() {
        return smartLockRepository;
    }

    public void setSmartLockRepository(SmartLockRepository smartLockRepository) {
        this.smartLockRepository = smartLockRepository;
    }

    public Boolean checkAvailability(User userForToken, int smartLockDescriptionBundleId, long mFromDateTime, long mToDateTime) {
        List<Interval> bookingDatesForHouse = bookingRepository.getBookingDatesForHouse(smartLockDescriptionBundleId);
        for (Interval interval : bookingDatesForHouse) {
            if (interval.overlaps(new Interval(mFromDateTime, mToDateTime))) {
                return false;
            }
        }
        return true;
    }
}
