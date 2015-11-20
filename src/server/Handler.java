package server;



import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by joshuapro on 2015-11-19.
 */
public class Handler extends Thread implements Serializable {

    private Socket clientSocket;
    protected String chosenWord;
    ServerToClient clientData= new ServerToClient(); // serverns svar till klient
    protected  String fileName;
    TextFile textFile;


    Handler (Socket socket,String fileName){
        // tråd konstruktor
        textFile= new TextFile();
        this.fileName=fileName;
        this.clientSocket=socket;

        //initizera serverToClient start värde
        clientData.setGames(0);
        clientData.setFailAttempts(0);
        clientData.setScore(0);
        clientData.setInfo("");



    }// slut på konstruktör

    @Override
    public void run() {
        try {
            boolean running=true;
            Object clientObject;
            ObjectInputStream in=null;
            ObjectOutputStream out=null;

            // skapa output Stream till skika objektet till klienten
            try {
                out= new ObjectOutputStream(clientSocket.getOutputStream());
            }catch (IOException e){
                System.out.println(e.toString());
            }
            try {
                // skapa the input stream att läsa det objekt som klient har skickat
                in= new ObjectInputStream(clientSocket.getInputStream());
            }catch (IOException e){
                System.out.println(e.toString());
            return;
            }

            while (running){
                // läs det objekt som klient har skickat
                try {
                    clientObject=in.readObject();
                }catch (ClassNotFoundException e){
                    System.out.println(e.toString());
                    return;
                }catch (OptionalDataException e){
                    System.out.println(e.toString());
                    return;
                }catch (IOException e){
                    System.out.println(e.toString());
                    return;
                }
                Hangme hangme= new Hangme(); // skapa hangme klass sådan att den implementerar hangme case
                if(clientObject instanceof ClientToServer){
                    if (((ClientToServer) clientObject).getAction()==1 ){ // send action
                        // implement the server's respons based on the client's action on the game
                        hangme.calculate(chosenWord,clientData,(ClientToServer)clientObject);

                    }else if (((ClientToServer)clientObject).getAction()==2){ // New Game
                        //Initiallize failattempts and delete previously viewed messages


                        clientData.setFailAttempts(10);
                       // clientData.setScore(0);
                           clientData.setInfo("");
                      clientData.setGames((clientData.getGames()+1));

                        //välj ett ny ord till klienten

                        try {
                            chosenWord=textFile.chooseWord(fileName,textFile.countLines(fileName));
                        }catch (IOException e ){
                            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, e);
                        }

                        clientData.setWord(new StringBuffer(textFile.dashWord(chosenWord)));

                    }else if (((ClientToServer) clientObject).getAction()==3){ // stopa klients running tråd i server sidan
                        running=false;
                    }
                }
                // send the server response to client
                try {

                    // The java.io.ObjectOutputStream.reset() method will disregard the state of any objects already written to the stream.
                    // The state is reset to be the same as a new ObjectOutputStream.
                    if (out != null) {
                        out.reset();
                    }

                    out.writeObject(clientData);
                    //  method flushes the stream. This will write any buffered output bytes and flush through to the underlying stream.
                    out.flush();

                }catch (IOException e){
                    System.out.println(e);
                }
            }
            //closes the streams if the server wants to stop running, if the running will be declarate to false
            System.out.println("Connection Closed");
            clientSocket.close();

        }catch (IOException e){
            System.err.println(e.toString());
        }
    }
}
