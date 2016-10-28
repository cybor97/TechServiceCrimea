package ui;

import org.joda.time.DateTime;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static utils.Utils.dateFormatter;

public class DeparturesWindow extends JFrame implements ActionListener, DocumentListener
{
    private static final DeparturesWindow instance = new DeparturesWindow();
    private final JTable table;
    private final JTextArea dateArea;
    private final JTextArea durationArea;
    private final JTextArea resultArea;
    private final JTextArea commentArea;
    private final Button addButton;
    private final Button editButton;
    private final Button removeButton;
    private final Button updateButton;

    private DeparturesWindow()
    {
        super("Выезды");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 500);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (size.getWidth() / 2 - getWidth() / 2), (int) (size.getHeight() / 2 - getHeight() / 2));

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel tablePanel = new JPanel(new BorderLayout());
        table = new JTable(new String[][]{}, new String[]{"Дата", "Длительность", "Результат", "Комментрий"});
        tablePanel.add(table, BorderLayout.CENTER);
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 0));

        JPanel datePanel = new JPanel(new GridLayout(0, 1));
        datePanel.add(new Label("Дата"));
        dateArea = new JTextArea();
        dateArea.setBorder(new NodeBorder(Color.gray));
        dateArea.getDocument().addDocumentListener(this);
        datePanel.add(dateArea);
        bottomPanel.add(datePanel);

        JPanel durationPanel = new JPanel(new GridLayout(0, 1));
        durationPanel.add(new Label("Длительность(минут)"));
        durationArea = new JTextArea();
        durationArea.setBorder(new NodeBorder(Color.gray));
        durationArea.getDocument().addDocumentListener(this);
        durationPanel.add(durationArea);
        bottomPanel.add(durationPanel);

        JPanel resultPanel = new JPanel(new GridLayout(0, 1));
        resultPanel.add(new Label("Результат"));
        resultArea = new JTextArea();
        resultArea.setBorder(new NodeBorder(Color.gray));
        resultArea.getDocument().addDocumentListener(this);
        resultPanel.add(resultArea);
        bottomPanel.add(resultPanel);

        JPanel commentPanel = new JPanel(new GridLayout(0, 1));
        commentPanel.add(new Label("Комментарий"));
        commentArea = new JTextArea();
        commentArea.setBorder(new NodeBorder(Color.gray));
        commentArea.getDocument().addDocumentListener(this);
        commentPanel.add(commentArea);
        bottomPanel.add(commentPanel);


        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2));
        addButton = new Button("Добавить");
        addButton.setEnabled(false);
        addButton.addActionListener(this);
        buttonsPanel.add(addButton);
        updateButton = new Button("Обновить");
        updateButton.addActionListener(this);
        buttonsPanel.add(updateButton);
        editButton = new Button("Изменить");
        editButton.setEnabled(false);
        editButton.addActionListener(this);
        buttonsPanel.add(editButton);
        removeButton = new Button("Удалить");
        removeButton.setEnabled(false);
        removeButton.addActionListener(this);
        buttonsPanel.add(removeButton);
        bottomPanel.add(buttonsPanel);

        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        updateDisplayData();

    }

    public static DeparturesWindow getInstance()
    {
        return instance;
    }

    private void updateDisplayData()
    {

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        setButtons();
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        setButtons();
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
        setButtons();
    }

    private void setButtons()
    {
        boolean validated = validateInput();
        boolean rowSelected = table.getSelectedRow() > -1;
        addButton.setEnabled(validated);
        editButton.setEnabled(validated && rowSelected);
        removeButton.setEnabled(rowSelected);
    }

    private boolean validateInput()
    {
        try
        {
            return DateTime.parse(dateArea.getText(), dateFormatter) != null &&
                    Double.parseDouble(durationArea.getText()) > 0;
        } catch (NumberFormatException e)
        {
            return false;
        } catch (IllegalArgumentException e)
        {
            return false;
        }
    }

}
