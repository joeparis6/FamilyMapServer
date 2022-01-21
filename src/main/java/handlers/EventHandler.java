package handlers;

import JsonConvert.JsonSerializer;
import RequestAndResponse.AllEventsResponse;
import RequestAndResponse.EventResponse;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import dao.DataAccessException;
import service.EventService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;


public class EventHandler extends RequestHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {

            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {

                Headers reqHeaders = exchange.getRequestHeaders();

                if (reqHeaders.containsKey("Authorization")) {

                    String authToken = reqHeaders.getFirst("Authorization");
                    URI pathURI = exchange.getRequestURI();
                    String path = pathURI.toString();

                    EventService service = new EventService();
                    EventResponse eventResponse = new EventResponse();
                    AllEventsResponse allEventsResponse = new AllEventsResponse();
                    if (path.equals("/event")) {
                        allEventsResponse = service.getAllEvents(authToken);

                        if (allEventsResponse.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        }
                        else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }

                        JsonSerializer serializer = responseToJson;
                        String respData = serializer.serialize(allEventsResponse);
                        OutputStream respBody = exchange.getResponseBody();
                        writeString(respData, respBody);
                        respBody.close();

                    }
                    else {
                        String eventID = parseEventID(path);
                        eventResponse = service.getEvent(eventID, authToken);

                        if (eventResponse.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        }
                        else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                        }
                        JsonSerializer serializer = responseToJson;
                        String respData = serializer.serialize(eventResponse);

                        OutputStream respBody = exchange.getResponseBody();
                        writeString(respData, respBody);
                        respBody.close();

                    }

                } else {

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                }
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);

            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }

    public String parseEventID(String path) {
        path = path.replace("/event/", "");
        return path;
    }

}