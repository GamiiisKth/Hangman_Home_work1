package server;

import clientServerHandler.ClientToServer;

/**
 * Created by joshuapro on 2015-11-19.
 */
public class Hangme{

public  Hangme(){}

    public  void calculate(String chosenWord, ServerToClient serverAnswer, ClientToServer clientAnswer){

        if(clientAnswer.getClientWord().isEmpty()){
            serverAnswer.setInfo("Guess a letter or Guess whole word");

            // client send ONLY a letter
        }else if (clientAnswer.getClientWord().length()==1){
            if(chosenWord.indexOf(clientAnswer.getClientWord())==-1){
                serverAnswer.setInfo("Letter does not exist");
                serverAnswer.setFailAttempts((serverAnswer.getFailAttempts()-1));
                if(serverAnswer.getFailAttempts()==0){
                    serverAnswer.setInfo("You lost!!!");
                    serverAnswer.setWord(new StringBuffer(chosenWord));
                    serverAnswer.setInfo( "Click on New Game for another chance!");

                }


            }

            char c [] = new char[clientAnswer.getClientWord().length()];
            c =clientAnswer.getClientWord().toCharArray();

            // if letter already guessed
            if((serverAnswer.getWord().toString().indexOf(clientAnswer.getClientWord()))!=-1){
                serverAnswer.setInfo("Letter has already been guessed");
            }else // find where letter is
            {
                for (int i =0; i < chosenWord.length();i++){
                    if(String.valueOf(chosenWord.charAt(i)).equalsIgnoreCase(String.valueOf(c[0]))){
                        serverAnswer.word.setCharAt(i, c[0]);

                        // ingen dash kvar, spelaren har gissat hela spelet
                        if(serverAnswer.getWord().indexOf("-")==-1){
                            serverAnswer.word=new StringBuffer(chosenWord);
                            serverAnswer.setInfo("You win!!!");
                         //   serverAnswer.setGames((serverAnswer.getGames()+1));
                            serverAnswer.setScore((serverAnswer.getScore()+1));

                            // fortfarande dash kvar
                        }else{
                            serverAnswer.setScore((serverAnswer.getScore()+1));

                            serverAnswer.setInfo("Letter correct");
                        }
                    }
                }

            }
            // klient har skickat hela ordet som är korrekt
        }else if(clientAnswer.getClientWord().equalsIgnoreCase(chosenWord)){
            serverAnswer.word= new StringBuffer(chosenWord);
            System.out.println("win word: " + serverAnswer.word);

            serverAnswer.setInfo("You win!!!");
            serverAnswer.setScore((serverAnswer.getScore()+1));
            //serverAnswer.setGames((serverAnswer.getGames()+1));

        }
        // klient har skickat fel ord
        else {
            serverAnswer.setInfo("You guessed wrong word!");


            serverAnswer.setFailAttempts((serverAnswer.getFailAttempts()-1));
            if(serverAnswer.getFailAttempts()==0){
                serverAnswer.setInfo("You lost!!!");
                serverAnswer.word=new StringBuffer(chosenWord);
                serverAnswer.setGames((serverAnswer.getGames()+1));
            }
        }

    }

}
