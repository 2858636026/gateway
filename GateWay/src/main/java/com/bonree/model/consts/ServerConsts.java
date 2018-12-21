package com.bonree.model.consts;


import java.util.ArrayList;
import java.util.List;

/**
 * 常量
 */
public class ServerConsts {

    //保存url


    //客户端参数
    public final static String CLIENTURI = "/v2.0";             //客户端
    public final static String PLUGINURI = "/service";          //插件
    public final static String REQUESTPLUGINURI = "/plugin";    //请求插件的地址
    public final static String DEFAULTURI = "/";                //默认
    public final static String URI = "uri";


    public final static String HTTP = "http://"; //协议
    public final static String HEAD = "head"; //请求头
    public final static String BODY = "body"; //请求体


    public final static String TOKEN = "token";
    public final static String TIME = "time";

    public final static String CODE = "code";
    public final static String REASON = "reason";
    public final static String RESULT = "result";
    public final static String ERRORCODE = "errorCode";
    public final static String TYPE = "type";


    //HTTP请求变量
    public final static String CONTENT_TYPE_VALUE = "text/json;charset=utf-8";
    public final static String USER_AGENT_VALUE = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)";

    public final static String USERNAME = "username";
    public final static String SERVICETYPE = "serviceType";
    public final static String SERVICENAME = "serviceName";
    public final static String SERVER = "server";
    public final static String CHACHTIMES = "cacheTimeMs";
    public final static String CACHE = "cache";
    public final static String IP = "ip";


    //保存字段名称

    //插件参数
    public final static String PLUGINFLAG = "flag";
    public final static String SERVICE = "service";
    public final static String TPS = "tps";
    public final static String PORT = "port";
    public final static String pluginDataSourceList = "dataSourceList";
    public final static String PLUGINREGISTERED = "0";
    public final static String PLUGINHEARTBEAT = "1";
    //插件tps限流参数字段名称
    public final static String maxServicesNumber = "maxServicesNumber";


    public final static String splitter = "|||";


    public static List heads;//排除多余的请求头

    static {
        heads = new ArrayList();
        heads.add(USERNAME);
        heads.add(TOKEN);
        heads.add(SERVICETYPE);
        heads.add(SERVICENAME);
        heads.add(CHACHTIMES);
    }

}