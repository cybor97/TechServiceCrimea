package ui;

import data_exchange.TSCServer;
import utils.GlobalLogger;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static java.util.logging.Level.WARNING;
import static javax.swing.JSplitPane.VERTICAL_SPLIT;
import static javax.swing.text.DefaultCaret.ALWAYS_UPDATE;

public class MainWindow extends JFrame implements ActionListener
{
    private static MainWindow instance;
    private final JButton callsButton;
    private final JButton departuresButton;
    private final JButton exitButton;
    private final ByteArrayOutputStream outputStream;
    private final JTextArea logArea;
    private Thread LogDisplayManager;

    private MainWindow() throws Exception
    {
        setSize(400, 300);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (size.getWidth() / 2 - getWidth() / 2), (int) (size.getHeight() / 2 - getHeight() / 2));

        JSplitPane mainPanel = new JSplitPane(VERTICAL_SPLIT);

        JPanel buttonsPanel = new JPanel(new GridLayout(0, 1));

        callsButton = new JButton("Вызовы");
        callsButton.addActionListener(this);
        buttonsPanel.add(callsButton);

        departuresButton = new JButton("Выезды");
        departuresButton.addActionListener(this);
        buttonsPanel.add(departuresButton);

        exitButton = new JButton("Выход");
        exitButton.addActionListener(this);
        buttonsPanel.add(exitButton);

        mainPanel.add(buttonsPanel);

        logArea = new JTextArea();
        logArea.setEditable(false);
        ((DefaultCaret) logArea.getCaret()).setUpdatePolicy(ALWAYS_UPDATE);
        mainPanel.add(new JScrollPane(logArea));

        mainPanel.setResizeWeight(0.1);

        add(mainPanel);

        setVisible(true);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                exit();
            }
        });

        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        (LogDisplayManager = new Thread(this::runLogDisplayManager)).start();
    }

    public static MainWindow getInstance() throws Exception
    {
        if (instance == null)
            instance = new MainWindow();
        return instance;
    }

    public static void log(String message)
    {
        if (instance != null)
            instance.logArea.append(message + "\n");
    }

    private void runLogDisplayManager()
    {
        while (!Thread.interrupted())
            try
            {
                String logString = new String(outputStream.toByteArray());
                if (!logString.trim().isEmpty())
                    log(logString);
                Thread.sleep(100);
            } catch (InterruptedException e)
            {
                GlobalLogger.log(WARNING, "MainWindow.runLogDisplayManager: Interrupted while sleeping!");
            }
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
