package data;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class Call
{
    private final int id;
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

    public boolean isIncoming()
    {
        return incoming;
    }

    public void setIncoming(boolean incoming)
    {
        this.incoming = incoming;
    }

    public long getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }
}
