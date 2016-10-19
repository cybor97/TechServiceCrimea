package data_exchange;

public class RequestProcessor
{
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
                        //return new Response(DBHolder.GetCalls());
                    case "Departure":
                        //return new Response(DBHolder.GetDepartureData());
                }
                break;
            case "ADD":
                switch (request.in())
                {
                    case "Call":
                        //DBHolder.Add(Call.Parse(req.Data));
                        break;
                    case "Departure":
                        //DBHolder.Add(Departure.Parse(req.Data));
                        break;
                }
                return new Response("OK");
            case "SET":
                switch (request.in())
                {
                    case "Call":
                        //DBHolder.Set(Call.Parse(request.Data));
                        break;
                    case "Departure":
                        //DBHolder.Set(Departure.Parse(request.Data));
                        break;
                }
                return new Response("OK");
            case "DELETE":
            case "REMOVE":
                switch (request.in())
                {
                    case "Call":
                        //DBHolder.Remove(Call.Parse(request.Data));
                        break;
                    case "Departure":
                        //DBHolder.Remove(Departure.Parse(request.Data));
                        break;
                }
                return new Response("OK");
        }
        return result;
    }
}