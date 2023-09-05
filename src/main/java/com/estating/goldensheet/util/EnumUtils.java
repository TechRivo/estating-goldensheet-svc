package com.estating.goldensheet.util;

public class EnumUtils {

    public static String normalize(String name) {
        return name.replaceAll("_", " ")
                .replaceAll("-", " ")
                .toLowerCase();
    }

}
