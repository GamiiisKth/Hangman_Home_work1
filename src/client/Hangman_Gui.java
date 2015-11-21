package client;

import clientServerHandler.ClientServerHandler;
import clientServerHandler.ClientToServer;
import server.ServerToClient;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by joshuapro on 2015-11-18.
 */
/**
 * Gui for the reverse client. Used to submit string for reversal.
 */
public class Hangman_Gui extends JPanel implements ClientInterface
{
    private ClientToServer clientInput= new ClientToServer();
    ClientServerHandler clientHandler;
    private JButton connectButton;
    private JButton submitButton;
    private JButton disconnectButton;
    private JTextField wordField;
    private JTextField guessWord;
    private JTextField infoField;
    private  JButton  newGameButton;
    private JTextField gameValueFiled;
    private JTextField attemptValueFiled;
    private JTextField scoreValueFiled;
    private boolean uppdateTheGui=false;

    /**
     * Creates a new instance and builds the gui.
     */
    Hangman_Gui()
    {
        buildGui();

    }

    /**
     * The main method of the client. Starts the gui.
     *
     * @param args No command line parameters are used.
     */
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Hangman created by Ali Joshua");
        frame.setContentPane(new Hangman_Gui());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void buildGui()
    {
        setLayout(new GridLayout(5, 1));
        add(createConnectPanel());
        add(createWordPanel());
        add(createScorePanel());
        add(createInfoPanel());
    }

    /**
     ConnectPanel
     */
    private Component createConnectPanel()
    {
        JPanel connectPanel = new JPanel();
        connectPanel.setBorder(
                new TitledBorder(new EtchedBorder(), "Connection"));

        connectPanel.add(new JLabel("Host:"));
        final JTextField hostField = new JTextField("localhost");
        connectPanel.add(hostField);

        connectPanel.add(new JLabel("Port:"));
        final JTextField portField = new JTextField("5133");
        connectPanel.add(portField);


        connectButton = new JButton("Connect");
        connectPanel.add(connectButton);
        connectButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                String host=hostField.getText();
                int port = Integer.parseInt(portField.getText());
                connectButton.setEnabled(false);
                System.out.println(host+ "" +port);


                newGameButton.setEnabled(true);
                clientHandler= new ClientServerHandler(host,port,Hangman_Gui.this);
                new Thread(clientHandler).start();

            }
        });

        newGameButton= new JButton("New Game");
        connectPanel.add(newGameButton);
        newGameButton.setEnabled(false);
        //newGameButton.setAction(1);

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    uppdateTheGui=true;
             //   clientInput.setClientWord("");
                clientInput.setAction(2);


                clientHandler.setClientInput(clientInput);


            }
        });

        disconnectButton= new JButton("Disconnect");
        connectPanel.add(disconnectButton);
        disconnectButton.setEnabled(false);
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                clientInput.setAction(3);
                clientHandler.stop();
                clientHandler.setClientInput(clientInput);
                connectButton.setEnabled(true);
                disconnectButton.setEnabled(false);

            }
        });

        return connectPanel;
    }

    /**
     wordPanel
     * */
    private Component createWordPanel()
    {
        JPanel wordPanel = new JPanel();
        wordPanel.setBorder(new TitledBorder(new EtchedBorder(),
                "Word"));

        wordPanel.add(new JLabel("Guess:"));
        wordField = new JTextField(15);
        wordPanel.add(wordField);

        wordPanel.add(new JLabel("String to guess"));
        guessWord= new JTextField(15);
        wordPanel.add(guessWord);

        submitButton = new JButton("Guess");
        submitButton.setEnabled(false);
        wordPanel.add(submitButton);
        submitButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                String str= guessWord.getText().toString();
                clientInput.setClientWord(str);
                clientInput.setAction(1);
                clientHandler.setClientInput(clientInput);


            }
        });
        return wordPanel;
    }

    /**
     * score Panel
     * @return
     */
    private  Component createScorePanel(){
        JPanel scorePanel=new JPanel();

        scorePanel.setBorder(new TitledBorder(new EtchedBorder(),
                "Game state"));


        JLabel gameLable= new JLabel("game");
        scorePanel.add(gameLable);
        gameValueFiled=new JTextField(5);
        scorePanel.add(gameValueFiled);

        gameValueFiled.setEnabled(false);

        JLabel scoreLabel= new JLabel("score");
        scorePanel.add(scoreLabel);
        scoreValueFiled=new JTextField(5);
        scorePanel.add(scoreValueFiled);
        scoreValueFiled.setEnabled(false);

        JLabel attemptsLable= new JLabel("remaining attempts");
        scorePanel.add(attemptsLable);
        attemptValueFiled= new JTextField(5);
        scorePanel.add(attemptValueFiled);
        attemptValueFiled.setEnabled(false);


        return scorePanel;
    }

    /**
     * InfoPanel
     * @return infoPanel
     */
    private Component createInfoPanel(){
        JPanel infoPanel=new JPanel();
        infoPanel.setBorder(new TitledBorder(new EtchedBorder(),
                "Info"));
        infoField= new JTextField(25);
        infoField.setText("Game Info");
        infoPanel.add(infoField);
        infoField.setEnabled(false);
        return infoPanel;
    }

    /**
     * Callback method for the network layer. Should be invoked when
     * successfully connected to the server.
     */
 public void connected()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                disconnectButton.setEnabled(false);
                newGameButton.setEnabled(true);
                connectButton.setEnabled(false);
            }
        });
    }

    //method that updates the GUI with the server's response
    @Override
    public void setEnvironment(final ServerToClient serverToClient) {

        if (uppdateTheGui) {
            // kom ihåg att man behöver inte skapa en ny tråd för denna run metoden i clientHandler
            //denna run har egen tråd (swings egen tråd frt uppdatera gui) så denna tråd är alltid igång
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (clientHandler.getServerToClient().getInfo().equalsIgnoreCase("You win!!!")
                    || clientHandler.getServerToClient().getInfo().equalsIgnoreCase("You lost!!!")
                    || clientHandler.getServerToClient().getInfo().equalsIgnoreCase("Click on New Game for another chance!")){
                        submitButton.setEnabled(false);
                    }
                    submitButton.setEnabled(true);
                    wordField.setText((clientHandler.getServerToClient().getWord().toString()));
                    infoField.setText((clientHandler.getServerToClient()).getInfo());
                    gameValueFiled.setText(String.valueOf((clientHandler.getServerToClient()).getGames()));
                    scoreValueFiled.setText(String.valueOf((clientHandler.getServerToClient()).getScore()));
                    attemptValueFiled.setText(String.valueOf((clientHandler.getServerToClient()).getFailAttempts()));

                    //clear the input text field each time the client sends sthg
                    disconnectButton.setEnabled(true);
                    guessWord.setText("");
                    clientInput.setClientWord("");
                    clientInput.setAction(-1);

                }
            });

        }
    }


}
