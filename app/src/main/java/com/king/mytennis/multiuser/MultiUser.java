package com.king.mytennis.multiuser;

public class MultiUser {

    private String id;
    private String displayName;
    private String country;
    private String birthday;
    private String height;
    private String weight;
    private String fullName;
    private int flagImageResId;

    public MultiUser() {

    }

    public MultiUser(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getFlagImageResId() {
        return flagImageResId;
    }

    public void setFlagImageResId(int flagImageResId) {
        this.flagImageResId = flagImageResId;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof MultiUser) {
                MultiUser user = (MultiUser) o;
                return user.getId().equals(id);
            }
        }
        return false;
    }

}
