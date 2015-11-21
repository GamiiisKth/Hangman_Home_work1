package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Random;

/**
 * Created by joshuapro on 2015-11-19.
 */
public class TextFile {

    // metoden tar emot en fil och retunerar antalet line i filen
    public int countLines(String fileName) throws IOException{

        int current = 0;
        LineNumberReader reader = new LineNumberReader(new FileReader(fileName));

        while(reader.readLine() != null){
            current++;
        }

        System.out.println(current);

        return current;
    }

    public String chooseWord(String file,int fileLenght){

        String word= "";

        Random genrateRandomNumber = new Random();
        int rndWord = genrateRandomNumber.nextInt(fileLenght);
        try {
            BufferedReader input=new BufferedReader(new FileReader(file));
            int cont=0;

            // read the file line by line until the random number has been chosen to word
            while ((cont < rndWord) && (word=input.readLine()) !=null){
                cont++;
            }
            // close the BufferedReader
            input.close();

        }catch (IOException e){
            System.out.println("can't read the file in chooseword method "+ e);
        }
        System.out.println(word);
    return word;
    }


        // metoden kommer att omvandla den valda ordet till understräcker
    public String dashWord(String word){
        int wordLenght=word.length();
        char dashTheWord[]= new char[wordLenght];

        for (int i=0; i < wordLenght; i++){
            dashTheWord[i]='-';
        }
        return new String(dashTheWord);
    }
}
