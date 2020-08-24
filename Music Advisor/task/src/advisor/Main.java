package advisor;

public class Main {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length - 1; i++) {
            switch (args[i]) {
                case "-access":
                    Utils.SERVER_PATH = args[i + 1];
                    break;
                case "-resource":
                    Utils.API_PATH = args[i + 1];
                    break;
                case "-page":
                    Utils.PAGE = Integer.parseInt(args[i + 1]);
                    break;
            }
        }
        MusicAdviser advisor = new MusicAdviser();
        advisor.advise();
    }
}
