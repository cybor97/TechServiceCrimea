package data;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DBHolder
{
    public static final int NO_ID = -1;
    public static final String ID = "ID", DATE = "Date", DURATION = "Duration",

    CALLS = "Calls",
            INCOMING = "Incoming",
            PHONE_NUMBER = "PhoneNumber",
            COMMENT = "Comment",

    DEPARTURES = "Departures",
            ADDRESS = "Address",
            RESULT = "Result";
    //region Singleton pattern
    private static DBHolder instance;
    private Connection connection;

    private DBHolder()
    {
        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:TSC.db");
            connection.setAutoCommit(true);
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + CALLS + "(" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DATE + " INTEGER," +
                    DURATION + " INTEGER," +
                    INCOMING + " INTEGER," +
                    PHONE_NUMBER + " INTEGER," +
                    COMMENT + " TEXT);");
            statement.execute("CREATE TABLE IF NOT EXISTS " + DEPARTURES + "(" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DATE + " INTEGER," +
                    DURATION + " INTEGER," +
                    ADDRESS + " TEXT," +
                    RESULT + " TEXT);");
            statement.close();
        } catch (SQLException e)
        {
            System.err.println(e.getMessage() + "\n" + e.toString());
        }
    }

    public static DBHolder getInstance()
    {
        if (instance == null)
            instance = new DBHolder();
        return instance;
    }

    //endregion

    //region GET
    public List<Call> getCalls()
    {
        try
        {
            List<Call> result = new ArrayList<>();
            Statement statement = connection.createStatement();//This one is NEEDED. We should close it before return;
            ResultSet results = statement.executeQuery("SELECT * FROM " + CALLS);
            while (results.next())
                result.add(new Call(results.getInt(ID),
                        new DateTime(results.getLong(DATE)),
                        new Duration(results.getLong(DURATION)),
                        results.getBoolean(INCOMING),
                        results.getLong(PHONE_NUMBER),
                        results.getString(COMMENT)));
            statement.close();
            return result;
        } catch (SQLException e)
        {
            System.err.println("DBHolder.getCalls()->\n" + e.toString());
            return null;
        }
    }

    public List<Departure> getDepartures()
    {
        try
        {
            List<Departure> result = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM " + DEPARTURES);
            while (results.next())
                result.add(new Departure(results.getInt(ID),
                        new DateTime(results.getLong(DATE)),
                        new Duration(results.getLong(DURATION)),
                        results.getString(ADDRESS),
                        results.getString(RESULT)));
            statement.close();
            return result;
        } catch (SQLException e)
        {
            System.err.println("DBHolder.getDepartures()->\n" + e.toString());
            return null;
        }
    }

    //endregion
    //region ADD
    public void addCall(Call call)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO " + CALLS + " VALUES(?, ?, ?, ?, ?, ?)");
            statement.setNull(1, Types.ROWID);
            statement.setLong(2, call.getDate().getMillis());
            statement.setLong(3, call.getDuration().getMillis());
            statement.setInt(4, call.isIncoming() ? 1 : 0);
            statement.setLong(5, call.getPhoneNumber());
            statement.setString(6, call.getComment());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e)
        {
            System.err.println("DBHolder.addCall()->\n" + e.toString());
        }
    }

    public void addDeparture(Departure departure)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO " + DEPARTURES + " VALUES (?, ?, ?, ?, ?);");
            statement.setNull(1, Types.ROWID);
            statement.setLong(2, departure.getDate().getMillis());
            statement.setLong(3, departure.getDuration().getMillis());
            statement.setString(4, departure.getAddress());
            statement.setString(5, departure.getResult());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e)
        {
            System.err.println("DBHolder.addDeparture()->\n" + e.toString());
        }
    }

    //endregion
    //region SET
    public void setCall(Call call)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("UPDATE " + CALLS + " SET " +
                    DATE + "=?," +
                    DURATION + "=?," +
                    INCOMING + "=?," +
                    PHONE_NUMBER + "=?," +
                    COMMENT + "=?" +
                    " WHERE " + ID + " =?;");

            statement.setLong(1, call.getDate().getMillis());
            statement.setLong(2, call.getDuration().getMillis());
            statement.setInt(3, call.isIncoming() ? 1 : 0);
            statement.setLong(4, call.getPhoneNumber());
            statement.setString(5, call.getComment());
            statement.setLong(6, call.getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e)
        {
            System.err.println("DBHolder.setCall()->\n" + e.toString());
        }
    }

    public void setDeparture(Departure departure)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("UPDATE " + DEPARTURES + " SET " +
                    DATE + "=?," +
                    DURATION + "=?," +
                    ADDRESS + "=?," +
                    RESULT + "=?" +
                    " WHERE " + ID + " =?;");

            statement.setLong(1, departure.getDate().getMillis());
            statement.setLong(2, departure.getDuration().getMillis());
            statement.setString(3, departure.getAddress());
            statement.setString(4, departure.getResult());
            statement.setLong(5, departure.getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e)
        {
            System.err.println("DBHolder.setDeparture()->\n" + e.toString());
        }
    }

    //endregion
    //region REMOVE
    public void removeCall(Call call)
    {
        remove(CALLS, call.getId());
    }

    public void removeDeparture(Departure departure)
    {
        remove(DEPARTURES, departure.getId());
    }

    private void remove(String tableName, int id)
    {
        try
        {
            Statement statement = connection.createStatement();
            statement.execute(String.format("DELETE FROM %1s WHERE %2s = %3d", tableName, ID, id));
            statement.close();
        } catch (SQLException e)
        {
            System.err.println("DBHolder.removeCall()->\n" + id);
        }
    }
    //endregion
}
