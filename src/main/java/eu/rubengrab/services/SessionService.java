package eu.rubengrab.services;

import eu.rubengrab.model.User;
import eu.rubengrab.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ruben on 11.05.2017.
 */
@Service
public class SessionService {

    @Autowired
    UserRepository userRepository;

    Map<String, String> usernameTokenMap;

    public SessionService() {
        this.usernameTokenMap = new HashMap<>();
    }

    public String generateTokenForUser(User user) {
        String generatedToken = generateRandomToken();
        usernameTokenMap.put(user.getUsername(), generatedToken);
        return generatedToken;
    }

    private String generateRandomToken() {
        return "mockToken";
    }

    public User getUserForToken(String userToken) {
        String username = null;
        for (String key : usernameTokenMap.keySet()) {
            if (usernameTokenMap.get(key).equals(userToken)) {
                username = key;
            }
        }
        if (username == null) {
            return null;
        } else {
            return userRepository.getByUsername(username);
        }
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void removeTokenForUser(User user) {
        usernameTokenMap.remove(user.getUsername());
    }
}
