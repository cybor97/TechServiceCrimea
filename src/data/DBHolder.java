package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBHolder
{
    public static final int NO_ID = -1;
    public static final String ID = "ID", DATE = "Date", DURATION = "Duration",

    CALL = "Call",
            INCOMING = "Incoming",
            PHONE_NUMBER = "PhoneNumber",
            COMMENT = "Comment",

    DEPARTURE = "Departure",
            ADDRESS = "Address",
            RESULT = "Result";
    //region Singleton pattern
    private DBHolder instance;
    //endregion
    private Connection connection;

    private DBHolder()
    {
        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:TSC.db");
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + CALL + "(" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DATE + " INTEGER," +
                    DURATION + " INTEGER," +
                    INCOMING + " INTEGER," +
                    PHONE_NUMBER + " INTEGER," +
                    COMMENT + " TEXT);");
            statement.execute("CREATE TABLE IF NOT EXISTS " + DEPARTURE + "(" +
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

    public DBHolder getInstance()
    {
        if (instance == null)
            instance = new DBHolder();
        return instance;
    }

    //region GET
    public List<Call> getCalls()
    {
        //TODO:Implement me!
        return null;
    }

    public List<Departure> getDepartures()
    {
        //TODO:Implement me!
        return null;
    }

    //endregion
    //region ADD
    public void addCall(Call call)
    {
        //TODO:Implement me!
    }

    public void addDeparture(Departure departure)
    {
        //TODO:Implement me!
    }

    //endregion
    //region SET
    public void setCall(Call call)
    {
        //TODO:Implement me!
    }

    public void setDeparture(Departure departure)
    {
        //TODO:Implement me!
    }

    //endregion
    //region REMOVE
    public void removeCall(Call call)
    {
        //TODO:Implement me!
    }

    public void removeDeparture(Departure departure)
    {
        //TODO:Implement me!
    }
    //endregion
}
