package handlers;

import JsonConvert.JsonSerializer;
import RequestAndResponse.LoadRequest;
import RequestAndResponse.LoadResponse;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;

import dao.DataAccessException;
import service.Load;
import JsonConvert.JsonDeserializer;

public class LoadHandler extends RequestHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                Headers reqHeaders = exchange.getRequestHeaders();
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                System.out.println(reqData);

                JsonDeserializer deserializer = jsonToRequest;
                LoadRequest loadRequestFromJson = new LoadRequest();
                loadRequestFromJson = JsonDeserializer.deserialize(reqData, LoadRequest.class);
                Load newLoad = new Load();
                LoadResponse loadResponseToJson = newLoad.load(loadRequestFromJson); //returns a response result

                JsonSerializer serializer = responseToJson;
                String respData = serializer.serialize(loadResponseToJson);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                writeString(respData, respBody);

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

            exchange.getResponseBody().close();
        } catch (IOException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);

            exchange.getResponseBody().close();
            e.printStackTrace();
        }

    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }

}