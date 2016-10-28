package data;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class Departure
{
    private final int id;
    private final DateTime date;
    private final Duration duration;
    private final String address;
    private final String result;

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
