package handlers;

import JsonConvert.JsonSerializer;
import RequestAndResponse.PersonResponse;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import dao.DataAccessException;
import service.PersonService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;


public class PersonHandler extends RequestHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {


        try {

            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {

                Headers reqHeaders = exchange.getRequestHeaders();

                if (reqHeaders.containsKey("Authorization")) {

                    String authToken = reqHeaders.getFirst("Authorization");

                    URI pathURI = exchange.getRequestURI();
                    String path = pathURI.toString();
                    PersonResponse personResponse = new PersonResponse();

                    if (path.equals("/person")) {
                        PersonService service = new PersonService();
                        personResponse = service.personService(null, authToken, true);
                    }
                    else {
                        String personID = parsePersonID(path);
                        PersonService service = new PersonService();
                        personResponse = service.personService(personID, authToken, false);
                    }

                    if (personResponse.isSuccess()) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    }

                    JsonSerializer serializer = responseToJson;
                    String respData = serializer.serialize(personResponse);
                    OutputStream respBody = exchange.getResponseBody();
                    writeString(respData, respBody);
                    respBody.close();

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

    public String parsePersonID(String path) {
        path = path.replace("/person/", "");
        return path;
    }
}