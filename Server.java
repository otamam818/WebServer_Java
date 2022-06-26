import java.net.*;
import java.io.*;

public class Server {
    private final static int port = 8000;

    public static void main(String[] args) throws IOException {
        var ss = new ServerSocket(port);
        Socket s = ss.accept();

        // Add the socket into the input stream
        var in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        Request req = new Request(in);

        var out = new PrintWriter(s.getOutputStream());
        out.println(req.getHeader());
        out.flush();
        ss.close();
    }
}

