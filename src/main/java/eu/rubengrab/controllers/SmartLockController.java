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

    @Autowired
    private SmartLockService smartLockService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/smartLock/security/{major}/{minor}/{uuid}")
    SmartLockSecurityBundle getSmartLockSecurityBundle(@PathVariable String userTokenUSERNAME, @PathVariable Long major, @PathVariable Long minor, @PathVariable String uuid) {
//        User userForToken = getSessionService().getUserForToken(userTokenUSERNAME);
        User userForToken = getUserService().getUserByUsername(userTokenUSERNAME);
        return getSmartLockService().getEncryptedUnlockKey(userForToken, major, minor, uuid);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/smartLock/description/{major}/{minor}/{uuid}")
    SmartLockDescriptionBundle getSmartLockDescriptionBundle(@PathVariable String userToken, @PathVariable Long major, @PathVariable Long minor, @PathVariable String uuid) {
        return getSmartLockService().getSmartLockDescription(major, minor, uuid);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/smartLock/description")//gets all
    List<SmartLockDescriptionBundle> getSmartLockDescriptionBundles(@PathVariable String userTokenUSERNAME) {
        User userForToken = getUserService().getUserByUsername(userTokenUSERNAME);
        return getSmartLockService().getAllSmartLockDescriptions(userForToken);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/smartLock/userDescription") //gets user's
    List<SmartLockDescriptionBundle> getSmartLockDescriptionBundlesByUser(@PathVariable String userTokenUSERNAME) {
        User userForToken = getUserService().getUserByUsername(userTokenUSERNAME);
        return getSmartLockService().getUserSmartLockDescriptions(userForToken);
    }











    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private SessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    private SmartLockService getSmartLockService() {
        return smartLockService;
    }

    public void setSmartLockService(SmartLockService smartLockService) {
        this.smartLockService = smartLockService;
    }
}
