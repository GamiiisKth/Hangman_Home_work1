package clientServerHandler;

import client.ClientInterface;
import client.Hangman_Gui;
import server.ClientToServer;
import server.Handler;
import server.ServerToClient;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by joshuapro on 2015-11-19.
 */
public class ClientServerHandler  implements Runnable, ClientInterface {


    private ClientToServer clientInput= new ClientToServer(); // the action of the client
    private ServerToClient serverToClient= new ServerToClient();
    private final LinkedBlockingQueue<Object> objects = new LinkedBlockingQueue<>();
    private boolean running=true;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    private Hangman_Gui gui;
    private String host;
    private int port;

    private Socket clientSocket = null;

    //Constructor
    public ClientServerHandler(String host, int port, Hangman_Gui gui) {

        this.gui = gui;
        this.port = port;
        this.host = host;

        connect();

        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        running = false;
    }
    void connect() {
        try {
            clientSocket = new Socket(host, port);

            gui.connected();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + host + ".");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "
                    + host + ".");
            System.exit(1);
        }
    }
    //sends the client's guess to the server and returns the response from server
   // @Override
    public void sendClientAnswer() {
        // send
        try {

            try {
              Object o = objects.take();
                out.writeObject(o);
   // efter man har skickat objektet så spola outPutStrem från den så den nästa gång man inte skickar föregående objectet igen
                out.reset();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            out.flush();

        } catch (IOException e) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, e);
        }
        // recive
        ServerToClient a = new ServerToClient();
        Object serverObject = null;
        try {
            serverObject = in.readObject();

        } catch (IOException e) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, e);

        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());

        }
        if (serverObject instanceof ServerToClient) {
            a = (ServerToClient) serverObject;
            setServerToClient(a);
        } else {
            System.out.println(serverObject);
            System.out.println("Not instance");
            System.out.println("Returning nothing");
        }

    }

    @Override
    public void run() {
        while (running){
            sendClientAnswer();
        setEnvironment(getServerToClient());
    }
    }
    @Override
    public void setEnvironment(ServerToClient serverToClient) {
        // man behöver inte skapa en ny tråd här, kolla metoden i Hangman_gui , denna metodens tråd är alltid igång det e
        // swings enda tråd som uppdaterar gui och man behöver inte skapa en gång
        gui.setEnvironment(serverToClient);
    }



    public void setClientInput(ClientToServer clientInput) {
        this.clientInput = clientInput;
        objects.add(clientInput);
    }

    public ServerToClient getServerToClient() {
        return serverToClient;
    }

    public void setServerToClient(ServerToClient serverToClient) {
        this.serverToClient = serverToClient;
    }
}
