package com.lrl.liustationspring.dao.pojo.userREST;

public class TokenREST extends FieldREST{
    private static String type = "string";

    private String value;

    private boolean readOnly;

    private boolean writeOnly;
    public TokenREST(String token, boolean readOnly, boolean writeOnly) {
        this.value = token;
        this.readOnly = readOnly;
        this.writeOnly = writeOnly;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        TokenREST.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TokenREST{" +
                "value='" + value + '\'' +
                '}';
    }
}
