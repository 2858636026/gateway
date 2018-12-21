package com.bonree.common.ha;

import com.bonree.common.server.ZKDataProce;
import com.bonree.common.util.ZKUtil;
import com.bonree.model.consts.ConfigurationParam;
import com.bonree.model.global.GlobalPara;


public class HaUtils {

    /**
     * 创建主节点
     *
     * @throws Exception
     */
    public static void createTemporaryDir() throws Exception {
        GlobalPara.active = ZKUtil.createTemporary(ConfigurationParam.zk, ConfigurationParam.masterPath, new StringBuilder(ConfigurationParam.address).append(":").append(ConfigurationParam.port).toString());
        GlobalPara.masterAddress = ZKUtil.getZkString(ConfigurationParam.zk, ConfigurationParam.masterPath);
        GlobalPara.heartbeatDetection.setSuspend(!GlobalPara.active);//是否阻塞
        ZKDataProce.cycleWatch(ConfigurationParam.zk, ConfigurationParam.masterPath);
    }


}
