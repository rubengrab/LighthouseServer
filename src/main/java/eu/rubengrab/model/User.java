package eu.rubengrab.model;

/**
 * Created by Ruben on 11.05.2017.
 */

public class User {
    private String username;
    private String password;
    private String token;

    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
        setToken("");
    }

    public User() {
        setUsername("");
        setPassword("");
        setToken("");
    }

    public User(User user) {
        setUsername(user.getUsername());
        setPassword(user.getPassword());
        setToken(user.getToken());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder()
                .append("[User: ")
                .append("username:").append(username)
                .append(", ")
                .append("toke:").append(token)
                .append("]");
        return stringBuilder.toString();
    }
}
