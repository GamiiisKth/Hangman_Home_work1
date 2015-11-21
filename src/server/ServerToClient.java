package server;

/**
 * Created by joshuapro on 2015-11-19.
 */
public class ServerToClient implements Task {
    private static final long serialVersionUID = -7386258182412348165L;

    protected int failAttempts;
    protected int score;
    protected int games;
    protected String info;
    protected StringBuffer word;//dashes


    public StringBuffer getWord() {
        return word;
    }

    public void setWord(StringBuffer word) {
        this.word = word;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected String message;//replied message to the letter or word that client sends



    public int getFailAttempts() {
        return failAttempts;
    }

    public void setFailAttempts(int failAttempts) {
        this.failAttempts = failAttempts;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
