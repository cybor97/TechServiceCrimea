package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements ActionListener
{
    private final JPanel mainPanel;
    private final Button callsButton;
    private final Button departuresButton;
    private final Button exitButton;

    public MainWindow() throws Exception
    {
        super("Main window");
        setSize(100, 100);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (size.getWidth() / 2 - getWidth() / 2), (int) (size.getHeight() / 2 - getHeight() / 2));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new GridLayout(0, 1));

        callsButton = new Button("Вызовы");
        callsButton.addActionListener(this);
        mainPanel.add(callsButton);

        departuresButton = new Button("Выезды");
        departuresButton.addActionListener(this);
        mainPanel.add(departuresButton);

        exitButton = new Button("Выход");
        exitButton.addActionListener(this);
        mainPanel.add(exitButton);

        add(mainPanel);

        setVisible(true);

        Compiler.compileClass(CallsWindow.class);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source.getClass() == Button.class)
            if (source == callsButton)
                CallsWindow.getInstance().setVisible(true);
            else if (source == departuresButton)
                DeparturesWindow.getInstance().setVisible(true);
            else if (source == exitButton)
            {
                CallsWindow.getInstance().dispose();
                DeparturesWindow.getInstance().dispose();
                dispose();
            }
    }
}
