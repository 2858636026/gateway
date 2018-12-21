package com.bonree.model.consts;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import com.bonree.common.server.thread.BonreeThreadImpl;
import com.bonree.common.util.Address;
import com.bonree.common.xml.XmlUtils;
import com.bonree.model.global.GlobalPara;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 读取配置参数
 */
public class ConfigurationParam {
    public static Integer port;//网关端口
    public static String address;//网关ip,可选配置
    public static Integer bossGroup;
    public static Integer workerGroup;
    public static ZooKeeper zk;//zk连接参数
    public static String masterPath;//主节点注册路径
    public static Integer masterChickTimeMs;//主节点检测时间间隔

    public static Map<String, String> ServiceToken; //所有服务校验
    public static String saveDisk;//缓存本地磁盘位置
    public static Long ms;//缓存本地磁盘时间
    public static Integer defaultMaxServicesNumber; //默认最大限流参数
    public static Integer forwardinglimit;  //转发次数上限
    public static String pack;//一级根目录
    public static String plugin;//二级目录
    public static String tokenUrl;//token地址
    public static Boolean TOKENDEBUG;//是否测试去掉token校验
    public static Boolean PLUGINDEBUG;//是否测试去掉plugin,使用假数据


    /**
     * 读取初始化所有参数
     *
     * @param filePath 网关配置文件
     */
    public static void init(String filePath) throws Exception {

        //读取网关http参数
        XmlUtils xmlUtils = new XmlUtils(filePath);
        Element httpElement = xmlUtils.getElement("global http");
        port = xmlUtils.getIntegerValue(httpElement, "port");
        try {
            address = xmlUtils.getStringValue(httpElement, "address");//可选配合
        } catch (Exception e) {
            address = Address.getLocalHostLANAddress();//获取主机ip
        }
        bossGroup = xmlUtils.getIntegerValue(httpElement, "bossGroup");
        workerGroup = xmlUtils.getIntegerValue(httpElement, "workerGroup");
        //获取主机ip

        //读取zookeeper参数

        Element zkElement = xmlUtils.getElement("global zookeeper");
        String zkAddress = xmlUtils.getStringValue(zkElement, "address");
        Integer sessionTimeout = xmlUtils.getIntegerValue(zkElement, "sessionTimeout");
        zk = new ZooKeeper(zkAddress, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // TODO Auto-generated method stub
            }
        });

        masterPath = xmlUtils.getStringValue(zkElement, "masterPath");
        //拼接
//        if (masterPath.split("//").length != 2) {
//            String[] split = masterPath.split("//");
//            System.out.println(masterPath + " is error ,Must use secondary directory [/***/***]");
//            System.exit(0);
//        }
        pack = "/" + masterPath.split("/")[1];

        plugin = pack + "/registered";

        //获取token地址
        Element tokenElement = xmlUtils.getElement("global token");
        tokenUrl = xmlUtils.getStringValue(tokenElement, "tokenUrl");
        TOKENDEBUG = "debug".equalsIgnoreCase(tokenElement.attr("type"));

        //获取最大请求与缓存路径和转发最大次数
        Element gatewayElement = xmlUtils.getElement("global gateway");
        saveDisk = xmlUtils.getStringValue(gatewayElement, "saveDisk");
        ms = Long.valueOf(xmlUtils.getValue(gatewayElement, "ms"));
        forwardinglimit = xmlUtils.getIntegerValue(gatewayElement, "forwardinglimit");
        defaultMaxServicesNumber = xmlUtils.getIntegerValue(gatewayElement, "maxServicesNumber");

        //读取插件token
        ServiceToken = new HashMap<>();
        Element pluginElement = xmlUtils.getElement("global plugin");
        PLUGINDEBUG = "debug".equalsIgnoreCase(pluginElement.attr("type"));
        Elements children = pluginElement.children();
        for (Element child : children) {
            ServiceToken.put(child.attr("name"), child.val());
        }
        //实例化阻塞线程
        masterChickTimeMs = xmlUtils.getIntegerValue(zkElement, "masterChickTimeMs");//检测时间间隔
        GlobalPara.heartbeatDetection = new BonreeThreadImpl(masterChickTimeMs);

        //加载日志
        try {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();
            configurator.doConfigure(ConfigurationParam.class.getClassLoader().getResourceAsStream("logback.xml"));
            StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        init();//实例化类
    }

    private static void init() {//实例化全局变量
        GlobalPara.allAddress = new HashMap<>();
        GlobalPara.isactive = new HashMap<>();
        GlobalPara.serviceLimiting = new HashMap<>();
        GlobalPara.currentLimiting = new HashMap<>();
        GlobalPara.tokenLocalhost = new HashMap<>();
        GlobalPara.tpsParams = new ArrayList<>();//tps参数

        //设置tps参数值
        GlobalPara.tpsParams.add(ServerConsts.maxServicesNumber);//tps参数存入

    }
}
