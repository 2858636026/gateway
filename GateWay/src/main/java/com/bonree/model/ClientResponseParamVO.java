package com.bonree.model;

/**
 * 响应模型
 */
public class ClientResponseParamVO {
    private String reason;//提示内容
    private int code;//响应类型
    private String result;//响应值
    private Long cacheTimeMs;//缓存时间   可为空
    private String mess;

    public ClientResponseParamVO() {
    }

    /**
     * 防止再解析
     *
     * @param result
     */
    public void setString(String result) {
        this.mess = result;
    }

    @Override
    public String toString() {
        if ((mess == null) && (code != 0)) {
            mess = "{" +
                    "\"reason\":\"" + reason + '\"' +
                    ", \"code\":" + code +
                    ", \"result\":" + result +
                    "}";
        }
        return mess;
    }


    public Long getTime() {
        return cacheTimeMs;
    }

    public void setTime(Long cacheTimeMs) {
        this.cacheTimeMs = cacheTimeMs;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
