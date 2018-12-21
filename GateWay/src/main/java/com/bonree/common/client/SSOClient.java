package com.bonree.common.client;

import com.alibaba.fastjson.JSONObject;
import com.bonree.common.util.ResourceRelease;
import com.bonree.model.consts.ServerConsts;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SSOClient {
    private static Logger log = LoggerFactory.getLogger(SSOClient.class);

    public static Map<String, Object> sendPost(String url, Map<String, String> heads, Map<String, String> body) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder res = new StringBuilder();
        Map<String, Object> value = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
            conn.setRequestProperty(HTTP.CONTENT_TYPE, ServerConsts.CONTENT_TYPE_VALUE);
            conn.setRequestProperty(HTTP.USER_AGENT, ServerConsts.USER_AGENT_VALUE);
            conn.setConnectTimeout(10000);
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
                    if (val != null && !"null".equals(val)) {
                        param = new StringBuilder(param).append(key).append("=").append(val).append("&").toString();
                    } else {
                        throw new Exception(new StringBuilder(val).append("is null").toString());
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
                res.append(line);
            }
            Map head = conn.getHeaderFields();
            Set<String> keys = head.keySet();
            JSONObject headData = new JSONObject();
            for (String key : keys) {
                headData.put(key, conn.getHeaderField(key));
            }
            JSONObject jsonObject;
            try {
                jsonObject = JSONObject.parseObject(res.toString(), JSONObject.class);
            } catch (Exception e) {
                log.error("Response content is abnormal: {},{}", res.toString(), e);
                return null;
            }
            JSONObject bodyData = new JSONObject();
            bodyData.put(ServerConsts.TYPE, jsonObject.getString(ServerConsts.TYPE));
            bodyData.put(ServerConsts.ERRORCODE, jsonObject.getInteger(ServerConsts.ERRORCODE));
            bodyData.put(ServerConsts.REASON, jsonObject.getString(ServerConsts.REASON));
            bodyData.put(ServerConsts.RESULT, jsonObject.getString(ServerConsts.RESULT));
            value = new HashMap<>();
            value.put(ServerConsts.HEAD, headData);
            value.put(ServerConsts.BODY, bodyData);

        } catch (Exception e) {
            log.error("Sending a post request is abnormal! {}", e);
        } finally {
            ResourceRelease.release(out);
            ResourceRelease.release(in);
        }
        return value;
    }
}
