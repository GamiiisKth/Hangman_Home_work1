package server;

/**
 * Created by joshuapro on 2015-11-19.
 */
public class ClientToServer implements Task  {
    private static final long serialVersionUID = -7386258182412348265L;

    private int action;
    protected String clientWord;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getClientWord() {
        return clientWord;
    }

    public void setClientWord(String clientWord) {
        this.clientWord = clientWord;
    }

    @Override
    public void execute() {

    }
}
