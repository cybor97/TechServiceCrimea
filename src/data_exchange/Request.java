package data_exchange;

import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

import static utils.Utils.innerXml;

public class Request
{
    private String type;
    private String in;
    private String by;
    private String data;

    static Request parse(String xml)
    {
        Request result = new Request();
        if (xml != null && !xml.isEmpty())
            try
            {
                xml = xml.replaceAll("[^\\x20-\\x7e]", "");
                Element element = DocumentBuilderFactory
                        .newInstance()
                        .newDocumentBuilder()//Hate JAVA for that!
                        .parse(new ByteArrayInputStream(xml.getBytes("UTF-8")))
                        .getDocumentElement();

                result.type = element.getElementsByTagName("Type").item(0).getTextContent();
                result.in = element.getElementsByTagName("In").item(0).getTextContent();
                result.by = element.getElementsByTagName("By").item(0).getTextContent();
                result.data = innerXml(element.getElementsByTagName("Data").item(0));
            } catch (Exception e)
            {
                System.err.println("Request.parse()->\n" + e.toString());
            }
        return result;
    }

    String getType()
    {
        return type;
    }

    String in()
    {
        return in;
    }

//    public String by()
//    {
//        return by;
//    }

    public String getData()
    {
        return data;
    }
}
