package com.curootest.clientback.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    private String name;

    private String surname;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    private String email;

    @Column(name = "password")
    private String passwordHash;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "modification_date")
    private LocalDateTime modificationDate;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.modificationDate = LocalDateTime.now();
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
        this.modificationDate = LocalDateTime.now();
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
        this.modificationDate = LocalDateTime.now();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.modificationDate = LocalDateTime.now();
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        this.modificationDate = LocalDateTime.now();
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate() {
        this.modificationDate = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "userId")
    private List<Client> clients;
}
