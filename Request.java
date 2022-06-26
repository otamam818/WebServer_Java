import java.io.*;
import ServerRequest.GETRequest;

public class Request {
    // We want a way for the state of the RequestHandler to change,
    // so that we can map it to different functions
    private enum State {
        // For when processing the request just begins
        START,

        // For when the request is parsing the current line
        IN_REQUEST,

        // For when the request is finished parsing
        COMPLETED
    }
    private State currState;
    private String URI;
    private String currLine;
    private BufferedReader reader;
    private String header;

    public Request(BufferedReader reader) {
        this.currState = State.START;
        this.reader = reader;
        try {
            this.run();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getHeader() {
        return this.header;
    }

    /** Run the request
     *  @return A completed request which has a proper toString output
     */
    private void run() throws IOException {
        while (this.currState != State.COMPLETED) {
            // Do the operation for the associated state, store a return
            // variable
            switch (this.currState) {
                case START:      parseStart();
                                 break;
                case IN_REQUEST: parseInRequest();
                                 break;
                case COMPLETED:  // Nothing to do here
                                 break;
            }

            // Change the state based on the return variable
        }
    }

    private void parseStart() throws IOException {
        currLine = reader.readLine();
        if (currLine.startsWith("GET")) {
            URI = currLine.substring(
                "GET ".length(),
                currLine.indexOf(" HTTP/")
            );
            GETRequest greq = new GETRequest(URI);
            // System.out.println(greq.getHeader());
            this.header = greq.getHeader();
        }
        this.currState = State.IN_REQUEST;
    }

    private void parseInRequest() {
        this.currState = State.COMPLETED;
    }

    public void sendBinary() {
    }
}

