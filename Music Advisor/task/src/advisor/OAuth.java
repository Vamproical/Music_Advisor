package advisor;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OAuth {
    public OAuth() throws IOException, InterruptedException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        server.start();
        System.out.println("use this link to request the access code:");
        System.out.println(Utils.SERVER_PATH+ "/authorize"
                + "?client_id=" + Utils.CLIENT_ID
                + "&redirect_uri=" + Utils.REDIRECT_URI
                + "&response_type=" + "code");
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
        if (Utils.AUTH_CODE.isBlank()) { Thread.sleep(10); }
        server.stop(10);
    }

    public String getAccessToken() throws IOException, InterruptedException {
        System.out.println("Making http request for access_token...");
        HttpRequest httpRequest = HttpRequest.newBuilder().
                header("Content-Type", "application/x-www-form-urlencoded").
                uri(URI.create("https://accounts.spotify.com" + "/api/token")).
                POST(HttpRequest.BodyPublishers.ofString("client_id=" + Utils.CLIENT_ID
                        + "&client_secret=" + Utils.CLIENT_SECRET
                        + "&grant_type=" + "authorization_code"
                        + "&code=" + Utils.AUTH_CODE
                        + "&redirect_uri=" + Utils.REDIRECT_URI))
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
