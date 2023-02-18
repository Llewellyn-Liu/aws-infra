package com.lrl.liustationspring.dao.pojo.userREST;

public class PasswordREST extends FieldREST{
    private static String type = "string";

    private String value;

    private boolean readOnly;

    private boolean writeOnly;
    public PasswordREST(String password, boolean readOnly, boolean writeOnly) {
        this.value = password;
        this.readOnly = readOnly;
        this.writeOnly = writeOnly;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        PasswordREST.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PasswordREST{" +
                "value='" + value + '\'' +
                '}';
    }
}
