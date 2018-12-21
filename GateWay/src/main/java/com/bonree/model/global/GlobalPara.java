package com.bonree.model.global;

import com.alibaba.fastjson.JSONObject;
import com.bonree.common.server.BonreeThread;
import com.bonree.common.util.ParamUtil;

import java.util.List;
import java.util.Map;

/**
 * 全局变量
 */
public class GlobalPara {

    public static boolean active;//是否为主节点
    public static Map<String, Map<String, JSONObject>> allAddress;   //活跃插件地址
    public static String masterAddress;   //主节点地址
    public static Map<String, String> tableAndServer;   //具体的表与服务名称
    public static Map<String, JSONObject> serviceLimiting;   //服务限流参数
    public static Map<String, Integer> currentLimiting;   //当前服务人数
    public static Map<String, Long> isactive;//记录节点状态使用<ip:端口,时间>
    public static BonreeThread heartbeatDetection;//阻塞线程,心跳检测使用
    public static Map<String, Map<String, Object>> tokenLocalhost;//token本地缓存
    public static List<String> tpsParams;//tps参数

    /**
     * 限流控制,获取当前服务的使用情况
     *
     * @param service
     * @return
     */

    public static synchronized Integer userCurrent(String service) {
        if (ParamUtil.objIsExist(currentLimiting.get(service))) {
            currentLimiting.put(service, 1);
            return 1;
        } else {
            currentLimiting.put(service, currentLimiting.get(service) + 1);
        }
        return currentLimiting.get(service);
    }

    /**
     * 释放服务
     *
     * @param service
     */
    public static synchronized void freedCurrent(String service) {
        currentLimiting.put(service, currentLimiting.get(service) - 1);
    }
}
