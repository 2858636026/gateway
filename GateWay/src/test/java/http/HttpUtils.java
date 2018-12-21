package http;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*******************************************************************************
 * 版权信息：博睿宏远科技发展有限公司 Copyright: Copyright (c) 2007博睿宏远科技发展有限公司,Inc.All Rights
 * Reserved.
 *
 * @date Jan 8, 2014 11:09:53 AM
 * @Author: <a href=mailto:zhangnl@bonree.com>张念礼</a>
 * @Description: 发送HTTP数据请求 Version: 1.0
 ******************************************************************************/
public enum HttpUtils {

    instance;

    private final static Logger log = LoggerFactory.getLogger(HttpUtils.class);

    private Integer maxRetry = 3;       // 发送HTTP请求失败重试次数
    private Integer connTimeOut = 3000; // 发送HTTP请求超时时间
    private Integer reqTimeOut = 1 * 60 * 1000;// 请求超时
    private Integer resTimeOut = 3 * 60 * 1000;// 响应传输数据超时
    private Long sleepTime = 5000l;     // 重新发送休眠时间

    /**
     * 概述：发送Get请求
     *
     * @param url 请求地址
     * @return byte[]
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public byte[] sendMsg(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);    // gett请求
        Builder config = RequestConfig.custom();
        config.setConnectTimeout(connTimeOut);  // 链接超时
        config.setSocketTimeout(resTimeOut);   // 数据传输超时
        config.setConnectionRequestTimeout(reqTimeOut);// 连接超时
        httpGet.setConfig(config.build());
        return send(httpGet);
    }

    /**
     * 概述：发送post请求(content)
     *
     * @param sendBytes 请求内容
     * @param url       请求地址
     * @return byte[]
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public byte[] sendMsg(byte[] sendBytes, String url) throws Exception {
        HttpPost httpPost = new HttpPost(url);// post请求
        if (sendBytes != null) {
            ByteArrayEntity params = new ByteArrayEntity(sendBytes);
            httpPost.setEntity(params);// 设置发送的数据
        }
        Builder config = RequestConfig.custom();
        config.setConnectTimeout(connTimeOut);  // 链接超时
        config.setSocketTimeout(resTimeOut);   // 数据传输超时
        config.setConnectionRequestTimeout(reqTimeOut);// 连接超时
        httpPost.setConfig(config.build());
        return send(httpPost);
    }

    /**
     * 概述：发送post请求(form表单)
     *
     * @param paramsMap 请求参数集合
     * @param url       请求地址
     * @return
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    public byte[] sendMsg(Map<String, String> paramsMap, String url) throws Exception {
        HttpPost httpPost = new HttpPost(url);// post请求
        if (paramsMap != null && !paramsMap.isEmpty()) {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            for (Entry<String, String> entry : paramsMap.entrySet()) {
                formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity params = new UrlEncodedFormEntity(formparams, "utf-8");
            httpPost.setEntity(params);// 设置发送的数据
        }
        Builder config = RequestConfig.custom();
        config.setConnectTimeout(connTimeOut);  // 链接超时
        config.setSocketTimeout(resTimeOut);   // 数据传输超时
        config.setConnectionRequestTimeout(reqTimeOut);// 连接超时
        httpPost.setConfig(config.build());
        httpPost.setHeader("", "");
        return send(httpPost);
    }

    /**
     * 概述：发送http请求
     *
     * @param request
     * @return byte[]
     * @throws Exception
     * @user <a href=mailto:zhangnl@bonree.com>张念礼</a>
     */
    private byte[] send(HttpUriRequest request) throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String errorInfo = "";
        for (int i = 1; i <= maxRetry; i++) {
            try {
                httpClient = HttpClients.createDefault();
                response = httpClient.execute(request);
                StatusLine status = response.getStatusLine();
                HttpEntity entity = response.getEntity();
                if (status.getStatusCode() == 200 && entity != null) {
                    // 请求返回结果,如果为"true"表示请求发送成功
                    return EntityUtils.toByteArray(entity);
                } else {
                    errorInfo = status.toString();
                    log.warn("Send data failed, will retry {}, status: {}", i, status);
                    Thread.sleep(sleepTime);
                }
            } catch (Exception e) {
                Thread.sleep(sleepTime);
                log.warn("Send data Exceptoin, will retry {}, status: {}", i, e);
                errorInfo = e.getMessage();
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (httpClient != null) {
                    try {
                        httpClient.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (errorInfo != null && !errorInfo.isEmpty()) {
            throw new Exception(errorInfo);
        }
        return null;
    }

    private static String queryDataSource = "{\n" +
            "    \"reqType\": \"metadata\", \n" +
            "    \"reqPara\": {\n" +
            "        \"operationType\": \"datasource\",\n" +
            "        \"operationPara\": {\n" +
            "            \"operationType\": \"queryDatasource\" ,\n" +
            "            \"operationPara\": {\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";

    private static String addDatasourceJson = "{\n" +
            "    \"reqType\": \"metadata\"," +
            "    \"reqPara\": {\n" +
            "        \"operationType\": \"dataSource\", " +
            "        \"operationPara\": {\n" +
            "            \"operationType\": \"addDataSource\"," +
            "            \"operationPara\": {\n" +
            "                \"dataSourceName\": \"mytest2\", " +
            "                \"granularity\": \"0\", " +
            "                \"engineType\": \"druid\", " +
            "                \"partitionNum\": 3, " +
            "                \"datasourceDesc\": \"first create\" " +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";

    public static void main(String[] args) throws Exception {

        String url = "http://192.168.3.242:8081/v1";
        Map<String, String> param = new HashMap<String, String>();
        param.put("username", "zach");
        param.put("password", "123");
        param.put("reqJson", queryDataSource);

        byte[] r = instance.sendMsg(param, url);
        String st = new String(r);
        System.out.println(st);
    }
}
