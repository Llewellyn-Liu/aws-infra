package com.lrl.liustationspring.jsonTest;

import java.util.HashMap;

public class Prime {

    private String name;

    private String value;

    private HashMap<String, Integer> pair;

    public Prime(String name, String value, HashMap<String, Integer> pair) {
        this.name = name;
        this.value = value;
        this.pair = pair;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public HashMap<String, Integer> getPair() {
        return pair;
    }

    public void setPair(HashMap<String, Integer> pair) {
        this.pair = pair;
    }

    @Override
    public String toString() {
        return "Prime{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", pair=" + pair +
                '}';
    }
}
