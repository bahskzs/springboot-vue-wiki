package com.yqy.wiki.util;

import java.io.Serializable;

/**
 * @author bahsk
 * @createTime 2021-07-29 18:00
 * @description
 */
public class RequestContext implements Serializable {

    private static ThreadLocal<String> remoteAddr = new ThreadLocal<>();

    public static String getRemoteAddr() {
        return remoteAddr.get();
    }

    public static void setRemoteAddr(String remoteAddr) {
        RequestContext.remoteAddr.set(remoteAddr);
    }
}
