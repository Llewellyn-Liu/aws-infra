package com.lrl.liustationspring.dao.pojo;

import java.sql.Date;
import java.sql.Timestamp;

public class User {

    private Integer id;

    private String firstname;

    private String lastname;
    private String username;

    private String password;

    private Timestamp accountCreated;

    private Timestamp accountLastModified;

    private String token;

    public User() {
    }

    public User(Integer id, String firstname, String lastname, String username, String password, Timestamp accountCreated, Timestamp accountLastModified, String token) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.accountCreated = accountCreated;
        this.accountLastModified = accountLastModified;
        this.token = token;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", accountCreated=" + accountCreated +
                ", accountLastModified=" + accountLastModified +
                ", Token='" + token + '\'' +
                '}';
    }

    public UserForResponse parseFormat(){
        return new UserForResponse(id,firstname,lastname,username,accountCreated,accountLastModified,token);
    }
}
