package server;

import java.io.Serializable;

/**
 * Created by joshuapro on 2015-11-19.
 */
public interface Task extends Serializable
{
    public void execute();
}