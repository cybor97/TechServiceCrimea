package data;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class Call
{
    private int id;
    private DateTime date;
    private Duration duration;
    private boolean incoming;
    private long phoneNumber;
    private String comment;

    public Call(int id, DateTime date, Duration duration, boolean incoming, long phoneNumber, String comment)
    {
        this.id = id;
        this.date = date;
        this.duration = duration;
        this.incoming = incoming;
        this.phoneNumber = phoneNumber;
        this.comment = comment;
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

    public boolean isIncoming()
    {
        return incoming;
    }

    public long getPhoneNumber()
    {
        return phoneNumber;
    }

    public String getComment()
    {
        return comment;
    }
}
