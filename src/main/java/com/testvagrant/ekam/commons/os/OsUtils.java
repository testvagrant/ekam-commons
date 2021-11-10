package com.testvagrant.ekam.commons.os;

public final class OsUtils {
    private static String os = null;

    public static String getOsName() {
        if (os == null) {
            os = System.getProperty("os.name");
        }
        return os;
    }

    public static boolean isWindows() {
        return getOsName().startsWith("Windows");
    }
}