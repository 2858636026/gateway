package com.bonree.common.util;

import com.bonree.model.consts.ConfigurationParam;
import com.bonree.model.global.GlobalPara;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * zk工具类
 */
public class ZKUtil {
    private static Logger log = LoggerFactory.getLogger(ZKUtil.class);

    /**
     * 递归删除节点
     *
     * @param zk   zk客户端对象
     * @param path 删除zk的目录
     */
    public static boolean delete(ZooKeeper zk, String path) {
        ArrayList<String> paths = new ArrayList();
        paths.add(path);
        try {
            recording(zk, path, paths);
        } catch (Exception e) {
            log.error("{} {}", LogUtils.getLine(), e);
        }
        Collections.reverse(paths);
        //删除节点
        for (String childrenPath : paths) {
            try {
                zk.delete(childrenPath, -1);
            } catch (Exception e) {
                log.error("delete zknode error {}", e);
            }
        }
        return true;
    }

    /**
     * 读取所有子节点,删除方法的依赖
     *
     * @param zk
     * @param path
     * @param paths
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static void recording(ZooKeeper zk, String path, ArrayList<String> paths) throws Exception {
        List<String> children = null;
        try {
            children = zk.getChildren(path, false);
        } catch (Exception e) {
            throw new Exception(String.format("[%s] abnormal,%s", path, e.getMessage()));
        }
        for (String child : children) {
            String childPath = new StringBuilder(path).append("/").append(child).toString();
            paths.add(childPath);
            recording(zk, childPath, paths);
        }
    }

    /**
     * 创建永久节点,成功返回
     *
     * @param zk
     * @param path
     * @param str
     */
    public static boolean createPermanent(ZooKeeper zk, String path, String str) {
        try {
            if (ParamUtil.objIsExist(zk.exists(path, false))) {
                zk.create(path, str.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 创建节点,已经存在也算成功
     *
     * @param zk
     * @param path
     * @param str
     * @return
     */
    public static int createPermanentExists(ZooKeeper zk, String path, String str) {
        try {
            if (zk.exists(path, false) == null) {
                zk.create(path, str.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("{} create node successfully {} -> {}", LogUtils.getLine(), path, str);
                return 1;
            }
            log.info("{} node already exists {}->{}", LogUtils.getLine(), path, str);
            return 2;
        } catch (Exception e) {
            log.error("{} failed to create node {} -> {}", LogUtils.getLine(), path, str);
            return 3;
        }

    }


    /**
     * 创建临时节点
     *
     * @param zk
     * @param path
     * @param str
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static boolean createTemporary(ZooKeeper zk, String path, String str) {
        try {
            //创建的节点如果已经存在,抛异常
            String master = zk.create(path, str.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            if (zk.exists(path, false) == null) {
                log.error("After the primary node is created, check that the node does not exist.");
                return false;//创建失败
            }
            log.info("The primary node is created successfully :{}", master);
            return true;

        } catch (Exception e) {
            log.info("The primary node creates an exception. This node may already exist.");
        }
        //检查这个节点是否存在
        String masterData;
        try {
            masterData = new String(zk.getData(path, false, null));
            if ((ConfigurationParam.address + ":" + ConfigurationParam.port).equals(masterData)) {
                log.info("{} Creation failed, node content is current host", LogUtils.getLine());
                return true;
            }
            log.info("{} Creation failed, node content is not current host", LogUtils.getLine());
            return false;
        } catch (Exception e) {
            log.error("Get the primary node data exception,{}", e);
        }
        //读取主节点文件发送异常,文件可能不存在,返回创建失败,监听
        return false;
    }


    /**
     * 获取路径下目录值
     *
     * @param zk
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static String getZkString(ZooKeeper zk, String path) {
        try {
            if (zk.exists(path, false) == null) {
                return null;
            }
            return new String(zk.getData(path, false, null));
        } catch (Exception e) {
            log.error("get zk data error,path is {}", path, e);
        }
        return null;
    }

}
