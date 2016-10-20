package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements ActionListener
{
    JSplitPane mainPane;
    Button callsButton, departuresButton, exitButton;

    public MainWindow() throws Exception
    {
        super("Main window");
        setSize(200, 100);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (size.getWidth() / 2 - getWidth() / 2), (int) (size.getHeight() / 2 - getHeight() / 2));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        add(mainPane);

        callsButton = new Button("Calls");
        callsButton.addActionListener(this);
        mainPane.add(callsButton);

        departuresButton = new Button("Departures");
        departuresButton.addActionListener(this);
        mainPane.add(departuresButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().getClass() == Button.class && e.getSource() == callsButton)
            CallsWindow.getInstance().setVisible(true);
    }
}
