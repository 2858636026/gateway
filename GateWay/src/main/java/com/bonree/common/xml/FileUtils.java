package com.bonree.common.xml;

import java.io.*;

/**
 * Created by dell on 2018/3/26.
 */
public class FileUtils {

    public static String readConfigFile(String path) throws Exception {
        File file = new File(path);
        StringBuilder localStrBulider = new StringBuilder();
        if (file.isFile() && file.exists()) {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String lineStr = null;
            while ((lineStr = bufferReader.readLine()) != null) {
                localStrBulider.append(lineStr);
            }
            bufferReader.close();
            inputStreamReader.close();
        } else {
            throw new FileNotFoundException("file is not a file or file is not existing!");
        }
        return localStrBulider.toString();
    }
}
