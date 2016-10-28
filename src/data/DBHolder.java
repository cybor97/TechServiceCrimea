package data;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.sql.*;
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
                        results.getString(COMMENT)));
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
            Statement statement = connection.createStatement();
            statement.execute(String.format("INSERT INTO %1s VALUES (NULL, %2s, %3s, %4s, %5s,'%6s');",
                    CALLS,
                    call.getDate().getMillis(),
                    call.getDuration().getMillis(),
                    call.isIncoming() ? 1 : 0,
                    call.getPhoneNumber(),
                    call.getComment()));
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
            Statement statement = connection.createStatement();
            statement.execute(String.format("INSERT INTO %1s VALUES (NULL, %2s, %3s, '%4s', '%5s');",
                    DEPARTURES,
                    departure.getDate().getMillis(),
                    departure.getDuration().getMillis(),
                    departure.getAddress(),
                    departure.getResult()));
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
            Statement statement = connection.createStatement();
            statement.execute(String.format("UPDATE %1s " +
                            "SET %2s=%3s, %4s=%5s, %6s=%7s, %8s=%9s, %10s='%11s'" +
                            "WHERE ID = %12s;",
                    CALLS,

                    DATE,
                    call.getDate().getMillis(),

                    DURATION,
                    call.getDuration().getMillis(),

                    INCOMING,
                    call.isIncoming() ? 1 : 0,

                    PHONE_NUMBER,
                    call.getPhoneNumber(),

                    COMMENT,
                    call.getComment(),

                    call.getId()));
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
            Statement statement = connection.createStatement();
            statement.execute(String.format("UPDATE %1s " +
                            "SET %2s=%3s, %4s=%5s, %6s='%7s', %8s='%9s'" +
                            "WHERE ID = %10s;",
                    DEPARTURES,

                    DATE,
                    departure.getDate().getMillis(),

                    DURATION,
                    departure.getDuration().getMillis(),

                    ADDRESS,
                    departure.getAddress(),

                    RESULT,
                    departure.getResult(),

                    departure.getId()));
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
            statement.execute(String.format("DELETE FROM %1s WHERE %2s = %3d", CALLS, ID, id));
            statement.close();
        } catch (SQLException e)
        {
            System.err.println("DBHolder.removeCall()->\n" + id);
        }
    }
    //endregion
}
