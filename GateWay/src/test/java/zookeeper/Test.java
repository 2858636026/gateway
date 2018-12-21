package zookeeper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        URL url;
        URLConnection conn=null;
        try {
            url = new URL("http://127.0.1.1");
            conn = url.openConnection();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Map headers = conn.getHeaderFields();
        Set<String> keys = headers.keySet();
        for( String key : keys ){
            String val = conn.getHeaderField(key);
            System.out.println(key+"    "+val);
        }

        System.out.println( conn.getLastModified() );
    }

}

