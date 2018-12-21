package com.bonree.common.server;

import com.bonree.model.consts.ServerConsts;
import com.bonree.model.global.GlobalPara;

import java.util.Date;
import java.util.Map;

/**
 * token 本地校验
 */
public class TokenpProce {


    /**
     * 初始化token
     */
    public static void init() {
        thread(1000 * 60 * 10L);//十分钟清理一次token
    }

    public static void thread(Long tokenTime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(tokenTime);//检测时间
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    check();//删除失效的token
                }
            }
        }).start();
    }

    private static void check() {
        for (Map.Entry<String, Map<String, Object>> token : GlobalPara.tokenLocalhost.entrySet()) {
            if (Long.valueOf(token.getValue().get(ServerConsts.TIME).toString()) < new Date().getTime()) {//超时token无效了
                GlobalPara.tokenLocalhost.remove(token.getKey());//移除
            }
        }

    }


}
