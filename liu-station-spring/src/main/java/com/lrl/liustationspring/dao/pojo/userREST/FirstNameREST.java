package com.lrl.liustationspring.dao.pojo.userREST;

import java.util.HashMap;

public class FirstNameREST extends FieldREST{

    private static String type = "string";

    private String value;

    private boolean readOnly;

    private boolean writeOnly;

    public FirstNameREST(String firstname, boolean readOnly, boolean writeOnly){
        value = firstname;
        this.readOnly = readOnly;
        this.writeOnly = writeOnly;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        FirstNameREST.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isWriteOnly() {
        return writeOnly;
    }

    public void setWriteOnly(boolean writeOnly) {
        this.writeOnly = writeOnly;
    }

    @Override
    public String toString() {
        return "FirstNameREST{" +
                "value='" + value + '\'' +
                '}';
    }
}
