package eu.rubengrab.repositories;

import eu.rubengrab.model.User;
import org.springframework.stereotype.Repository;

/**
 * Created by Ruben on 11.05.2017.
 */
@Repository
public class UserRepository {

    public User getByUsername(String username) {
        return new User(username, "123456");
    }

}
