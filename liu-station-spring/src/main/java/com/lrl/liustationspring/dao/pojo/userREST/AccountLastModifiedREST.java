package com.lrl.liustationspring.dao.pojo.userREST;

import java.sql.Timestamp;

public class AccountLastModifiedREST extends FieldREST{
    private static String type = "int";

    private Timestamp value;

    private boolean readOnly;

    private boolean writeOnly;

    public AccountLastModifiedREST(Timestamp value, boolean readOnly, boolean writeOnly) {
        this.value = value;
        this.readOnly = readOnly;
        this.writeOnly = writeOnly;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        AccountLastModifiedREST.type = type;
    }

    public Timestamp getValue() {
        return value;
    }

    public void setValue(Timestamp value) {
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
        return "AccountCreatedREST{" +
                "value=" + value +
                ", readOnly=" + readOnly +
                ", writeOnly=" + writeOnly +
                '}';
    }
}
