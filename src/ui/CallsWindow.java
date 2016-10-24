package ui;

import com.sun.deploy.panel.NodeBorder;
import data.Call;
import data.DBHolder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Vector;

public class CallsWindow extends JFrame
{
    //region Singleton
    private static CallsWindow instance;
    //endregion
    JTable table;
    JComboBox<String> typeCB;
    JTextArea dateArea, durationArea, numberArea, commentArea;
    Button addButton, editButton, removeButton, updateButton;

    private CallsWindow()
    {
        super("Вызовы");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 500);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (size.getWidth() / 2 - getWidth() / 2), (int) (size.getHeight() / 2 - getHeight() / 2));

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel tablePanel = new JPanel(new BorderLayout());
        table = new JTable(new String[][]{}, new String[]{"Тип", "Дата", "Длительность", "Номер", "Комментрий"});
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
        datePanel.add(dateArea);
        bottomPanel.add(datePanel);

        JPanel durationPanel = new JPanel(new GridLayout(0, 1));
        durationPanel.add(new Label("Длительность(минут)"));
        durationArea = new JTextArea();
        durationArea.setBorder(new NodeBorder(Color.gray));
        durationPanel.add(durationArea);
        bottomPanel.add(durationPanel);

        JPanel numberPanel = new JPanel(new GridLayout(0, 1));
        numberPanel.add(new Label("Номер"));
        numberArea = new JTextArea();
        numberArea.setBorder(new NodeBorder(Color.gray));
        numberPanel.add(numberArea);
        bottomPanel.add(numberPanel);

        JPanel commentPanel = new JPanel(new GridLayout(0, 1));
        commentPanel.add(new Label("Комментарий"));
        commentArea = new JTextArea();
        commentArea.setBorder(new NodeBorder(Color.gray));
        commentPanel.add(commentArea);
        bottomPanel.add(commentPanel);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2));
        addButton = new Button("Добавить");
        buttonsPanel.add(addButton);
        updateButton = new Button("Обновить");
        buttonsPanel.add(updateButton);
        editButton = new Button("Изменить");
        buttonsPanel.add(editButton);
        removeButton = new Button("Удалить");
        buttonsPanel.add(removeButton);
        bottomPanel.add(buttonsPanel);

        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        updateDisplayData();
    }

    public static CallsWindow getInstance()
    {
        if (instance == null)
            instance = new CallsWindow();
        return instance;
    }

    private void updateDisplayData()
    {
        TableModel currentModel = table.getModel();
        table.setModel(new DefaultTableModel());
        DefaultTableModel model = (DefaultTableModel) (table.getModel());
        java.util.List<Call> calls = DBHolder.getInstance().getCalls();
        for (Call current : calls)
        {
            Vector<String> row = new Vector<>();
            row.add(current.isIncoming() ? "Входящий" : "Исходящий");
            row.add(current.getDate().toString());
            row.add(current.getDuration().toString());
            row.add(Long.toString(current.getPhoneNumber()));
            row.add(current.getComment());
            model.addRow(row);
        }
        table.setModel(currentModel);
    }
}
