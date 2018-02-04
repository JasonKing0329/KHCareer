package com.king.converter.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/1/26 15:34
 */
@Entity(nameInDb = "users")
public class User {

    @Id(autoincrement = true)
    private Long id;

    private String nameEng;
    private String nameChn;
    private String namePinyin;
    private String nameShort;
    private String country;
    private String city;
    private String birthday;
    @Generated(hash = 1840821913)
    public User(Long id, String nameEng, String nameChn, String namePinyin,
            String nameShort, String country, String city, String birthday) {
        this.id = id;
        this.nameEng = nameEng;
        this.nameChn = nameChn;
        this.namePinyin = namePinyin;
        this.nameShort = nameShort;
        this.country = country;
        this.city = city;
        this.birthday = birthday;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNameEng() {
        return this.nameEng;
    }
    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }
    public String getNameChn() {
        return this.nameChn;
    }
    public void setNameChn(String nameChn) {
        this.nameChn = nameChn;
    }
    public String getNamePinyin() {
        return this.namePinyin;
    }
    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }
    public String getNameShort() {
        return this.nameShort;
    }
    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }
    public String getCountry() {
        return this.country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getBirthday() {
        return this.birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

}
