package client;

import com.bonree.common.client.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class TextClient {
    public static void main(String[] args) throws InterruptedException {
//        String ip = "http://122.11.51.170:30106/";//http://122.11.51.170:30106/service
        String pathUrlzhuce ="http://127.0.0.1:80/service";
//        String pathUrlzhuce = "http://192.168.101.86:81/service";
//        String clientUrl = "http://127.0.0.1:81/v2.0";
        zhuce(pathUrlzhuce);
//        for (int i = 0; i < 1; i++) {
//            ThreadExec(pathUrlplugin);
//        }
//        client(clientUrl);
    }

    private static void client(String pathUrl) {
        Map<String, String> body = new HashMap<>();
        Map<String, String> head = new HashMap<>();
        head.put("serviceType", "datasource");
        head.put("username", "datasource");
        head.put("token", "6ff49e4a00a5ac95");
        head.put("serviceName", "datasource.qwe");
        head.put("Origin", "chrome-extension://geobniafmelcledgickglbajofpkllpl");
        head.put("Accept", "*/*");
        head.put("Connection", "keep-alive");
        head.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36");
        head.put("Host", "192.168.101.86");
        head.put("Accept-Encoding", "gzip");
        head.put("Accept-Language", "Accept-Language");
        body.put("ip", "192.168.102.9");
        body.put("paramsJson", "{\"fields\":[\"appid\",\"domain\"],\"filters\":[{\"fieldName\":\"type\",\"CompareType\":\"=\",\"value\":1},{\"fieldName\":\"resource_type\",\"CompareType\":\"=\",\"value\":1}],\"timeRange\":\"20180821100000/20180822100000\",\"primaryFilter\":{\"fieldName\":\"appid\",\"CompareType\":\"=\",\"value\":\"11393\"},\"groupby\":[\"appid\",\"domain\"],\"privateParams\":{}}");
        Map<String, Object> stringMapMap = HttpClient.sendPost(pathUrl, head, body, null);
        System.out.println(stringMapMap);
    }

    private static void zhuce(String pathUrl) {
        /**
         * 测试主方法
         * @param args
         */
        String Array = "[{" +
                "        \"serviceType\":\"query\"," +
                "        \"serviceName\":\"datasource.crash\"," +
                "        \"cacheTimeMs\":1241241241" +
                "},{" +
                "        \"serviceType\":\"datasource\"," +
                "        \"serviceName\":\"datasource.qwe\"," +
                "        \"cacheTimeMs\":14241241241" +
                "}]";
        Map<String, String> body = new HashMap<String, String>();
        body.put("flag", "0");
        body.put("token", "1234567");
        body.put("service", "sdk");
        body.put("tps", "{\"maxServicesNumber\":1000}");
        body.put("ip", "119.75.216.20");
        body.put("port", "80");
        body.put("dataSourceList", Array);
        Map<String, String> head = new HashMap<>();
        Map<String, Object> stringMapMap = HttpClient.sendPost(pathUrl, head, body, null);
        System.out.println(stringMapMap);
    }


    public static void ThreadExec(String path) {
        client(path);
    }
}