package advisor;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isAuth = false;
        String input = "";
        while (!input.equals("exit")) {
            input = scanner.nextLine();
            switch (input) {
                case "auth":
                    System.out.println("https://accounts.spotify.com/authorize?client_id=c915d91261d3470a8a920b25c8687a04redirect_uri=http://localhost:8080&response_type=code");
                    System.out.println();
                    System.out.println("---SUCCESS---\n");
                    isAuth = true;
                    break;
                case "new":
                    if (!isAuth) {
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
                    if (!isAuth) {
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
