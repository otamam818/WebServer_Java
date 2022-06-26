package ServerRequest;
import java.io.*;
import java.util.Scanner;
import java.util.Date;

public class GETRequest {
    private final String CRLF = "\r\n";

    private String URI;
    private boolean isNonBinary;
    private InputStream data;
    private long dataSize;
    private boolean fileExists;
    private String MIMEtype;

    public GETRequest(String URI) {
        assert (URI.startsWith("/"));
        this.URI = URI;
        this.isNonBinary = checkBinaryStatus();
        this.dataSize = 0;
        checkFileExistence();
        if (isNonBinary) {
            this.data = getNonBinary();
            this.binaryData = null;
        }
        else {
            try {
                this.binaryData = getBinary();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.MIMEtype = findMIMEtype();
    }

    private boolean checkBinaryStatus() {
        String[] validExtensions = {
            ".java",
            ".py",
            ".html",
            ".sh",
            ".c",
            ".css"
        };
        boolean finBool = false;
        for (String s : validExtensions) {
            finBool = finBool || URI.endsWith(s);
        }
        return finBool;
    }

    private String findMIMEtype() {
        return
        this.URI.endsWith(".html") ? "text/html" :
        this.URI.endsWith(".js") ? "text/javascript" :
        this.URI.endsWith(".css") ? "text/css" :
        this.URI.endsWith(".json") ? "application/json" :
        "application/octet-stream";
    }

    // private ByteStream getData(int BufferSize);

    /** Uses the URI to get the Non-Binary content
     *  @return The string that contains all the characters of the file
     */
    private String getNonBinary() {
        // Omit the '/' character
        if (!this.fileExists)
            return null;
        String finString = "";
        String path = URI.substring(1);
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                finString += scanner.nextLine() + "\n";
                this.dataSize++;
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return finString;
    }

    private void checkFileExistence() {
        String path = URI.substring(1);
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            // If it can scan it properly, the file exists
            this.fileExists = true;
            while (scanner.hasNextLine()) {
                finString += scanner.nextLine() + "\n";
                this.dataSize++;
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            this.fileExists = false;
        }
    }

    public InputStream getBinary() {
        try {
            return new FileInputStream(URI);
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    public String getHeader() {
        String response = fileExists ? "200 OK" : "400 Not Found";
        String statusLine = "HTTP/1.0 " + response + CRLF;
        String date = "Date: " + getDate() + CRLF;
        String contentLength = "Content-Length: " + dataSize + CRLF;
        String MIME = "Content-type: " + MIMEtype + CRLF;

        return
            statusLine
            + date
            + contentLength
            + MIME
            + CRLF;
    }

    private static String getDate() {
        String[] dayVars = (new Date()).toString().split(" ");

        String day = dayVars[0];
        String month = dayVars[1];
        String dayOfMonth = dayVars[2];
        String time = dayVars[3];
        String zone = dayVars[4];
        String year = dayVars[5];

        // System.out.println("Day: " + dayVars);
        String finStr = String.format("%s, %s %s %s %s %s",
            day,
            dayOfMonth,
            month,
            year,
            time,
            zone
        );
        return finStr;
    }
}

