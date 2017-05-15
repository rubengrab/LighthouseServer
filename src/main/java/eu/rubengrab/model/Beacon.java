package eu.rubengrab.model;

/**
 * Created by Ruben on 15.05.2017.
 */
public class Beacon {
    String major;
    String minor;
    String uuid;

    public Beacon(String major, String minor, String uuid) {
        this.major = major;
        this.minor = minor;
        this.uuid = uuid;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
