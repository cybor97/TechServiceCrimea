package data;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class Departure
{
    private int id;
    private DateTime date;
    private Duration duration;
    private String address, result;

    public Departure(int id, DateTime date, Duration duration, String address, String result)
    {
        this.id = id;
        this.date = date;
        this.duration = duration;
        this.address = address;
        this.result = result;
    }

    public int getId()
    {
        return id;
    }

    public DateTime getDate()
    {
        return date;
    }

    public Duration getDuration()
    {
        return duration;
    }

    public String getAddress()
    {
        return address;
    }

    public String getResult()
    {
        return result;
    }
}
