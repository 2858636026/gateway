package com.bonree.common.code;


public enum CodeMessage {

    OK(200, "ok"),
    URI_ERROR(5001, "Path exception 路径异常"),
    PARAM_EOORO(5002, "Abnormal parameter 异常参数"),
    TOKEN_EOORO(5003, "Abnormal authority 异常权限"),
    RELY_ERROR(5004, "Dependency exception 依赖性异常"),
    INTERNAL_ERROR(5005, "Primary server internal exception 主服务器内部异常"),
    REPEAT_ERROR(5006, "Repeat registration 重复注册"),
    SERVICE_EOORO(5007, "Service not registered 服务未注册"),
    PLUGIN_EOORO(5008, "Plugin address is not open 插件地址未打开"),
    OTHER_ERROR(5009, "Other exception 其他");

    private int code;
    private String message;

    CodeMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage(String prompt) {
        return message + String.format("[%s]: ", prompt);
    }

    public String getMessage() {
        return message + ": ";
    }

}
