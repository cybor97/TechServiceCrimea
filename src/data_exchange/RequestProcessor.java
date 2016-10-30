package data_exchange;

import data.Call;
import data.DBHolder;
import data.Departure;

public class RequestProcessor
{
    private static DBHolder dbHolder = DBHolder.getInstance();

    public static Response process(Request request)
    {
        Response result = new Response();
        switch (request.getType())
        {
            case "TEST":
                return new Response("OK");
            case "GET":
                switch (request.in())
                {
                    case "Call":
                        return new Response(dbHolder.getCalls());
                    case "Departure":
                        return new Response(dbHolder.getDepartures());
                }
                break;
            case "ADD":
                switch (request.in())
                {
                    case "Call":
                        dbHolder.addCall(Call.parse(request.getData()));
                        break;
                    case "Departure":
                        dbHolder.addDeparture(Departure.parse(request.getData()));
                        break;
                }
                return new Response("OK");
            case "SET":
                switch (request.in())
                {
                    case "Call":
                        dbHolder.setCall(Call.parse(request.getData()));
                        break;
                    case "Departure":
                        dbHolder.setDeparture(Departure.parse(request.getData()));
                        break;
                }
                return new Response("OK");
            case "DELETE":
            case "REMOVE":
                switch (request.in())
                {
                    case "Call":
                        dbHolder.removeCall(Call.parse(request.getData()));
                        break;
                    case "Departure":
                        dbHolder.removeDeparture(Departure.parse(request.getData()));
                        break;
                }
                return new Response("OK");
        }
        return result;
    }
}