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

public class OAuth {
    private String code = "";

    public OAuth() throws IOException, InterruptedException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        server.start();
        System.out.println("use this link to request the access code:");
        System.out.println("https://accounts.spotify.com" + "/authorize"
                + "?client_id=" + "c915d91261d3470a8a920b25c8687a04"
                + "&redirect_uri=" + "http://localhost:8080"
                + "&response_type=" + "code");
        System.out.println();
        System.out.println("waiting for code...");
        server.createContext("/",
                exchange -> {
                    String query = exchange.getRequestURI().getQuery();
                    String result;

                    if (query != null && query.contains("code")) {
                        code = query.substring(5);
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
        while (code.equals("")) {
            Thread.sleep(10);
        }
        server.stop(10);
    }

    public String getAccessToken() throws IOException, InterruptedException {
        System.out.println("Making http request for access_token...");
        HttpRequest httpRequest = HttpRequest.newBuilder().
                POST(HttpRequest.BodyPublishers.ofString("client_id=" + "c915d91261d3470a8a920b25c8687a04"
                        + "&client_secret=" + "f461101a522c4e198ab2daba736e9166"
                        + "&grant_type=" + "authorization_code"
                        + "&code=" + code
                        + "&redirect_uri=" + "http://localhost:8080"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create("https://accounts.spotify.com" + "/api/token"))
                .build();
        HttpResponse<String> httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String token = httpResponse.body();
        JsonObject jo = JsonParser.parseString(token).getAsJsonObject();
        return jo.get("access_token").getAsString();
    }
}
