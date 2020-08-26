package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SpotifyRequests {
    private final static String newRequest = Utils.API_PATH + "/v1/browse/new-releases";
    private static final String categoriesRequest = Utils.API_PATH + "/v1/browse/categories";
    private static final String featuredRequest = Utils.API_PATH + "/v1/browse/featured-playlists";

    public static String getRequestedData(String requests) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Utils.ACCESS_TOKEN)
                .uri(URI.create(requests))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static JsonObject getNewReleases() throws IOException, InterruptedException {
        String verboseJson = getRequestedData(newRequest);
        return JsonParser.parseString(verboseJson).getAsJsonObject().get("albums").getAsJsonObject();
    }

    public static JsonObject getCategories() throws IOException, InterruptedException {
        String categories = getRequestedData(categoriesRequest);
        return JsonParser.parseString(categories).getAsJsonObject().getAsJsonObject().get("categories").getAsJsonObject();
    }

    public static JsonObject getFeatures() throws IOException, InterruptedException {
        String features = getRequestedData(featuredRequest);
        return JsonParser.parseString(features).getAsJsonObject().get("playlists").getAsJsonObject();
    }

    public static String getCategoryIdByCategoryName(String categoryName) throws IOException, InterruptedException {
        String categories = getRequestedData(categoriesRequest);
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

    public static JsonObject getPlaylists(String categoryName) throws IOException, InterruptedException {
        String categoryID = getCategoryIdByCategoryName(categoryName);
        if (categoryID == null) {
            return null;
        }
        String playlistsURL = Utils.API_PATH + "/v1/browse/categories/" + categoryID + "/playlists";
        return JsonParser.parseString(getRequestedData(playlistsURL)).getAsJsonObject().get("playlists").getAsJsonObject();
    }
}
