package com.lrl.liustationspring.dao.pojo.userREST;

public class UserREST {

    private IdREST id;

    private UsernameREST username;

    private FirstNameREST firstname;

    private LastNameREST lastname;

    private PasswordREST password;

    private AccountCreatedREST accountCreated;

    private AccountLastModifiedREST accountLastModified;

    private TokenREST token;

    public UserREST() {
    }

    public UserREST(IdREST id, UsernameREST username, FirstNameREST firstName, LastNameREST lastName, PasswordREST password, AccountCreatedREST accountCreated, AccountLastModifiedREST accountLastModified, TokenREST token) {
        this.id = id;
        this.username = username;
        this.firstname = firstName;
        this.lastname = lastName;
        this.password = password;
        this.accountCreated = accountCreated;
        this.accountLastModified = accountLastModified;
        this.token = token;
    }

    public IdREST getId() {
        return id;
    }

    public void setId(IdREST id) {
        this.id = id;
    }

    public UsernameREST getUsername() {
        return username;
    }

    public void setUsername(UsernameREST username) {
        this.username = username;
    }

    public FirstNameREST getFirstname() {
        return firstname;
    }

    public void setFirstname(FirstNameREST firstname) {
        this.firstname = firstname;
    }

    public LastNameREST getLastname() {
        return lastname;
    }

    public void setLastname(LastNameREST lastName) {
        this.lastname = lastName;
    }

    public PasswordREST getPassword() {
        return password;
    }

    public void setPassword(PasswordREST password) {
        this.password = password;
    }

    public AccountCreatedREST getAccountCreated() {
        return accountCreated;
    }

    public void setAccountCreated(AccountCreatedREST accountCreated) {
        this.accountCreated = accountCreated;
    }

    public AccountLastModifiedREST getAccountLastModified() {
        return accountLastModified;
    }

    public void setAccountLastModified(AccountLastModifiedREST accountLastModified) {
        this.accountLastModified = accountLastModified;
    }

    public TokenREST getToken() {
        return token;
    }

    public void setToken(TokenREST token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserREST{" +
                "id=" + id +
                ", username=" + username +
                ", firstName=" + firstname +
                ", lastName=" + lastname +
                ", password=" + password +
                ", accountCreated=" + accountCreated +
                ", accountLastModified=" + accountLastModified +
                ", token=" + token +
                '}';
    }
}
