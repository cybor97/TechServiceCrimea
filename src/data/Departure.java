package data;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class Departure
{
    private final int id;
    private DateTime date;
    private Duration duration;
    private String address;
    private String result;

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

    public void setDate(DateTime date)
    {
        this.date = date;
    }

    public Duration getDuration()
    {
        return duration;
    }

    public void setDuration(Duration duration)
    {
        this.duration = duration;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }
}
