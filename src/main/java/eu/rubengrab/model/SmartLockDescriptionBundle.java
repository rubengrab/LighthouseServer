package eu.rubengrab.model;

/**
 * Created by Ruben on 12.05.2017.
 */

public class SmartLockDescriptionBundle {
    int id;
    String name;
    String address;
    Beacon beacon;

    public SmartLockDescriptionBundle() {
    }

    public SmartLockDescriptionBundle(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public SmartLockDescriptionBundle(int id, String name, String address, Beacon beacon) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.beacon = beacon;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
