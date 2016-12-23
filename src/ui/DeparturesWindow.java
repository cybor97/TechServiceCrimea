package ui;

import data.DBHolder;
import data.Departure;
import org.joda.time.DateTime;
import org.joda.time.Period;
import utils.Utils;
import utils.Validator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import static data.DBHolder.NO_ID;
import static utils.Utils.dateFormatter;
import static utils.Utils.setButtons;

public class DeparturesWindow extends JFrame implements ActionListener, DocumentListener, ListSelectionListener, Validator
{
    private static final DeparturesWindow instance = new DeparturesWindow();
    private final JTable table;
    private final JTextArea dateArea;
    private final JTextArea durationArea;
    private final JTextArea addressArea;
    private final JTextArea resultArea;
    private final JButton addButton;
    private final JButton editButton;
    private final JButton removeButton;
    private final JButton updateJButton;
    private java.util.List<Departure> departures;

    private DeparturesWindow()
    {
        super("Выезды");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1100, 600);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (size.getWidth() / 2 - getWidth() / 2), (int) (size.getHeight() / 2 - getHeight() / 2));

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel tablePanel = new JPanel(new BorderLayout());
        table = new JTable(new DefaultTableModel(new String[][]{}, new String[]{"Дата", "Длительность", "Адрес", "Результат"})
        {
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        });
        table.getSelectionModel().addListSelectionListener(this);
        table.setCellSelectionEnabled(false);

        tablePanel.add(table, BorderLayout.CENTER);
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 0));

        JPanel datePanel = new JPanel(new GridLayout(0, 1));
        datePanel.add(new JLabel("Дата"));
        dateArea = new JTextArea();
        dateArea.setBorder(new NodeBorder(Color.gray));
        dateArea.getDocument().addDocumentListener(this);
        datePanel.add(dateArea);
        bottomPanel.add(datePanel);

        JPanel durationPanel = new JPanel(new GridLayout(0, 1));
        durationPanel.add(new JLabel("Длительность"));
        durationArea = new JTextArea();
        durationArea.setBorder(new NodeBorder(Color.gray));
        durationArea.getDocument().addDocumentListener(this);
        durationPanel.add(durationArea);
        bottomPanel.add(durationPanel);

        JPanel addressPanel = new JPanel(new GridLayout(0, 1));
        addressPanel.add(new JLabel("Адрес"));
        addressArea = new JTextArea();
        addressArea.setBorder(new NodeBorder(Color.gray));
        addressArea.getDocument().addDocumentListener(this);
        addressPanel.add(addressArea);
        bottomPanel.add(addressPanel);

        JPanel resultPanel = new JPanel(new GridLayout(0, 1));
        resultPanel.add(new JLabel("Результат"));
        resultArea = new JTextArea();
        resultArea.setBorder(new NodeBorder(Color.gray));
        resultArea.getDocument().addDocumentListener(this);
        resultPanel.add(resultArea);
        bottomPanel.add(resultPanel);

        JPanel JButtonsPanel = new JPanel(new GridLayout(2, 2));
        addButton = new JButton("Добавить");
        addButton.setEnabled(false);
        addButton.addActionListener(this);
        JButtonsPanel.add(addButton);
        updateJButton = new JButton("Обновить");
        updateJButton.addActionListener(this);
        JButtonsPanel.add(updateJButton);
        editButton = new JButton("Изменить");
        editButton.setEnabled(false);
        editButton.addActionListener(this);
        JButtonsPanel.add(editButton);
        removeButton = new JButton("Удалить");
        removeButton.setEnabled(false);
        removeButton.addActionListener(this);
        JButtonsPanel.add(removeButton);
        bottomPanel.add(JButtonsPanel);

        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        updateDisplayData();

    }

    public static DeparturesWindow getInstance()
    {
        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source == addButton)
            DBHolder.getInstance().addDeparture(new Departure(NO_ID,
                    DateTime.parse(dateArea.getText(), Utils.dateFormatter),
                    Period.parse(durationArea.getText(), Utils.periodFormatter).toStandardDuration(),
                    addressArea.getText(),
                    resultArea.getText()));
        else if (source == editButton)
        {
            int row = table.getSelectedRow();
            if (row > -1 && row < table.getRowCount())
            {
                Departure departure = departures.get(row);
                departure.setDate(DateTime.parse(dateArea.getText(), dateFormatter));
                departure.setDuration(Period.parse(durationArea.getText(), Utils.periodFormatter).toStandardDuration());
                departure.setAddress(addressArea.getText());
                departure.setResult(resultArea.getText());
                DBHolder.getInstance().setDeparture(departure);
            }

        } else if (source == removeButton)
            DBHolder.getInstance().removeDeparture(departures.get(table.getSelectedRow()));
        updateDisplayData();
    }

    public void updateDisplayData()
    {
        DefaultTableModel model = (DefaultTableModel) (table.getModel());
        model.getDataVector().clear();
        departures = DBHolder.getInstance().getDepartures();
        for (Departure current : departures)
        {
            Vector<String> row = new Vector<>();
            row.add(current.getDate().toString(dateFormatter));
            row.add(current.getDuration().toPeriod().toString(Utils.periodFormatter));
            row.add(current.getAddress());
            row.add(current.getResult());
            model.addRow(row);
        }
        model.fireTableDataChanged();
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        setButtons(this, table, addButton, editButton, removeButton);
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        setButtons(this, table, addButton, editButton, removeButton);
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
        setButtons(this, table, addButton, editButton, removeButton);
    }

    @Override
    public boolean validateValue()
    {
        try
        {
            return DateTime.parse(dateArea.getText(), dateFormatter) != null &&
                    Period.parse(durationArea.getText(), Utils.periodFormatter) != null;
        } catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        setButtons(this, table, addButton, editButton, removeButton);
        int row = table.getSelectedRow();
        if (row > -1 && row < table.getRowCount())
        {
            Departure departure = departures.get(row);
            dateArea.setText(departure.getDate().toString(Utils.dateFormatter));
            durationArea.setText(departure.getDuration().toPeriod().toString(Utils.periodFormatter));
            addressArea.setText(departure.getAddress());
            resultArea.setText(departure.getResult());
        }

    }
}
