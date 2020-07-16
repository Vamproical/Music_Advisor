package advisor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length > 1 && args[0].equals("-access")) {
            Utils.SERVER_PATH = args[1];
        }
        MusicAdviser advisor = new MusicAdviser();
        advisor.advise();
    }
}
