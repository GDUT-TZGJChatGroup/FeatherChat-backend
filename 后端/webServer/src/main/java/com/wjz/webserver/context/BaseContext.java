package com.wjz.webserver.context;

public class BaseContext {

    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setCurrentAcc(String Acc) {
        threadLocal.set(Acc);
    }

    public static String getCurrentAcc() {
        return threadLocal.get();
    }

    public static void removeCurrentAcc() {
        threadLocal.remove();
    }

}
