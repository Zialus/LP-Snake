package fcup;

import java.io.IOException;

public class Game {

    public static void main(String[] args) {
        try {
            new GameInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}