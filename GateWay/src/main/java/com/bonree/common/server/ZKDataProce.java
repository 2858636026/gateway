package com.bonree.common.server;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonree.common.util.LogUtils;
import com.bonree.common.util.ParamUtil;
import com.bonree.common.util.ZKUtil;
import com.bonree.model.consts.ConfigurationParam;
import com.bonree.model.consts.ServerConsts;
import com.bonree.model.global.GlobalPara;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * zookeeper处理类
 */
public class ZKDataProce {

    private static Logger log = LoggerFactory.getLogger(ZKDataProce.class);

    /**
     * 同步数据
     *
     * @param zk
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static void getZKdata(ZooKeeper zk, String path) throws KeeperException, InterruptedException {
        Map<String, Map<String, JSONObject>> zkDataMap = getZKDataMap(zk, path);//更新内存数据
//        log.info("{},{}","---",zkDataMap);
        GlobalPara.allAddress = zkDataMap;
        Map<String, String> zkServerMap = getZKServerMap(zk, path);
        GlobalPara.tableAndServer = zkServerMap;
        Map<String, JSONObject> zkDataServiceMilit = getZKDataServiceMilit(zk, path);
        GlobalPara.serviceLimiting = zkDataServiceMilit;
        update();
        //监听数据方法,调用获取数据
        SynchronousData(zk, path);
    }

    /**
     * 获取下一级目录所有参数的map集合
     *
     * @param zk   zk客户端
     * @param path
     * @return
     */
    private static Map<String, JSONObject> getZKDataServiceMilit(ZooKeeper zk, String path) {
        Map<String, JSONObject> map = null;
        String msg;
        String childname;
        JSONObject jsonObject;
        try {
            List<String> children = zk.getChildren(path, false);
            map = new HashMap<>();
            for (String childName : children) {
                childname = childName;
                msg = new String(zk.getData(new StringBuilder(path).append("/").append(childName).toString(), false, null));
                try {
                    if (ParamUtil.strIsExist(msg)) {
                        return map;
                    }
                    jsonObject = JSONObject.parseObject(msg, JSONObject.class);
                    map.put(childName, jsonObject);
                } catch (Exception e) {
                    log.error("{} The data anomaly zk content is : {}{}{}->{}] ", LogUtils.getLine(), path, "/", childname, msg);
                }
            }
        } catch (Exception e) {
            log.error("{} Query the child node exception, the path is :{}", LogUtils.getLine(), path);
        }
        return map;
    }


