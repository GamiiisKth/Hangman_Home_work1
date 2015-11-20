package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by joshuapro on 2015-11-19.
 */
public class Server  {
    static final String USAGE = "java Server [hostname] [port] ";
    static String filename = "/Users/joshuapro/IdeaProjects/Hangman_Home_work1/src/server/words.txt";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        int port = 5133;
        String host = "localhost";
        boolean listening = true;
        if (args.length > 1) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println(USAGE);
                System.exit(0);
            }
            host=args[0];

            if (args[0].equalsIgnoreCase("-h") || args[0].equalsIgnoreCase("-help")) {
                System.out.println(USAGE);
                System.exit(1);
            }

        }

        try {

            //create an IP address and the server's socket to this address and port 5133
            InetAddress addr = InetAddress.getByName(host);
            ServerSocket serversocket = new ServerSocket(port, 1000, addr);

            while (listening) {    // the main server's loop
                System.out.println("Listening to connections...");
                Socket clientsocket = serversocket.accept();

                //creates a thread in the connection socket that accepted the client
               new Thread(new Handler(clientsocket, filename)).start();

            }
            serversocket.close();
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }

    }
}
