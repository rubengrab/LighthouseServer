package eu.rubengrab.model;


import java.sql.Date;
import java.util.List;

/**
 * Created by Ruben on 12.05.2017.
 */
public class SmartLockDescriptionBundle {
    private int id;
    private String name;
    private String address;
    private Beacon beacon;
    private boolean isMine;
    private boolean isBooked;
    private LocationPOJO location;
    private Double pricePerNight;
    private String facilities;
    private String description;
    private Date date_start;
    private Date date_end;
    private List<String> imageNameList;


    private SmartLockDescriptionBundle(int id, String name, String address, Beacon beacon, boolean isMine, boolean isBooked, LocationPOJO location, Double pricePerNight, String facilities, String description, Date date_start, Date date_end, List<String> imageNameList) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.beacon = beacon;
        this.isMine = isMine;
        this.isBooked = isBooked;
        this.location = location;
        this.pricePerNight = pricePerNight;
        this.facilities = facilities;
        this.description = description;
        this.date_start = date_start;
        this.date_end = date_end;
        this.imageNameList = imageNameList;
    }

    public static class Builder {
        private int id;
        private String name;
        private String address;
        private Beacon beacon;
        private boolean isMine;
        private boolean isBooked;
        private LocationPOJO location;
        private Double pricePerNight;
        private String facilities;
        private String description;
        private Date date_start;
        private Date date_end;
        private List<String> imageNameList;

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

        public Builder location(final LocationPOJO location) {
            this.location = location;
            return this;
        }

        public Builder pricePerNight(final Double pricePerNight) {
            this.pricePerNight = pricePerNight;
            return this;
        }

        public Builder facilities(final String facilities) {
            this.facilities = facilities;
            return this;
        }

        public Builder dateStart(final Date date_start) {
            this.date_start = date_start;
            return this;
        }

        public Builder dateEnd(final Date date_end) {
            this.date_end = date_end;
            return this;
        }

        public Builder description(final String description) {
            this.description = description;
            return this;
        }

        public Builder imageNameList(final List<String> imageNameList){
            this.imageNameList = imageNameList;
            return this;
        }


        public SmartLockDescriptionBundle build() {
            return new SmartLockDescriptionBundle(id, name, address, beacon, isMine, isBooked, location, pricePerNight, facilities, description, date_start, date_end, imageNameList);
        }
    }

    public List<String> getImageNameList() {
        return imageNameList;
    }

    public void setImageNameList(List<String> imageNameList) {
        this.imageNameList = imageNameList;
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

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate_start() {
        return date_start;
    }

    public void setDate_start(Date date_start) {
        this.date_start = date_start;
    }

    public Date getDate_end() {
        return date_end;
    }

    public void setDate_end(Date date_end) {
        this.date_end = date_end;
    }

    public LocationPOJO getLocation() {
        return location;
    }

    public void setLocation(LocationPOJO location) {
        this.location = location;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public boolean getIsMine() {
        return isMine;
    }

    public void setIsMine(boolean mine) {
        isMine = mine;
    }

    public boolean getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(boolean booked) {
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