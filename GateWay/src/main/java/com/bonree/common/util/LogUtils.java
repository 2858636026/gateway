package com.bonree.common.util;

public class LogUtils {

    /**
     * 显示类位置与行数
     * @return
     */
    public static String getLine() {
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        return new StringBuilder(element.getMethodName())
                .append("(")
                .append(element.getFileName())
                .append(":")
                .append(element.getLineNumber())
                .append(")")
                .append(" ->")
                .toString();
    }
}