package eu.rubengrab.controllers;

import eu.rubengrab.model.SmartLockBundle;
import eu.rubengrab.model.SmartLockHouseDescription;
import eu.rubengrab.model.User;
import eu.rubengrab.services.SessionService;
import eu.rubengrab.services.SmartLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ruben on 11.05.2017.
 */
@RestController
@RequestMapping("/{userToken}")
public class SmartLockController {

    @Autowired
    private SmartLockService smartLockService;
    @Autowired
    private SessionService sessionService;

    @RequestMapping(method = RequestMethod.GET, value = "/smartlocks/{major}/{minor}/{uuid}")
    SmartLockBundle getSmartLockBundle(@PathVariable String userToken, @PathVariable Long major, @PathVariable Long minor, @PathVariable String uuid) {
        User userForToken = getSessionService().getUserForToken(userToken);
        return getSmartLockService().getEncryptedUnlockKey(userForToken,major, minor, uuid);
    }

    @RequestMapping(method = RequestMethod.GET, value = "smartlocksHouse/{major}/{minor}/{uuid}")
    SmartLockHouseDescription getSmartLockHouseDescription(@PathVariable String userToken, @PathVariable Long major, @PathVariable Long minor, @PathVariable String uuid) {
        return getSmartLockService().getSmartLockDescription(major, minor, uuid);
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
