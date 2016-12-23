package ui;

import data_exchange.TSCServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame implements ActionListener
{
    private final JPanel mainPanel;
    private final JButton callsButton;
    private final JButton departuresButton;
    private final JButton exitButton;

    public MainWindow() throws Exception
    {
        super("Main window");
        setSize(200, 100);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (size.getWidth() / 2 - getWidth() / 2), (int) (size.getHeight() / 2 - getHeight() / 2));

        mainPanel = new JPanel(new GridLayout(0, 1));

        callsButton = new JButton("Вызовы");
        callsButton.addActionListener(this);
        mainPanel.add(callsButton);

        departuresButton = new JButton("Выезды");
        departuresButton.addActionListener(this);
        mainPanel.add(departuresButton);

        exitButton = new JButton("Выход");
        exitButton.addActionListener(this);
        mainPanel.add(exitButton);

        add(mainPanel);

        setVisible(true);

        Compiler.compileClass(CallsWindow.class);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                exit();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source.getClass() == JButton.class)
            if (source == callsButton)
                CallsWindow.getInstance().setVisible(true);
            else if (source == departuresButton)
                DeparturesWindow.getInstance().setVisible(true);
            else if (source == exitButton)
            {
                CallsWindow.getInstance().dispose();
                DeparturesWindow.getInstance().dispose();
                exit();
            }
    }

    private void exit()
    {
        TSCServer.getInstance().stop();
        dispose();
        System.exit(0);
    }
}
