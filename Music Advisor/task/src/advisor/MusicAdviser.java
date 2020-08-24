package advisor;

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
                        oAuth.getNew();
                    }
                    break;
                case "categories":
                    if (Utils.ACCESS_TOKEN.isEmpty()) {
                        System.out.println("Please, provide access for application.");
                    } else {
                        oAuth.getCategories();
                    }
                    break;
                case "featured":
                    if (Utils.ACCESS_TOKEN.isEmpty()) {
                        System.out.println("Please, provide access for application.");
                    } else {
                        oAuth.getFeatured();
                    }
                    break;
                case "exit":
                    break;
                default:
                    if (Utils.ACCESS_TOKEN.isEmpty()) {
                        System.out.println("Please, provide access for application.");
                    } else if (input.startsWith("playlists")) {
                        oAuth.getPlaylist(input.substring(10));
                    }
                    break;
            }
        }
    }
}
