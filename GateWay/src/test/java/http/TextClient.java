package http;

import com.alibaba.fastjson.JSONObject;
import com.bonree.common.client.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class TextClient {
    public static void main(String[] args) throws InterruptedException {
//        String ip = "http://122.11.51.170:30106/";//http://122.11.51.170:30106/service

        String url = "http://127.0.0.1:81/v2.0";
//        String url = "http://192.168.101.86:80/v2.0";
        String fileName = "1.txt";
        String s = Text.readFile("C:\\workspace\\gateway\\GateWay\\src\\test\\java\\http", fileName);
        JSONObject jsonObject = JSONObject.parseObject(s, JSONObject.class);
        Map<String, String> paraMap = new HashMap<>();
        paraMap.put("username", "datasource");
        paraMap.put("token", "6ff49e4a00a5ac95");
        paraMap.put("serviceType", "datasource");
        paraMap.put("serviceName", "datasource_sdk_netPerformance");
        paraMap.put("paramsJson", jsonObject.toString());

        Map<String, Object> stringObjectMap = HttpClient.sendPost(url, null, paraMap, null);
        System.out.println(stringObjectMap);
    }
}