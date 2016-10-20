import ui.MainWindow;

public class Main
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
}
