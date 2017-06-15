package eu.rubengrab.controllers;

import eu.rubengrab.model.SmartLockDescriptionBundle;
import eu.rubengrab.model.SmartLockSecurityBundle;
import eu.rubengrab.model.User;
import eu.rubengrab.services.SessionService;
import eu.rubengrab.services.SmartLockService;
import eu.rubengrab.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Ruben on 11.05.2017.
 */
@RestController
@RequestMapping("/{userTokenUSERNAME}")
public class SmartLockController {

    private final SmartLockService smartLockService;
    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public SmartLockController(SmartLockService smartLockService, SessionService sessionService, UserService userService) {
        this.smartLockService = smartLockService;
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/smartLock/security/{major}/{minor}/{uuid}")
    SmartLockSecurityBundle getSmartLockSecurityBundle(@PathVariable String userTokenUSERNAME, @PathVariable Long major, @PathVariable Long minor, @PathVariable String uuid) {
        System.out.println("/smartLock/security/{major}/{minor}/{uuid}");
        User userForToken = getUserService().getUserByUsername(userTokenUSERNAME);
        return getSmartLockService().getEncryptedUnlockKey(userForToken, major, minor, uuid);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/smartLock/description/{smartLockId}")
    SmartLockDescriptionBundle getSmartLockDescriptionBundle(@PathVariable String userTokenUSERNAME, @PathVariable String smartLockId) {
        System.out.println("/smartLock/description/{smartLockId}");
        User userForToken = getUserService().getUserByUsername(userTokenUSERNAME);
        return getSmartLockService().getSmartLockDescription(userForToken, smartLockId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/smartLock/description")
    List<SmartLockDescriptionBundle> getSmartLockDescriptionBundles(@PathVariable String userTokenUSERNAME) {
        System.out.println("/smartLock/description");
        User userForToken = getUserService().getUserByUsername(userTokenUSERNAME);
        return getSmartLockService().getAllSmartLockDescriptions(userForToken);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/smartLock/description/history")
    List<SmartLockDescriptionBundle> getPastBookingByUser(@PathVariable String userTokenUSERNAME) {
        System.out.println("/smartLock/description/history");
        User userForToken = getUserService().getUserByUsername(userTokenUSERNAME);
        return getSmartLockService().getPastBookingHistory(userForToken);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/smartLock/description/active")
    List<SmartLockDescriptionBundle> getActiveBookingByUser(@PathVariable String userTokenUSERNAME) {
        System.out.println("/smartLock/description/active");
        User userForToken = getUserService().getUserByUsername(userTokenUSERNAME);
        return getSmartLockService().getActiveBookingByUser(userForToken);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/smartLock/description/future")
    List<SmartLockDescriptionBundle> getFutureBookingByUser(@PathVariable String userTokenUSERNAME) {
        System.out.println("/smartLock/description/future");
        User userForToken = getUserService().getUserByUsername(userTokenUSERNAME);
        return getSmartLockService().getFutureBookingByUser(userForToken);
    }


    private UserService getUserService() {
        return userService;
    }

    private SessionService getSessionService() {
        return sessionService;
    }

    private SmartLockService getSmartLockService() {
        return smartLockService;
    }

}
