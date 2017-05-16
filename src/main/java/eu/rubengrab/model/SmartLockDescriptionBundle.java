package eu.rubengrab.model;

/**
 * Created by Ruben on 12.05.2017.
 */
public class SmartLockDescriptionBundle {
    private int id;
    private String name;
    private String address;
    private Beacon beacon;
    private Boolean isMine;
    private Boolean isBooked;


    private SmartLockDescriptionBundle(int id, String name, String address, Beacon beacon, Boolean isMine, Boolean isBooked) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.beacon = beacon;
        this.isMine = isMine;
        this.isBooked = isBooked;
    }

    public static class Builder {
        private int id;
        private String name;
        private String address;
        private Beacon beacon;
        private Boolean isMine;
        private Boolean isBooked;

        public Builder() {
        }

        public Builder id(final int id) {
            this.id = id;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder address(final String address) {
            this.address = address;
            return this;
        }

        public Builder beacon(final Beacon beacon) {
            this.beacon = beacon;
            return this;
        }

        public Builder isMine(final boolean isMine) {
            this.isMine = isMine;
            return this;
        }

        public Builder isBooked(final boolean isBooked) {
            this.isBooked = isBooked;
            return this;
        }

        public SmartLockDescriptionBundle build() {
            return new SmartLockDescriptionBundle(id, name, address, beacon, isMine, isBooked);
        }
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SmartLockDescriptionBundle)) {
            return false;
        }
        SmartLockDescriptionBundle other = (SmartLockDescriptionBundle) obj;
        return other.getId() == this.getId();
    }

    public Boolean getMine() {
        return isMine;
    }

    public void setMine(Boolean mine) {
        isMine = mine;
    }

    public Boolean getBooked() {
        return isBooked;
    }

    public void setBooked(Boolean booked) {
        isBooked = booked;
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