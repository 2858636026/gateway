package com.bonree.common.util;


import com.bonree.model.consts.ServerConsts;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

public class ParamUtil {

    /**
     * 字符串是否为空或者空串
     *
     * @param str
     * @return
     */
    public static boolean strIsExist(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;

    }

    /**
     * 对象是否为空
     *
     * @param obj
     * @return
     */
    public static boolean objIsExist(Object obj) {
        if (obj == null) {
            return true;
        }
        return false;

    }

    /**
     * 删除请求中的多余内容并生产一个map集合用于转化Hash码与对应的缓存文件
     *
     * @param param
     * @return
     */
    public static Map<String, String> getMap(Map<String, String> param) {
        Map<String, String> temp = new HashMap<>();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            temp.put(entry.getKey(),entry.getValue());
        }
        temp.remove(ServerConsts.USERNAME);
        temp.remove(ServerConsts.TOKEN);
        temp.remove(ServerConsts.IP);
        temp.remove(ServerConsts.CHACHTIMES);
        return temp;
    }

}
