package com.king.converter.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "matches")
public class MatchBean {

    @Id(autoincrement = true)
    private Long id;

    private int week;
    private int month;
    private String level;
    private String court;
    private String region;
    private String country;
    private String city;

    @Generated(hash = 988656448)
    public MatchBean(Long id, int week, int month, String level, String court,
            String region, String country, String city) {
        this.id = id;
        this.week = week;
        this.month = month;
        this.level = level;
        this.court = court;
        this.region = region;
        this.country = country;
        this.city = city;
    }

    @Generated(hash = 1910626172)
    public MatchBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
