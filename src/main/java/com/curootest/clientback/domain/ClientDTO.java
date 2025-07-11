package com.curootest.clientback.domain;

public class ClientDTO {

    private String id;
    private String name;
    private String surname;
    private String idType;
    private String idNumber;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getIdType() {
        return idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}
