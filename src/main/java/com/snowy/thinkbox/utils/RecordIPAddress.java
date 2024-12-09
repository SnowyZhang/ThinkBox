package com.snowy.thinkbox.utils;

import java.io.Serializable;

//public class RecordIPAddress implements Serializable {
//
//    private static ThreadLocal<String> remoteAddr = new ThreadLocal<>();
//
//    public static String getRemoteAddr() {
//        return remoteAddr.get();
//    }
//
//    public static void setRemoteAddr(String remoteAddr) {
//        RecordIPAddress.remoteAddr.set(remoteAddr);
//    }
//
//}

public class RecordIPAddress implements Serializable{

    private static final ThreadLocal<String> remoteAddr = ThreadLocal.withInitial(() -> null);

    /**
     * 获取当前线程的远程 IP 地址
     *
     * @return 当前线程的远程 IP 地址
     */
    public static String getRemoteAddr() {
        return remoteAddr.get();
    }

    /**
     * 设置当前线程的远程 IP 地址
     *
     * @param remoteAddr 远程 IP 地址
     */
    public static void setRemoteAddr(String remoteAddr) {
        RecordIPAddress.remoteAddr.set(remoteAddr);
    }

    /**
     * 清除当前线程的远程 IP 地址，防止内存泄漏
     */
    public static void clear() {
        remoteAddr.remove();
    }
}
