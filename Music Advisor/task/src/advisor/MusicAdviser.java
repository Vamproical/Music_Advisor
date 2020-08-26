package advisor;

import java.util.List;
import java.util.Scanner;

public class MusicAdviser {
    public void advise() throws Exception {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        OAuth oAuth = new OAuth();
        Authorization authorization = Authorization.getInstance();
        while (!input.equals("exit")) {
            input = scanner.nextLine();
            switch (input) {
                case "auth":
                    authorization.getConnection();
                    authorization.getAccessToken();
                    System.out.println("---SUCCESS---");
                    break;
                case "new":
                    if (Utils.ACCESS_TOKEN.isEmpty()) {
                        System.out.println("Please, provide access for application.");
                    } else {
                        List<String> newList = oAuth.getNew();
                        View.print(newList);
                    }
                    break;
                case "categories":
                    if (Utils.ACCESS_TOKEN.isEmpty()) {
                        System.out.println("Please, provide access for application.");
                    } else {
                        List<String> categoriesList = oAuth.getCategories();
                        View.print(categoriesList);
                    }
                    break;
                case "featured":
                    if (Utils.ACCESS_TOKEN.isEmpty()) {
                        System.out.println("Please, provide access for application.");
                    } else {
                        List<String> featuredList = oAuth.getFeatured();
                        View.print(featuredList);
                    }
                    break;
                case "next":
                    View.printNextPage();
                    break;
                case "prev":
                    View.printPrevPage();
                    break;
                case "exit":
                    System.out.println("---GOODBYE---");
                    break;
                default:
                    if (Utils.ACCESS_TOKEN.isEmpty()) {
                        System.out.println("Please, provide access for application.");
                    } else if (input.startsWith("playlists")) {
                        List<String> playlistList = oAuth.getPlaylist(input.substring(10));
                        if (playlistList != null) {
                            View.print(playlistList);
                        }
                    }
                    break;
            }
        }
    }
}
