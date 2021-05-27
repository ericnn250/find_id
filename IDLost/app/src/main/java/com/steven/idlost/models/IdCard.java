package com.steven.idlost.models;

public class IdCard {
    private String id;
    private String id_number;
    private String user_id;
    private String names;
    private String isue_district;
    private String isue_sector;
    private String foundsector;
    private String type;
    private String status;
    private String dob;

    public IdCard() {
    }

    public IdCard(String id, String id_number,String user_id, String names,String dob ,String foundsector,String isue_district, String isue_sector, String type, String status) {
        this.id = id;
        this.id_number = id_number;
        this.user_id=user_id;
        this.names=names;
        this.isue_district = isue_district;
        this.isue_sector = isue_sector;
        this.type = type;
        this.status = status;
        this.dob=dob;
        this.foundsector=foundsector;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDob() {
        return dob;
    }

    public String getFoundsector() {
        return foundsector;
    }

    public void setFoundsector(String foundsector) {
        this.foundsector = foundsector;
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

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }


    public String getIsue_district() {
        return isue_district;
    }

    public void setIsue_district(String isue_district) {
        this.isue_district = isue_district;
    }

    public String getIsue_sector() {
        return isue_sector;
    }

    public void setIsue_sector(String isue_sector) {
        this.isue_sector = isue_sector;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "IdCard{" +
                "id='" + id + '\'' +
                ", id_number='" + id_number + '\'' +
                ", user_id='" + user_id + '\'' +
                ", names='" + names + '\'' +
                ", isue_district='" + isue_district + '\'' +
                ", isue_sector='" + isue_sector + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
