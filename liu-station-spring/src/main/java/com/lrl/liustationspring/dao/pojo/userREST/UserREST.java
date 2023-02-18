package com.lrl.liustationspring.dao.pojo.userREST;

import com.lrl.liustationspring.dao.pojo.User;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;

public class UserREST {

    private static String type = "Object";

    private HashMap<String, FieldREST> properties;

    public UserREST(HashMap<String, FieldREST> properties) {
        this.properties = properties;
    }

    public UserREST(Integer id, String firstname, String lastname, String username, String password, Timestamp accountCreated, Timestamp accountLastModified, String token) {


        properties = new HashMap<String, FieldREST>();
        properties.put("id", new IdREST(id, true, false));
        properties.put("firstname", new FirstNameREST(firstname, true, false));
        properties.put("lastname", new LastNameREST(lastname, true, false));
        properties.put("username", new UsernameREST(username, true, false));
        properties.put("password", new PasswordREST(password, true, false));
        properties.put("time0", new AccountCreatedREST(accountCreated, true, false));
        properties.put("time1", new AccountLastModifiedREST(accountLastModified, true, false));
        properties.put("token", new TokenREST(token, true, false));


    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        UserREST.type = type;
    }

    public HashMap<String, FieldREST> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, FieldREST> properties) {
        this.properties = properties;
    }
}
