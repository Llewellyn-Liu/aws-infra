package com.lrl.liustationspring;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        File f = new File("thisdir");
        if(!f.exists()) f.mkdir();
    }
}
