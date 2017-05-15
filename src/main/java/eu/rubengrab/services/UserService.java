package eu.rubengrab.services;

import eu.rubengrab.model.User;
import eu.rubengrab.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Ruben on 11.05.2017.
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionService sessionService;

    public User login(User user) {
        User userByUsername = userRepository.getByUsername(user.getUsername());
        if (userByUsername != null && userByUsername.getPassword().equals(user.getPassword())) {
            String generatedToken = sessionService.generateTokenForUser(user);
            user.setToken(generatedToken);
            return user;
        } else {
            return null;
        }
    }

    public Boolean logout(User user) {
        String token = user.getToken();
        if (token == null) {
            return Boolean.FALSE;
        }
        User userForToken = sessionService.getUserForToken(token);
        if (user.getUsername().equals(userForToken.getUsername())) {
            sessionService.removeTokenForUser(user);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
