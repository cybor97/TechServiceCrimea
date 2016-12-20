package data;

import com.sun.istack.internal.NotNull;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class DBHolder
{
    public static final int NO_ID = -1;

    //region Singleton pattern
    private static DBHolder instance;
    private static Session session;

    private DBHolder()
    {
        Configuration configuration = new Configuration().configure();
        if (!new File("TSC.db").exists())
            configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        session = configuration.buildSessionFactory().openSession();
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
        return session.createQuery("from Call ", Call.class).list();
    }

    public List<Departure> getDepartures()
    {
        return session.createQuery("from Departure ", Departure.class).list();
    }

    //endregion
    //region ADD
    public void addCall(Call call)
    {
        executeTransaction(() -> session.save(call));
    }

    public void addDeparture(Departure departure)
    {
        executeTransaction(() -> session.save(departure));
    }

    //endregion
    //region SET
    public void setCall(Call call)
    {
        executeTransaction(() -> session.update(call));
    }

    public void setDeparture(Departure departure)
    {
        executeTransaction(() -> session.update(departure));
    }

    //endregion
    //region REMOVE
    public void removeCall(Call call)
    {
        executeTransaction(() -> session.remove(call));
    }

    public void removeDeparture(Departure departure)
    {
        executeTransaction(() -> session.remove(departure));
    }

    //endregion
    //region Tools
    private void executeTransaction(@NotNull Runnable runnable)
    {
        session.beginTransaction();
        runnable.run();
        session.getTransaction().commit();
    }
    //endregion
}
