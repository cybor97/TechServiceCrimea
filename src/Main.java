import data.DBHolder;
import data_exchange.RequestProcessor;
import data_exchange.TSCServer;
import ui.CallsWindow;
import ui.DeparturesWindow;
import ui.MainWindow;

class Main
{
    public static void main(String[] args)
    {
        try
        {
            initServer();
            DBHolder.getInstance();
            new MainWindow();
        } catch (Exception e)
        {
            System.err.println(e.toString());
        }
    }

    private static void initServer()
    {
        TSCServer.getInstance().setOnClientAcceptedListener(client -> client.setOnRequestAcceptedListener((sender, request) ->
        {
            String result = RequestProcessor.process(request).toXML();
            CallsWindow.getInstance().updateDisplayData();
            DeparturesWindow.getInstance().updateDisplayData();
            return result;
        }));
        TSCServer.getInstance().start();
    }
}