    /**
     * 监听同步数据
     *
     * @param zk
     * @param path
     * @throws KeeperException
     * @throws InterruptedException
     */
    private static void SynchronousData(ZooKeeper zk, String path) throws KeeperException, InterruptedException {
        zk.exists(ConfigurationParam.plugin, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                //获取数据再监听
                try {
                    log.info("{} Trigger listener", LogUtils.getLine());
                    getZKdata(zk, path);
                } catch (Exception e) {
                    log.error("Synchronous data exception", e);
                }
            }
        });
    }


    /**
     * 按照格式获取dataList数据
     *
     * @param zk
     * @param path
     */

    private static Map<String, Map<String, JSONObject>> getZKDataMap(ZooKeeper zk, String path) throws KeeperException, InterruptedException {
        Map<String, Map<String, JSONObject>> map = new HashMap<>();
        List<String> children = zk.getChildren(path, false);
        for (String service : children) {
            List<String> address = zk.getChildren(new StringBuilder(ConfigurationParam.plugin).append("/").append(service).toString(), false);
            for (String addres : address) {
                String ipPort = addres;
                //  String tps = new String(zk.getData(ServerConsts.plugin + "/" + service + "/" + addres, false, null));
                String dataSourceListPath = new StringBuilder(ConfigurationParam.plugin).append("/").append(service).append("/").append(addres).append("/").append(ServerConsts.pluginDataSourceList).toString();
                JSONArray jsonArray = JSONArray.parseObject(new String(zk.getData(dataSourceListPath, false, null)), JSONArray.class);
                for (Object str : jsonArray) {
                    JSONObject jsonObject = JSONObject.parseObject(str.toString(), JSONObject.class);
                    String serviceName = jsonObject.getString(ServerConsts.SERVICENAME);//拼接名称为三个
                    String serviceType = jsonObject.getString(ServerConsts.SERVICETYPE);//拼接名称为三个
                    String cacheTimeMs = jsonObject.getString(ServerConsts.CHACHTIMES);
                    String formatServerName = serviceType + "|||" + serviceName;
                    JSONObject timeMs = JSONObject.parseObject(new StringBuilder("{\"").append(ServerConsts.CHACHTIMES).append("\":").append(cacheTimeMs).append("}").toString(), JSONObject.class);
                    if (map.get(formatServerName) == null) {
                        Map<String, JSONObject> internal = new HashMap<>();
                        internal.put(ipPort, timeMs);
                        map.put(formatServerName, internal);
                    } else {
                        map.get(formatServerName).put(ipPort, timeMs);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 读取服务
     *
     * @param zk
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    private static Map<String, String> getZKServerMap(ZooKeeper zk, String path) throws KeeperException, InterruptedException {
        Map<String, String> map = new HashMap<>();
        List<String> children = zk.getChildren(path, false);
        for (String service : children) {
            List<String> address = zk.getChildren(new StringBuilder(ConfigurationParam.plugin).append("/").append(service).toString(), false);
            for (String addres : address) {
                String dataSourceListPath = new StringBuilder(ConfigurationParam.plugin).append("/").append(service).append("/").append(addres).append("/").append(ServerConsts.pluginDataSourceList).toString();
                JSONArray jsonArray = JSONArray.parseObject(new String(zk.getData(dataSourceListPath, false, null)), JSONArray.class);
                for (Object str : jsonArray) {
                    JSONObject jsonObject = JSONObject.parseObject(str.toString(), JSONObject.class);
                    String serviceName = jsonObject.getString(ServerConsts.SERVICENAME);//拼接名称为三个
                    String serviceType = jsonObject.getString(ServerConsts.SERVICETYPE);//拼接名称为三个
                    map.put(serviceType + ServerConsts.splitter + serviceName, service);
                }
            }
        }
        return map;
    }


    /**
     * 监听选主
     *
     * @param zk
     * @param path
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static void cycleWatch(ZooKeeper zk, String path) throws KeeperException, InterruptedException {
        log.info("注册监听");
        zk.exists(path, watchedEvent -> {
            log.info("触发监听,Master node delete, trigger listener");
            GlobalPara.active = ZKUtil.createTemporary(zk, path, ConfigurationParam.address + ":" + ConfigurationParam.port);
            GlobalPara.masterAddress = ZKUtil.getZkString(ConfigurationParam.zk, ConfigurationParam.masterPath);

            if (GlobalPara.active) {//更新
                update();
            }
            //更新内容
            GlobalPara.heartbeatDetection.setSuspend(!GlobalPara.active);
            try {
                cycleWatch(zk, path);
            } catch (Exception e) {
                log.error("Listen, select the primary node error", e);
            }
        });
    }

    /**
     * 更新节点状态
     */
    public static void update() {
        log.info(LogUtils.getLine() + " Update memory");
        HashMap hashMap = new HashMap();
        for (Map.Entry<String, Map<String, JSONObject>> ipAndTps : GlobalPara.allAddress.entrySet()) {
            Set<Map.Entry<String, JSONObject>> entries = ipAndTps.getValue().entrySet();
            for (Map.Entry<String, JSONObject> entry : entries) {
                hashMap.put(new StringBuilder(ConfigurationParam.plugin).append("/").append(GlobalPara.tableAndServer.get(ipAndTps.getKey())).append("/").append(entry.getKey()).toString(), new Date().getTime());
            }
        }
        GlobalPara.isactive.clear();
        GlobalPara.isactive.putAll(hashMap);
    }
}
