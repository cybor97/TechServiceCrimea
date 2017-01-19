package utils;

import ui.MainWindow;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GlobalLogger
{
    public static void log(Level level, String message)
    {
        Logger.getGlobal().log(level, message);
        MainWindow.log(message);
    }
}
