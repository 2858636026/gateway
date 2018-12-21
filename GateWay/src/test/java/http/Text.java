package http;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Text {

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
        } finally {
            Text.release(fileReader);
            Text.release(bufferedReader);
        }

        if (stringBuffer != null) {
            return stringBuffer.toString();
        }
        return null;
    }


    private static void release(FileReader fileReader) {
        if (fileReader != null) {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void release(BufferedReader fileReader) {
        if (fileReader != null) {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static HashMap<String, String> getDeruidJson(JSONObject jsonObject) {
        HashMap hashMap = new HashMap();
        hashMap.put("filter", jsonObject.getString("filter"));
        hashMap.put("intervals", jsonObject.getString("intervals"));
        hashMap.put("granularity", jsonObject.getString("granularity"));
        hashMap.put("context", jsonObject.getString("context"));
        hashMap.put("dataSource", jsonObject.getString("dataSource"));
        hashMap.put("aggregations", jsonObject.getString("aggregations"));
        hashMap.put("limitSpec", jsonObject.getString("limitSpec"));
        hashMap.put("queryType", jsonObject.getString("queryType"));
        hashMap.put("dimensions", jsonObject.getString("dimensions"));
        return hashMap;
    }
}
