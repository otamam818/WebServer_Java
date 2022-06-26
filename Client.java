import java.net.*;
import java.io.*;

public class Client {
    private final static String server = "127.0.0.1";
    private final static int port = 8000;

    public static void main(String[] args) throws IOException {
        try {
            var s = new Socket(server, port);

            PrintStream out = new PrintStream(s.getOutputStream());
            out.println("GET /index.html HTTP/1.0");
            var in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String line = in.readLine();
            while (line != null) {
                System.out.println(line);
                line = in.readLine();
            }

            in.close();
            out.close();
            s.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

