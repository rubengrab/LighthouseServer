package eu.rubengrab.controllers;

import eu.rubengrab.model.User;
import eu.rubengrab.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ruben on 11.05.2017.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<User> login(@RequestBody User user) {

        User loggedInUser = getUserService().login(user);

        if (loggedInUser == null) {
            return new ResponseEntity<>(new User(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<Boolean> logout(@RequestBody User user) {
        Boolean successfulLogout = getUserService().logout(user);

        if (successfulLogout) {
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        }
    }

    private UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
