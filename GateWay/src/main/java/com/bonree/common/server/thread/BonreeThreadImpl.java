package com.bonree.common.server.thread;

import com.bonree.common.server.BonreeThread;
import com.bonree.common.util.LogUtils;
import com.bonree.common.server.ZKDataProce;
import com.bonree.common.util.ZKUtil;
import com.bonree.model.consts.ConfigurationParam;
import com.bonree.model.consts.ServerConsts;
import com.bonree.model.global.GlobalPara;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class BonreeThreadImpl extends BonreeThread {
    private static Logger log = LoggerFactory.getLogger(BonreeThreadImpl.class);

    public BonreeThreadImpl(int time) {
        super(time);
    }

    @Override
    protected void runPersonelLogic() {//心跳检测
        boolean bool = false;
        try {
            for (Map.Entry<String, Long> stringLongEntry : GlobalPara.isactive.entrySet()) {
                if (new Date().getTime() - stringLongEntry.getValue() > ConfigurationParam.masterChickTimeMs) {//断开了
                    String[] split = stringLongEntry.getKey().split("/");
                    log.info("{} delete->{}", LogUtils.getLine(), split[split.length - 1]);
                    ZKUtil.delete(ConfigurationParam.zk, stringLongEntry.getKey());
                    bool = true;
                }
            }
        } catch (Exception e) {
            log.error("{} java.util.ConcurrentModificationException.", LogUtils.getLine(),e);
        }
        if (bool) {
            log.info("{} update data", LogUtils.getLine());
            try {
                ConfigurationParam.zk.setData(ConfigurationParam.plugin, UUID.randomUUID().toString().replace("-", "").toLowerCase().getBytes(), -1);
                ZKDataProce.update();
            } catch (Exception e) {
                log.error("Modify the node content to trigger the listener to update all data exceptions.", e);
            }
        }
    }

}
