package com.jnm.Tutor.util;

import java.util.UUID;


public class UUIDFactory {
    public static String random() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
