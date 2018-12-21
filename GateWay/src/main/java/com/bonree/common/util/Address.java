package com.bonree.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 获取正确的ip地址,ip地址是一个列表,根据优先级选取最合适的ip地址
 */
public class Address {
    public static String getLocalHostLANAddress() throws Exception {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        break;
                    }
                    if (inetAddr.isSiteLocalAddress()) {
                        // 如果是site-local地址，就是它了
                        return inetAddr.getHostAddress();
                    }
                    if (candidateAddress == null) {
                        // site-local类型的地址未被发现，先记录候选地址
                        candidateAddress = inetAddr;
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
