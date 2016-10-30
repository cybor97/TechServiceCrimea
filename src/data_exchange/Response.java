package data_exchange;

import com.sun.org.apache.xerces.internal.impl.PropertyManager;
import com.sun.xml.internal.stream.writers.XMLStreamWriterImpl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import data.Call;
import data.Departure;

public class Response
{
    private final List<?> data;

    public Response()
    {
        data = new ArrayList<>();
    }

    public Response(String str)
    {
        this(Collections.singletonList(str));
    }

    public Response(List<?> data)
    {
        this.data = data;
    }

    public String toXML()
    {
        try
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            XMLStreamWriter writer = new XMLStreamWriterImpl(stream, "UTF-8", new PropertyManager(PropertyManager.CONTEXT_WRITER));
            writer.writeStartElement("RICServerResponse");
            for (Object current : data)
                if (current instanceof Call)
                {
                    Call call = (Call) current;
                    writer.writeStartElement("Call");
                    writer.writeAttribute("ID", Integer.toString(call.getId()));
                    writer.writeAttribute("Incoming", Boolean.toString(call.isIncoming()));
                    writer.writeAttribute("Date", call.getDate().toString());
                    writer.writeAttribute("Duration", call.getDuration().toString());
                    writer.writeAttribute("PhoneNumber", Long.toString(call.getPhoneNumber()));
                    writer.writeCharacters(call.getComment());
                    writer.writeEndElement();
                } else if (current instanceof Departure)
                {
                    Departure departure = (Departure) current;
                    writer.writeStartElement("Departure");
                    writer.writeAttribute("ID", Integer.toString(departure.getId()));
                    writer.writeAttribute("Date", departure.getDate().toString());
                    writer.writeAttribute("Duration", departure.getDuration().toString());
                    writer.writeAttribute("Address", departure.getAddress());
                    writer.writeAttribute("Result", departure.getResult());
                    writer.writeEndElement();
                } else if (current instanceof String)
                {
                    writer.writeStartElement("Text");
                    writer.writeCharacters(current.toString());
                    writer.writeEndElement();
                }
            writer.writeEndElement();
        } catch (Exception e)
        {
            System.err.println("Response.toString()->\n" + e.toString());
        }
        return super.toString();
    }
}