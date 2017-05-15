package eu.rubengrab.model;

/**
 * Created by Ruben on 11.05.2017.
 */
public class SmartLockSecurityBundle {
    private final String cypherText;
    private final String address;

    public SmartLockSecurityBundle(String cypherText, String address) {
        this.cypherText = cypherText;
        this.address = address;
    }

    public String getCypherText() {
        return cypherText;
    }

    public String getAddress() {
        return address;
    }
}
