package org.example;

public class DriverUtils {
    public static String getDriver(String path){
        System.out.println(DriverUtils.class.getClassLoader().getResource(path).getPath());
        return DriverUtils.class.getClassLoader().getResource(path).getPath();

    }
}
