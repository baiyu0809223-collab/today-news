package com.pxxy;

import java.util.UUID;

public class test {
    public static void main(String[] args) {
        String new_name="违规名称_"+ UUID.randomUUID().toString().substring(0, 5);
        System.out.printf(new_name);
    }
}
