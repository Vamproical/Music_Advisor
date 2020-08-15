package advisor;

import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class OAuth {
    public OAuth() {

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
        checkIfError(response.body());
        if (response.body().contains("access_token")) {
            parseAccessToken(response.body());
        }
    }

    public void getNew() throws Exception {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Utils.ACCESS_TOKEN)
                .uri(URI.create(Utils.API_PATH + "/v1/browse/new-releases"))
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        checkIfError(response.body());
        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject albums = jo.get("albums").getAsJsonObject();
        albums.get("items").getAsJsonArray().forEach(item -> {
            JsonObject album = item.getAsJsonObject();
            String name = album.get("name").getAsString();
            String url = album.get("external_urls").getAsJsonObject().get("spotify").getAsString();
            List<String> artists = new ArrayList<>();
            album.get("artists").getAsJsonArray().forEach(artist -> {
                artists.add(artist.getAsJsonObject().get("name").getAsString());
                System.out.println(name);
                System.out.println(artists);
                System.out.println(url);
                System.out.println();
            });

        });
    }

    public void getFeatured() throws Exception {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Utils.ACCESS_TOKEN)
                .uri(URI.create(Utils.API_PATH + "/v1/browse/featured-playlists"))
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        checkIfError(response.body());
        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject playlists = jo.get("playlists").getAsJsonObject();
        playlists.get("items").getAsJsonArray().forEach(item -> {
            JsonObject album = item.getAsJsonObject();
            String name = album.get("name").getAsString();
            String url = album.get("external_urls").getAsJsonObject().get("spotify").getAsString();
            System.out.println(name);
            System.out.println(url);
            System.out.println();
        });
    }

    public void getCategories() throws Exception {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Utils.ACCESS_TOKEN)
                .uri(URI.create(Utils.API_PATH + "/v1/browse/categories"))
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        checkIfError(response.body());
        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject playlists = jo.get("categories").getAsJsonObject();
        playlists.get("items").getAsJsonArray().forEach(item -> {
            JsonObject category = item.getAsJsonObject();
            String name = category.get("name").getAsString();
            System.out.println(name);
        });
    }

    private String getCategoryIdByCategoryName(String categoryName) throws Exception {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Utils.ACCESS_TOKEN)
                .uri(URI.create(Utils.API_PATH + "/v1/browse/categories"))
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String categories = response.body();
        checkIfError(response.body());
        JsonArray items = JsonParser.parseString(categories).getAsJsonObject()
                .get("categories").getAsJsonObject()
                .get("items").getAsJsonArray();
        for (JsonElement item : items) {
            String name = item.getAsJsonObject().get("name").getAsString();
            if (categoryName.equals(name)) {
                return item.getAsJsonObject().get("id").getAsString();
            }
        }
        return null;
    }

    public void getCategories(String playlist) throws Exception {
        String categoryID = getCategoryIdByCategoryName(playlist);
         if(categoryID == null){
            System.out.println("Unknown category name.");
            return;
        }
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Utils.ACCESS_TOKEN)
                .uri(URI.create(Utils.API_PATH + "/v1/browse/categories/" + categoryID + "/playlists"))
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        checkIfError(response.body());
        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject playlists = jo.get("playlists").getAsJsonObject();
        playlists.get("items").getAsJsonArray().forEach(item -> {
            JsonObject category = item.getAsJsonObject();
            String url = category.get("external_urls").getAsJsonObject().get("spotify").getAsString();
            String name = category.get("name").getAsString();
            System.out.println(name);
            System.out.println(url);
            System.out.println();
        });
    }

    private void checkIfError(String response) throws Exception {

        try {
            JsonObject object = JsonParser.parseString(response).getAsJsonObject();
            if (object.has("error")) {
                String errorMessage = object.get("error").getAsJsonObject()
                        .get("message").getAsString();
                throw new Exception(errorMessage);
            }
        } catch (JsonSyntaxException | IllegalStateException e) {
            throw new Exception("Wrong json format : \n" + response);
        }
    }

    public void parseAccessToken(String body) {
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        Utils.ACCESS_TOKEN = jo.get("access_token").getAsString();
    }
}
