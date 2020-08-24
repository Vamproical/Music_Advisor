package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Authorization {
    private static Authorization instance;

    private Authorization() {
    }

    public static Authorization getInstance() {
        if (instance == null) {
            instance = new Authorization();
        }
        return instance;
    }

    public void getConnection() throws IOException, InterruptedException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.start();
        System.out.println("use this link to request the access code:");
        System.out.println(Utils.SERVER_PATH + "/authorize"
                + "?client_id=" + Utils.CLIENT_ID
                + "&redirect_uri=" + Utils.REDIRECT_URI
                + "&response_type=code");
        System.out.println();
        System.out.println("waiting for code...");
        server.createContext("/",
                exchange -> {
                    String query = exchange.getRequestURI().getQuery();
                    String result;
                    if (query != null && query.contains("code")) {
                        Utils.AUTH_CODE = query.substring(5);
                        result = "Got the code. Return back to your program.";
                    } else {
                        result = "Not found authorization code. Try again.";
                    }
                    exchange.sendResponseHeaders(200, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(result);
                }
        );
        while (Utils.AUTH_CODE.equals("")) {
            Thread.sleep(10);
        }
        server.stop(10);
    }

    public void getAccessToken() throws Exception {
        System.out.println("Making http request for access_token...");
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(Utils.SERVER_PATH + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=authorization_code" +
                                "&code=" + Utils.AUTH_CODE +
                                "&client_id=" + Utils.CLIENT_ID +
                                "&client_secret=" + Utils.CLIENT_SECRET +
                                "&redirect_uri=" + Utils.REDIRECT_URI))
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.body().contains("access_token")) {
            parseAccessToken(response.body());
        }
    }

    public void parseAccessToken(String body) {
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        Utils.ACCESS_TOKEN = jo.get("access_token").getAsString();
    }
}
