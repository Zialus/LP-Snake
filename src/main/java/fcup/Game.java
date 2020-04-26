package fcup;

import lombok.extern.java.Log;

import java.io.IOException;
import java.util.logging.Level;

@Log
public class Game {

    public static void main(String[] args) {
        try {
            new GameInstance();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

}