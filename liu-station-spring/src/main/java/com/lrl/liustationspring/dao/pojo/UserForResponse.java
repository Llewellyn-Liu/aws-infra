package com.lrl.liustationspring.dao.pojo;

import java.sql.Timestamp;

public class UserForResponse {



    private Integer id;

    private String firstname;

    private String lastname;
    private String username;
    private Timestamp accountCreated;

    private Timestamp accountLastModified;

    private String Token;

    public UserForResponse() {
    }

    public UserForResponse(Integer id, String firstname, String lastname, String username, Timestamp accountCreated, Timestamp accountLastModified, String token) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.accountCreated = accountCreated;
        this.accountLastModified = accountLastModified;
        Token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getAccountCreated() {
        return accountCreated;
    }

    public void setAccountCreated(Timestamp accountCreated) {
        this.accountCreated = accountCreated;
    }

    public Timestamp getAccountLastModified() {
        return accountLastModified;
    }

    public void setAccountLastModified(Timestamp accountLastModified) {
        this.accountLastModified = accountLastModified;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", accountCreated=" + accountCreated +
                ", accountLastModified=" + accountLastModified +
                ", Token='" + Token + '\'' +
                '}';
    }
}
