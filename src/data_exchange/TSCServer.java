package data_exchange;

import java.net.ServerSocket;
import java.util.List;

public class TSCServer implements Runnable
{
    private ServerSocket listener;
    private Thread manager, cleaner;
    private List<TSCClient> clients;
    private ClientAcceptedListener clientAcceptedListener;

    public void start()
    {
        if (manager == null || !manager.isAlive())
        {
            manager = new Thread(this);
            manager.start();
        }
        if (cleaner == null || !cleaner.isAlive())
        {
            cleaner = new Thread(() ->
            {
                //TODO:Implement me!
            });
            cleaner.start();
        }
    }

    @Override
    public void run()
    {
        //TODO:Implement me!
    }

    public void stop()
    {
        //TODO:Implement me!
    }

    public interface ClientAcceptedListener
    {
        void onClientAccepted(TSCClient client);
    }
}
