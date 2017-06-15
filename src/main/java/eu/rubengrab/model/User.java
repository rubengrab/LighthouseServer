package eu.rubengrab.model;

/**
 * Created by Ruben on 11.05.2017.
 */
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String creditCard;
    private String username;
    private String password;
    private String token;

    public User(int id, String username, String password) {
        this(username, password);
        setId(id);
    }

    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
        setToken("");
        setId(-1);
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

    public User(int id, String username, String password, String firstName, String lastName, String email, String creditCard) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.creditCard = creditCard;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
