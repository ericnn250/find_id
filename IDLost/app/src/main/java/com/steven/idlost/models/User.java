package com.steven.idlost.models;

public class User {
    private String id;
    private String national_id;
    private String email;
    private String names;
    private String phone;
    private String dob;
    private  String sector;
    private String district;
    private String security_level;
    private String desc;

    public User() {
    }

    public User(String id, String nationalId,String desc, String email, String names, String sector, String district,String phone,String dob,String security_level) {
        this.id = id;
        this.national_id = nationalId;
        this.email = email;
        this.names = names;
        this.sector = sector;
        this.district = district;
        this.phone=phone;
        this.dob=dob;
        this.security_level=security_level;
        this.desc=desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNational_id() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSecurity_level() {
        return security_level;
    }

    public void setSecurity_level(String security_level) {
        this.security_level = security_level;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", national_id='" + national_id + '\'' +
                ", email='" + email + '\'' +
                ", names='" + names + '\'' +
                ", phone='" + phone + '\'' +
                ", dob='" + dob + '\'' +
                ", sector='" + sector + '\'' +
                ", district='" + district + '\'' +
                ", security_level='" + security_level + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
