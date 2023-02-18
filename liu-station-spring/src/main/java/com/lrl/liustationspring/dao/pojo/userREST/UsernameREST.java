package com.lrl.liustationspring.dao.pojo.userREST;

public class UsernameREST extends FieldREST{
    private static String type = "string";

    private String value;

    private boolean readOnly;

    private boolean writeOnly;

    public UsernameREST(String username, boolean readOnly, boolean writeOnly) {
        this.value = username;
        this.readOnly = readOnly;
        this.writeOnly = writeOnly;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        UsernameREST.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "UsernameREST{" +
                "value='" + value + '\'' +
                '}';
    }
}
