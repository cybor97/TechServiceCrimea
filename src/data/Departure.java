package data;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.w3c.dom.Element;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilderFactory;

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

    public static Departure parse(String xml)
    {
        if (xml != null && xml.isEmpty())
            try
            {
                xml = xml.replaceAll("[^\\x20-\\x7e]", "");
                Element element = DocumentBuilderFactory
                        .newInstance()
                        .newDocumentBuilder()//Hate JAVA for that!
                        .parse(new ByteArrayInputStream(xml.getBytes("UTF-8")))
                        .getDocumentElement();

                return new Departure(Integer.parseInt(element.getAttribute("ID")),
                        DateTime.parse(element.getAttribute("Date")),//FIXME: 100% bug with formatting
                        Period.parse(element.getAttribute("Duration")).toStandardDuration(),//FIXME: 100% bug with formatting
                        element.getAttribute("Incoming"),
                        element.getAttribute("PhoneNumber"));
            } catch (Exception e)
            {
                System.err.println("Request.parse()->\n" + e.toString());
            }
        return null;
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

    @Override
    public String toString()
    {
        return super.toString();
    }
}
