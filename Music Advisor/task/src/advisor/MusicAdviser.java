package advisor;

import java.io.IOException;
import java.util.Scanner;

public class MusicAdviser {
    public void advise() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equals("exit")) {
            input = scanner.nextLine();
            switch (input) {
                case "auth":
                    OAuth oAuth = new OAuth();
                    Utils.ACCESS_TOKEN = oAuth.getAccessToken();
                    System.out.println("response:");
                    System.out.println(Utils.ACCESS_TOKEN);
                    System.out.println("---SUCCESS---\n");
                    break;
                case "new":
                    if (Utils.ACCESS_TOKEN.isEmpty()) {
                        System.out.println("Please, provide access for application.");
                    } else {
                        System.out.println("---NEW RELEASES---\n" +
                                "Mountains [Sia, Diplo, Labrinth]\n" +
                                "Runaway [Lil Peep]\n" +
                                "The Greatest Show [Panic! At The Disco]\n" +
                                "All Out Life [Slipknot]");
                    }
                    break;
                case "categories":
                    System.out.println("---CATEGORIES---\n" +
                            "Top Lists\n" +
                            "Pop\n" +
                            "Mood\n" +
                            "Latin\n");
                    break;
                case "featured":
                    if (Utils.ACCESS_TOKEN.isEmpty()) {
                        System.out.println("Please, provide access for application.");
                    } else {
                        System.out.println("---FEATURED---\n" + "Mellow Morning\n" +
                                "Wake Up and Smell the Coffee\n" +
                                "Monday Motivation\n" +
                                "Songs to Sing in the Shower\n");
                    }
                    break;
                case "playlists Mood":
                    System.out.println("---MOOD PLAYLISTS---\n" +
                            "Walk Like A Badass  \n" +
                            "Rage Beats  \n" +
                            "Arab Mood Booster  \n" +
                            "Sunday Stroll");
                    break;
                case "exit":
                    System.out.println("---GOODBYE!---");
                    break;
            }
        }
    }
}
