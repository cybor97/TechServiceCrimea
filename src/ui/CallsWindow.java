package ui;

import data.Call;
import data.DBHolder;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import static data.DBHolder.NO_ID;
import static utils.Utils.dateFormatter;

public class CallsWindow extends JFrame implements ActionListener, DocumentListener, ListSelectionListener
{
    private static CallsWindow instance;
    private final JTable table;
    private final JComboBox<String> typeCB;
    private final JTextArea dateArea;
    private final JTextArea durationArea;
    private final JTextArea numberArea;
    private final JTextArea commentArea;
    private final Button addButton;
    private final Button editButton;
    private final Button removeButton;
    private final Button updateButton;
    private java.util.List<Call> calls;

    private CallsWindow()
    {
        super("Вызовы");

        calls = new ArrayList<>();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 500);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (size.getWidth() / 2 - getWidth() / 2), (int) (size.getHeight() / 2 - getHeight() / 2));

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel tablePanel = new JPanel(new BorderLayout());
        table = new JTable(new DefaultTableModel(new String[][]{}, new String[]{"Тип", "Дата", "Длительность", "Номер", "Комментрий"}));
        table.getSelectionModel().addListSelectionListener(this);
        table.setCellSelectionEnabled(false);
        tablePanel.add(table, BorderLayout.CENTER);
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 0));

        JPanel typePanel = new JPanel(new GridLayout(0, 1));
        typePanel.add(new Label("Тип"));
        typeCB = new JComboBox<>(new String[]{"Входящий", "Исходящий"});
        typeCB.setBorder(new NodeBorder(Color.gray));
        typePanel.add(typeCB);
        bottomPanel.add(typePanel);

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

        JPanel numberPanel = new JPanel(new GridLayout(0, 1));
        numberPanel.add(new Label("Номер"));
        numberArea = new JTextArea();
        numberArea.setBorder(new NodeBorder(Color.gray));
        numberArea.getDocument().addDocumentListener(this);
        numberPanel.add(numberArea);
        bottomPanel.add(numberPanel);

        JPanel commentPanel = new JPanel(new GridLayout(0, 1));
        commentPanel.add(new Label("Комментарий"));
        commentArea = new JTextArea();
        commentArea.setBorder(new NodeBorder(Color.gray));
        commentArea.getDocument().addDocumentListener(this);
        commentPanel.add(commentArea);
        bottomPanel.add(commentPanel);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2));
        addButton = new Button("Добавить");
        addButton.addActionListener(this);
        buttonsPanel.add(addButton);
        updateButton = new Button("Обновить");
        updateButton.addActionListener(this);
        buttonsPanel.add(updateButton);
        editButton = new Button("Изменить");
        editButton.addActionListener(this);
        buttonsPanel.add(editButton);
        removeButton = new Button("Удалить");
        removeButton.addActionListener(this);
        buttonsPanel.add(removeButton);
        bottomPanel.add(buttonsPanel);

        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setButtons();
        updateDisplayData();

    }

    public static CallsWindow getInstance()
    {
        if (instance == null)
            instance = new CallsWindow();
        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source == addButton)
            DBHolder.getInstance().addCall(new Call(NO_ID,
                    DateTime.parse(dateArea.getText(), dateFormatter),
                    Duration.standardMinutes(Long.parseLong(durationArea.getText())),
                    typeCB.getSelectedIndex() == 0,
                    Long.parseLong(numberArea.getText()),
                    commentArea.getText()));
        else if (source == removeButton)
            DBHolder.getInstance().removeCall(calls.get(table.getSelectedRow()));
        updateDisplayData();
    }

    private void updateDisplayData()
    {
        DefaultTableModel model = (DefaultTableModel) (table.getModel());
        model.getDataVector().clear();
        calls = DBHolder.getInstance().getCalls();
        for (Call current : calls)
        {
            Vector<String> row = new Vector<>();
            row.add(current.isIncoming() ? "Входящий" : "Исходящий");
            row.add(current.getDate().toString(dateFormatter));
            row.add(current.getDuration().getStandardMinutes() + " минут.");
            row.add("+" + current.getPhoneNumber());
            row.add(current.getComment());
            model.addRow(row);
        }
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
            long phoneNumber = Long.parseLong(numberArea.getText());
            return DateTime.parse(dateArea.getText(), dateFormatter) != null &&
                    Long.parseLong(durationArea.getText()) > 0 &&
                    phoneNumber > Long.parseLong("10000000000") &&
                    phoneNumber < Long.parseLong("99999999999");
        } catch (NumberFormatException e)
        {
            return false;
        } catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        setButtons();
        int row = table.getSelectedRow();
        if (row > -1 && row < table.getRowCount())
        {
            typeCB.setSelectedIndex(table.getValueAt(row, 0).toString().equals("Входящий") ? 0 : 1);
            dateArea.setText(table.getValueAt(row, 1).toString());
            durationArea.setText(table.getValueAt(row, 2).toString());
            numberArea.setText(table.getValueAt(row, 3).toString());
            commentArea.setText(table.getValueAt(row, 4).toString());
        }
    }
}
