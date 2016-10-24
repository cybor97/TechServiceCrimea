package ui;

import javax.swing.*;

public class DeparturesWindow extends JFrame
{
    private static DeparturesWindow instance = new DeparturesWindow();

    private DeparturesWindow()
    {
        super("Выезды");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 350);
        setLocationByPlatform(true);
    }

    public static DeparturesWindow getInstance()
    {
        return instance;
    }
}
