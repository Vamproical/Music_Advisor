package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

public class OAuth {

    public List<String> getNew() throws Exception {
        List<String> newList = new ArrayList<>();
        JsonObject newReleases = SpotifyRequests.getNewReleases();
        checkIfError(newReleases);
        newReleases.get("items").getAsJsonArray().forEach(item -> {
            JsonObject album = item.getAsJsonObject();
            String name = album.get("name").getAsString();
            String url = album.get("external_urls").getAsJsonObject().get("spotify").getAsString();
            List<String> artists = new ArrayList<>();
            album.get("artists").getAsJsonArray().forEach(artist -> {
                artists.add(artist.getAsJsonObject().get("name").getAsString());
                newList.add(name + "\n");
                newList.add(artists.toString() + "\n");
                newList.add(url + "\n");
                newList.add("\n");
            });
        });
        return newList;
    }

    public List<String> getFeatured() throws Exception {
        List<String> featuredList = new ArrayList<>();
        JsonObject features = SpotifyRequests.getFeatures();
        checkIfError(features);
        features.get("items").getAsJsonArray().forEach(item -> {
            JsonObject album = item.getAsJsonObject();
            String name = album.get("name").getAsString();
            String url = album.get("external_urls").getAsJsonObject().get("spotify").getAsString();
            featuredList.add(name + "\n");
            featuredList.add(url + "\n");
            featuredList.add("\n");
        });
        return featuredList;
    }

    public List<String> getCategories() throws Exception {
        JsonObject categories = SpotifyRequests.getCategories();
        List<String> categoriesList = new ArrayList<>();
        checkIfError(categories);
        categories.get("items").getAsJsonArray().forEach(item -> {
            JsonObject category = item.getAsJsonObject();
            String name = category.get("name").getAsString();
            categoriesList.add(name + "\n");
        });
        return categoriesList;
    }

    public List<String> getPlaylist(String playlist) throws Exception {
        List<String> playlistList = new ArrayList<>();
        JsonObject playlists = SpotifyRequests.getPlaylists(playlist);
        if (playlist == null) {
            System.out.println("Unknown category name.");
            return null;
        }
        checkIfError(playlists);
        playlists.get("items").getAsJsonArray().forEach(item -> {
            JsonObject category = item.getAsJsonObject();
            String url = category.get("external_urls").getAsJsonObject().get("spotify").getAsString();
            String name = category.get("name").getAsString();
            playlistList.add(name + "\n");
            playlistList.add(url + "\n");
            playlistList.add("\n");
        });
        return playlistList;
    }

    private void checkIfError(JsonObject object) throws Exception {
        try {
            if (object.has("error")) {
                String errorMessage = object.get("error").getAsJsonObject()
                        .get("message").getAsString();
                throw new Exception(errorMessage);
            }
        } catch (JsonSyntaxException | IllegalStateException e) {
            throw new Exception("Wrong json format : \n" + object.toString());
        }
    }
}
