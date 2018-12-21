package client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonree.model.consts.ConfigurationParam;
import com.bonree.model.consts.ServerConsts;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class getData {
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        String connectionString = "mini1:2181,mini2:2181,mini3:2181";
        int sessionTimeout = 3000;
        System.out.println("获取连接");
        ZooKeeper zk = new ZooKeeper(connectionString, sessionTimeout,new Watcher(){
            @Override
            public void process(WatchedEvent event) {
                // TODO Auto-generated method stub
                System.out.println("触发");
            }
        });
        System.out.println("结束");
    }
}
