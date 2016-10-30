package utils;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Utils
{
    public static final DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
            .appendDayOfMonth(2)
            .appendLiteral('.')
            .appendMonthOfYear(2)
            .appendLiteral('.')
            .appendYear(4, 4)
            .toFormatter();
    public static final PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
            .printZeroAlways()
            .appendHours()
            .appendLiteral(":")
            .appendMinutes()
            .toFormatter();

    public static String innerXml(Node node)
    {
        String s = "";
        NodeList childs = node.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++)
        {
            s += serializeNode(childs.item(i));
        }
        return s;
    }

    private static String serializeNode(Node node)
    {
        String s = "";
        if (node.getNodeName().equals("#text")) return node.getTextContent();
        s += "<" + node.getNodeName() + " ";
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null)
        {
            for (int i = 0; i < attributes.getLength(); i++)
            {
                s += attributes.item(i).getNodeName() + "=\"" + attributes.item(i).getNodeValue() + "\"";
            }
        }
        NodeList childs = node.getChildNodes();
        if (childs == null || childs.getLength() == 0)
        {
            s += "/>";
            return s;
        }
        s += ">";
        for (int i = 0; i < childs.getLength(); i++)
            s += serializeNode(childs.item(i));
        s += "</" + node.getNodeName() + ">";
        return s;
    }

}
