package com.bonree.common.server;

import com.bonree.common.util.IO;
import com.bonree.common.util.LogUtils;
import com.bonree.model.consts.ConfigurationParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class Disk {
    private static Logger log = LoggerFactory.getLogger(Disk.class);

    /**
     * 清理缓存
     *
     * @param path
     */
    public static void update(String path) {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(ConfigurationParam.ms);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                remove(path);
            }
        }).start();
    }

    /**
     * 删除文件
     *
     * @param path
     */
    private static void remove(String path) {
        String[] strings = IO.readChildFile(path);
        for (String string : strings) {
            if (!string.contains("-")) {
                continue;
            }
            Long time;
            try {
                time = Long.valueOf(string.split("-")[1]);//获取时间
            } catch (NumberFormatException e) {//防止填写的目录重复,引起的误删文件
                continue;
            }
            if ((new Date().getTime() - time) > 0) {//超过了缓存时间,删除数据
                log.debug("delete file is " + path);
                boolean remove = IO.remove(path, string);
                if (!remove) {
                    log.error("{} File deletion failed", LogUtils.getLine());
                }
            }
        }
    }
}