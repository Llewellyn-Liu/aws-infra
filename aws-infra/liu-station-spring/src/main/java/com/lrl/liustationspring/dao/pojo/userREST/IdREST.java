package com.lrl.liustationspring.dao.pojo.userREST;

public class IdREST extends FieldREST {

    private int value;

    private String readonly;

    private String writeonly;

    public IdREST(){

    }

    public IdREST(int value, String readonly, String writeonly) {
        this.value = value;
        this.readonly = readonly;
        this.writeonly = writeonly;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getReadonly() {
        return readonly;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    public String getWriteonly() {
        return writeonly;
    }

    @Override
    public String toString() {
        return "IdREST{" +
                "value=" + value +
                ", readonly='" + readonly + '\'' +
                ", writeonly='" + writeonly + '\'' +
                '}';
    }

    public void setWriteonly(String writeonly) {
        this.writeonly = writeonly;
    }
}