package ui;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import data.DBHolder;
import data.Departure;
import utils.Utils;

import static data.DBHolder.NO_ID;
import static utils.Utils.dateFormatter;

public class DeparturesWindow extends JFrame implements ActionListener, DocumentListener, ListSelectionListener
{
    private static final DeparturesWindow instance = new DeparturesWindow();
    private final JTable table;
    private final JTextArea dateArea;
    private final JTextArea durationArea;
    private final JTextArea addressArea;
    private final JTextArea resultArea;
    private final Button addButton;
    private final Button editButton;
    private final Button removeButton;
    private final Button updateButton;
    private java.util.List<Departure> departures;

    private DeparturesWindow()
    {
        super("Выезды");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 500);
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
        datePanel.add(new Label("Дата"));
        dateArea = new JTextArea();
        dateArea.setBorder(new NodeBorder(Color.gray));
        dateArea.getDocument().addDocumentListener(this);
        datePanel.add(dateArea);
        bottomPanel.add(datePanel);

        JPanel durationPanel = new JPanel(new GridLayout(0, 1));
        durationPanel.add(new Label("Длительность"));
        durationArea = new JTextArea();
        durationArea.setBorder(new NodeBorder(Color.gray));
        durationArea.getDocument().addDocumentListener(this);
        durationPanel.add(durationArea);
        bottomPanel.add(durationPanel);

        JPanel addressPanel = new JPanel(new GridLayout(0, 1));
        addressPanel.add(new Label("Адрес"));
        addressArea = new JTextArea();
        addressArea.setBorder(new NodeBorder(Color.gray));
        addressArea.getDocument().addDocumentListener(this);
        addressPanel.add(addressArea);
        bottomPanel.add(addressPanel);

        JPanel resultPanel = new JPanel(new GridLayout(0, 1));
        resultPanel.add(new Label("Результат"));
        resultArea = new JTextArea();
        resultArea.setBorder(new NodeBorder(Color.gray));
        resultArea.getDocument().addDocumentListener(this);
        resultPanel.add(resultArea);
        bottomPanel.add(resultPanel);

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
                    Period.parse(durationArea.getText(), Utils.periodFormatter) != null;
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
            Departure departure = departures.get(row);
            dateArea.setText(departure.getDate().toString(Utils.dateFormatter));
            durationArea.setText(departure.getDuration().toPeriod().toString(Utils.periodFormatter));
            addressArea.setText(departure.getAddress());
            resultArea.setText(departure.getResult());
        }

    }
}
