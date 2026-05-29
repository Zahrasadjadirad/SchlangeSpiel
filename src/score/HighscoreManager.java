package score;

import java.io.*;
import java.util.ArrayList;

public class HighscoreManager {

    private final String FILE_PATH = "highscores.txt";

    public void saveScore(String username, int score) {

        try {

            FileWriter writer =
                    new FileWriter(FILE_PATH, true);

            writer.write(username + ";" + score + "\n");

            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public ArrayList<String> loadScores() {

        ArrayList<String> scores =
                new ArrayList<>();

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(FILE_PATH)
                    );

            String line;

            while((line = reader.readLine()) != null) {

                scores.add(line);
            }

            reader.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return scores;
    }
}

