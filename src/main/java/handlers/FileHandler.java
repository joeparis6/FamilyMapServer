package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;

public class FileHandler extends RequestHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {

                String urlPath = exchange.getRequestURI().toString();

                if (urlPath == null || urlPath.equals("/")) {
                    urlPath = "/index.html";
                }

                String filePath = "web" + urlPath;
                File file = new File (filePath);
                if (file.exists()) {
                    OutputStream responseBody = exchange.getResponseBody();
                    try {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        Files.copy(file.toPath(), responseBody);
                    } catch (AccessDeniedException e) {
                        e.printStackTrace();
                        System.out.println("access denied");
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                    }
                }
                else {
                    System.out.println("file does not exist");
                    filePath = "web/HTML/404.html";
                    file = new File (filePath);

                    OutputStream responseBody = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    Files.copy(file.toPath(), responseBody);
                }
                exchange.getResponseBody().close();

            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
        }

    }
}
