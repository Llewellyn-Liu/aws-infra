package com.lrl.liustationspring.dao.pojo.userREST;

public class IdREST extends FieldREST{
    private static String type = "int";

    private int value;

    private boolean readOnly;

    private boolean writeOnly;

    public IdREST(int id, boolean readOnly, boolean writeOnly ) {
        this.value = id;
        this.readOnly = readOnly;
        this.writeOnly = writeOnly;
    }

    public static String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public static void setType(String type) {
        IdREST.type = type;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "IdREST{" +
                "value=" + value +
                '}';
    }
}
