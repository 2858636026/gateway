package com.bonree.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class IO {
    private static Logger log = LoggerFactory.getLogger(IO.class);

    /**
     * 写入文件
     *
     * @param path
     * @param name
     * @param time
     * @param data
     * @return
     */
    public static boolean write(String path, String name, Long time, String data) throws IOException {
        //找到目标文件

        FileWriter fileWriter = null;
        try {
            File file = new File(new StringBuilder(path).append("/").toString(), new StringBuilder(name).append("-").append(time).toString());
            System.out.println(new StringBuilder(path).append("/").toString()+ new StringBuilder(name).append("-").append(time).toString());
            // 建立数据的输入通道
            log.info("{} {}", LogUtils.getLine(), " ", file.toString());
            fileWriter = new FileWriter(file);
            fileWriter.write(data);
            fileWriter.flush();
            return true;
        } catch (IOException e) {
            log.error("{} 删除异常", LogUtils.getLine());
            return false;
        } finally {
            ResourceRelease.release(fileWriter);
        }
    }

    /**
     * 读取文件内容
     *
     * @param path
     * @param name
     * @return
     */
    public static String readFile(String path, String name) {
        StringBuffer stringBuffer = null;
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        try {
            File file = new File(new StringBuilder(path).append("/").toString(), name);
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            stringBuffer = new StringBuffer();
            String read;
            while ((read = bufferedReader.readLine()) != null) {
                stringBuffer.append(read);
            }
        } catch (IOException e) {
            log.error("{}{}{}: has been deleted, can not be read", path, "/", name);
        } finally {
            ResourceRelease.release(fileReader);
            ResourceRelease.release(bufferedReader);
        }

        if (stringBuffer != null) {
            return stringBuffer.toString();
        }
        return null;
    }

    /**
     * 读取所有文件名称
     *
     * @param path
     * @return
     */
    public static String[] readChildFile(String path) {
        File[] files = new File(path).listFiles();
        String[] Names = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            Names[i] = files[i].getName();
        }
        return Names;
    }

    /**
     * 删除文件
     *
     * @param path
     * @param name
     * @return
     */
    public static boolean remove(String path, String name) {//删除文件
        File file = new File(path + "/", name);
        return file.delete();
    }

    /**
     * 创建文件
     *
     * @param savePath
     */
    public static void mkdir(String savePath) {
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


}
