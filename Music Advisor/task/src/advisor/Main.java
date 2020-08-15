package advisor;

public class Main {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("-access")) {
                Utils.SERVER_PATH = args[i + 1];
            } else if (args[i].equals("-resource")) {
                Utils.API_PATH = args[i + 1];
            }
        }
        MusicAdviser advisor = new MusicAdviser();
        advisor.advise();
    }
}
