package ui;

import data.Call;
import data.DBHolder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class CallsWindow extends JFrame
{
    //region Singleton
    private static CallsWindow instance;
    //endregion
    JTable table;

    private CallsWindow()
    {
        super("Calls window");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 500);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (size.getWidth() / 2 - getWidth() / 2), (int) (size.getHeight() / 2 - getHeight() / 2));
        JPanel tablePanel = new JPanel(new BorderLayout());
        table = new JTable(new String[][]{}, new String[]{"Тип", "Дата", "Длительность", "Номер", "Комментрий"});
        tablePanel.add(table, BorderLayout.CENTER);
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
        add(tablePanel);
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
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        java.util.List<Call> calls = DBHolder.getInstance().getCalls();
        for (Call current : calls)
        {
            Vector row = new Vector();
            row.add(current.isIncoming() ? "Входящий" : "Исходящий");
            model.addRow(row);
        }
    }
}
