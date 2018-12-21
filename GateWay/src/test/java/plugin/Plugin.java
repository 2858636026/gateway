package plugin;

import com.alibaba.fastjson.JSONObject;
import com.bonree.model.consts.ServerConsts;

import java.util.HashMap;
import java.util.Map;

public class Plugin {
    public static Map<String,Map<String,Object>> getData() {
        Map<String,Map<String,Object>> head = new HashMap<>();
        String headData = "{\"head\":{\"null\":\"HTTP/1.1 200 OK\", \"Connection\":\"keep-alive\", \"Content-Length\":115, \"Content-Type\":\"text/json;charset=utf-8\"}}";
        String bodyData = "{\"body\":{\"reason\":\"data\", \"code\":200}}";
        Map maphead = JSONObject.parseObject(headData, Map.class);
        Map mapbody = JSONObject.parseObject(bodyData, Map.class);
        head.put(ServerConsts.HEAD,maphead);
        head.put(ServerConsts.BODY,mapbody);
        return head;
    }

    public static void main(String[] args) {
        System.out.println(getData());
    }
}
