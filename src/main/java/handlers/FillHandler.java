package handlers;


import JsonConvert.JsonSerializer;
import RequestAndResponse.FillRequest;
import RequestAndResponse.FillResponse;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import dao.DataAccessException;
import service.Fill;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;

public class FillHandler extends RequestHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                URI pathURI = exchange.getRequestURI();
                String path = pathURI.toString();
                String[] params = getFillParamsFromURI(path);
                String userName = params[0];
                String gens = params[1];

                FillRequest fillRequestFromJson = new FillRequest(userName, gens);
                Fill newFill = new Fill();
                FillResponse fillResponseToJson = newFill.fill(fillRequestFromJson); //returns a response result
                JsonSerializer serializer = responseToJson;

                String respData = serializer.serialize(fillResponseToJson);
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

    public String[] getFillParamsFromURI(String path) {
        path = path.replace("/fill/", "");
        StringBuilder pathVar1 = new StringBuilder();
        StringBuilder pathVar2 = null;
        System.out.println(path);
        int i = 0;
        int length = path.length();
        while (i < length && path.charAt(i) != '/') {
             pathVar1.append(path.charAt(i));
             i++;
        }
        i++;
        if (i < path.length()) {
            pathVar2 = new StringBuilder();
        }
        while(i < path.length()) {
            pathVar2.append(path.charAt(i));
            i++;
        }
        String userName = String.valueOf(pathVar1);
        System.out.println(userName);
        String gens;
        if (pathVar2 != null) {
            gens = String.valueOf(pathVar2);
            System.out.println(gens);
        }
        else {
            gens = null;
        }
        String[] params = new String[2];
        params[0] = userName;
        params[1] = gens;
        return params;
    }

}
