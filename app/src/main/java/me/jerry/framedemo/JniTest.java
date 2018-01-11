package me.jerry.framedemo;

/**
 * Created by Jerry on 2017/9/1.
 */

public class JniTest {
    static {
        System.loadLibrary("MyLibrary");
    }

    public static native String getString();

}
