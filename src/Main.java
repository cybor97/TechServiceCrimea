import data_exchange.Request;
import data_exchange.RequestProcessor;
import data_exchange.TSCClient;
import data_exchange.TSCServer;
import ui.CallsWindow;
import ui.DeparturesWindow;
import ui.MainWindow;

class Main implements TSCServer.OnClientAcceptedListener, TSCClient.OnRequestAcceptedListener
{
    public static void main(String[] args)
    {
        try
        {
            new MainWindow();
        } catch (Exception e)
        {
            System.err.println(e.toString());
        }
    }

    @Override
    public void onClientAccepted(TSCClient client)
    {
        client.setOnRequestAcceptedListener(this);
    }

    @Override
    public String onRequestAccepted(TSCClient sender, Request request)
    {
        String result = RequestProcessor.process(request).toXML();
        CallsWindow.getInstance().updateDisplayData();
        DeparturesWindow.getInstance().updateDisplayData();
        return result;
    }
}
