package handlers;

import JsonConvert.JsonSerializer;
import com.sun.net.httpserver.HttpHandler;

import JsonConvert.JsonDeserializer;

public abstract class RequestHandler implements HttpHandler {

    JsonDeserializer jsonToRequest = new JsonDeserializer();
    JsonSerializer responseToJson = new JsonSerializer();

}
