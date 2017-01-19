package data_exchange;

import utils.GlobalLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import static java.util.logging.Level.WARNING;

public class TSCServer implements Runnable
{
    //region Singleton pattern
    private static TSCServer instance;
    //endregion
    private ServerSocket listener;
    private Thread manager, cleaner;
    private List<TSCClient> clients;
    private OnClientAcceptedListener onClientAcceptedListener;

    private TSCServer()
    {

    }

    public static TSCServer getInstance()
    {
        if (instance == null)
            instance = new TSCServer();
        return instance;
    }

    public void start()
    {
        if (clients == null)
            clients = new ArrayList<>();
        if (manager == null || !manager.isAlive())
        {
            manager = new Thread(this);
            manager.start();
        }
        if (cleaner == null || !cleaner.isAlive())
        {
            cleaner = new Thread(() ->
            {
                while (!Thread.interrupted())
                {
                    for (int i = 0; i < clients.size(); i++)
                    {
                        TSCClient client = clients.get(i);
                        if (client == null || !client.isConnected())
                        {
                            if (client != null)
                                client.stop();
                            clients.remove(i);
                        }
                    }
                    try
                    {
                        Thread.sleep(100);
                    } catch (InterruptedException e)
                    {
                        GlobalLogger.log(WARNING, "Interrupted while sleeping.");
                    }
                }
            });
            cleaner.setPriority(Thread.MIN_PRIORITY);
            cleaner.start();
        }
    }

    @Override
    public void run()
    {
        try
        {
            if (listener == null)
            {
                listener = new ServerSocket(12005);
                listener.setSoTimeout(100);//100ms
                while (!Thread.interrupted())
                    try
                    {
                        TSCClient client = new TSCClient(listener.accept());
                        client.start();
                        clients.add(client);
                        if (onClientAcceptedListener != null)
                            onClientAcceptedListener.onClientAccepted(client);
                    } catch (SocketTimeoutException e)
                    {
                        //System.err.println("TSCServer.{manager}.run(){main loop}->\n" + e.toString());
                    }
            }
        } catch (IOException e)
        {
            System.err.println("TSCServer.{manager}.run()->\n" + e.toString());
        }
    }

    public void stop()
    {
        try
        {
            if (listener != null)
                listener.close();
        } catch (IOException e)
        {
            System.err.println("TSCServer.stop()->\n" + e.toString());
        }
        if (manager != null)
        {
            manager.interrupt();
            manager = null;
        }
        if (cleaner != null)
        {
            cleaner.interrupt();
            cleaner = null;
        }
        if (clients != null)
        {
            for (TSCClient current : clients)
                current.stop();
            clients.clear();
        }
    }

    public void setOnClientAcceptedListener(OnClientAcceptedListener onClientAcceptedListener)
    {
        this.onClientAcceptedListener = onClientAcceptedListener;
    }

    public interface OnClientAcceptedListener
    {
        void onClientAccepted(TSCClient client);
    }
}
