package eu.rubengrab.controllers;

import eu.rubengrab.model.User;
import eu.rubengrab.services.BookingService;
import eu.rubengrab.services.SessionService;
import eu.rubengrab.services.SmartLockService;
import eu.rubengrab.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

/**
 * Created by Ruben on 19.05.2017.
 */
@RestController
@RequestMapping("/{userTokenUSERNAME}")
public class BookingController {

    @Autowired
    private SmartLockService smartLockService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;

    @RequestMapping(method = RequestMethod.GET, value = "/checkBooking/{smartLockDescriptionBundleId}/{mFromDateTime}/{mToDateTime}")
    Boolean checkAvailability(@PathVariable String userTokenUSERNAME, @PathVariable int smartLockDescriptionBundleId, @PathVariable long mFromDateTime, @PathVariable long mToDateTime) {
        System.out.print(new Date(mFromDateTime) +" to " + new Date(mToDateTime) + " availability requested: ");
        User userForToken = getUserService().getUserByUsername(userTokenUSERNAME);
        Boolean isAvailable = getBookingService().checkAvailability(userForToken, smartLockDescriptionBundleId, mFromDateTime, mToDateTime);
        System.out.println(isAvailable);
        return isAvailable;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/bookNow/{smartLockDescriptionBundleId}/{mFromDateTime}/{mToDateTime}")
    Boolean bookNow(@PathVariable String userTokenUSERNAME, @PathVariable int smartLockDescriptionBundleId, @PathVariable long mFromDateTime, @PathVariable long mToDateTime) {

        User userForToken = getUserService().getUserByUsername(userTokenUSERNAME);
        return getBookingService().bookNow(userForToken, smartLockDescriptionBundleId, mFromDateTime, mToDateTime);
    }


    public BookingService getBookingService() {
        return bookingService;
    }

    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public SmartLockService getSmartLockService() {
        return smartLockService;
    }

    public void setSmartLockService(SmartLockService smartLockService) {
        this.smartLockService = smartLockService;
    }

    public SessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
