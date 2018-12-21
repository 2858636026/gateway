package com.bonree.common.client;


import com.alibaba.fastjson.JSONObject;
import com.bonree.common.util.ResourceRelease;
import com.bonree.model.ClientResponseParamVO;
import com.bonree.model.consts.ServerConsts;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.net.telnet.TelnetClient;

/**
 * 客户端工具类
 */
public class HttpClient {


    private static Logger log = LoggerFactory.getLogger(HttpClient.class);

    /**
     * 向指定URL发送POST请求
     *
     * @param url
     * @param heads
     * @param body
     * @return 响应结果
     */
    public static Map<String, Object> sendPost(String url, Map<String, String> heads, Map<String, String> body, ClientResponseParamVO clientResponseParamVO) {
        PrintWriter out = null;
        BufferedReader in = null;
        String res = "";
        Map<String, Object> value = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
            conn.setRequestProperty(HTTP.CONTENT_TYPE, ServerConsts.CONTENT_TYPE_VALUE);
            conn.setRequestProperty(HTTP.USER_AGENT, ServerConsts.USER_AGENT_VALUE);
            if (heads != null) {//设置头信息
                for (Map.Entry<String, String> header : heads.entrySet()) {
                    if ("null".equals(header.getValue()) || header.getValue() == null) {
                        clientResponseParamVO.setReason(new StringBuilder(header.getKey()).append(" ").append("[value is null]").toString());
                        throw new Exception(new StringBuilder(header.getKey()).append(" ").append("is null").toString());
                    } else {
                        conn.setRequestProperty(header.getKey(), header.getValue());
                    }
                }
            }
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //获取响应头信息
            out = new PrintWriter(conn.getOutputStream());
            // 设置请求属性
            String param = "";
            if (body != null && body.size() > 0) {
                Iterator<String> ite = body.keySet().iterator();
                while (ite.hasNext()) {
                    String key = ite.next();// key
                    String val = body.get(key);
                    val = URLEncoder.encode(val, "utf-8");//编码
                    if (val != null && !"null".equals(val)) {
                        param = new StringBuilder(param).append(key).append("=").append(val).append("&").toString();
                    } else {
                        clientResponseParamVO.setReason(new StringBuilder(key).append(" k:v is null").toString());
                        throw new Exception(new StringBuilder(key).append(" k:v is null").toString());
                    }
                }
                param = param.substring(0, param.length() - 1);
            }

            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                res += line;
            }

            Map head = conn.getHeaderFields();
            Set<String> keys = head.keySet();
            JSONObject headData = new JSONObject();
            for (String key : keys) {
                headData.put(key, conn.getHeaderField(key));
            }
            JSONObject jsonObject;
            try {
                jsonObject = JSONObject.parseObject(res, JSONObject.class);
            } catch (Exception e) {
                clientResponseParamVO.setReason("json error  message");
                log.error("json error  message{},{}", res, e);
                return null;
            }
            JSONObject bodyData = new JSONObject();
            bodyData.put(ServerConsts.CODE, jsonObject.getInteger(ServerConsts.CODE));
            bodyData.put(ServerConsts.REASON, jsonObject.getString(ServerConsts.REASON));
            bodyData.put(ServerConsts.RESULT, jsonObject.getString(ServerConsts.RESULT));
            value = new HashMap<>();
            value.put(ServerConsts.HEAD, headData);
            value.put(ServerConsts.BODY, bodyData);
        } catch (Exception e) {
            clientResponseParamVO.setReason("Sending a post request is abnormal! ,head :" + heads + ",body " + body + " url :" + url);
            e.printStackTrace();
            log.error("Sending a post request is abnormal! ,head :{},body {},url {}", heads, body, url, e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            ResourceRelease.release(out);
            ResourceRelease.release(in);
        }
        return value;
    }

    /**
     * 判断ip端口是否可用
     *
     * @param host
     * @param port
     * @return
     */
    public static boolean isHostConnecTable(String host, int port) {
        TelnetClient telnetClient = new TelnetClient();
        boolean connected = false;
        try {
            telnetClient.connect(host, port);
            connected = telnetClient.isConnected();
        } catch (Exception e) {
            log.error("telnet connect error: connect to [{}:{}] fail!", host, port);
        }
        ResourceRelease.close(telnetClient);
        return connected;
    }

}