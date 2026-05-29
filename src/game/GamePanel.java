package game;

import player.Player;
import score.HighscoreManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 150;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    int bodyParts = 6;
    char direction = 'R';
    Timer timer;
    Random random;
    int foodX;
    int foodY;
    int score = 0;
    boolean running = false;

    Player player;

    HighscoreManager highscoreManager = new HighscoreManager();

    public GamePanel(Player player) {

        random = new Random();

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.player = player;
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {

        newFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void newFood() {

        foodX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {

            // Food
            g.setColor(Color.red);

            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            // Snake Head
            g.setColor(Color.green);

            g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);

            // Snake Body
            for (int i = 1; i < bodyParts; i++) {

                g.setColor(new Color(45, 180, 0));

                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // Score
            g.setColor(Color.white);

            g.setFont(new Font("Ink Free", Font.BOLD, 30));

            FontMetrics metrics =
                    getFontMetrics(g.getFont());

            g.drawString(
                    "Score: " + score,
                    (SCREEN_WIDTH - metrics.stringWidth("Score: " + score)) / 2,
                    g.getFont().getSize()
            );
            g.drawString(
                    "Player: " + player.getUsername(),
                    20,
                    40
            );
        } else {

            gameOver(g);
        }
    }

    public void checkFood() {

        if (x[0] == foodX && y[0] == foodY) {

            bodyParts++;

            score++;

            newFood();
        }
    }

    public void move() {

        for (int i = bodyParts; i > 0; i--) {

            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {

            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;

            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;

            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;

            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }


    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {

                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    public void checkCollisions() {

        // برخورد با بدن خودش
        for (int i = bodyParts; i > 0; i--) {

            if (x[0] == x[i] && y[0] == y[i]) {

                running = false;
            }
        }

        // برخورد با دیوار چپ
        if (x[0] < 0) {
            running = false;
        }

        // برخورد با دیوار راست
        if (x[0] >= SCREEN_WIDTH) {
            running = false;
        }

        // برخورد با سقف
        if (y[0] < 0) {
            running = false;
        }

        // برخورد با کف
        if (y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {

            timer.stop();

            highscoreManager.saveScore(
                    player.getUsername(),
                    score
            );
        }
    }

    public void gameOver(Graphics g) {

        // Score
        g.setColor(Color.red);

        g.setFont(new Font("Ink Free", Font.BOLD, 40));

        FontMetrics metrics1 =
                getFontMetrics(g.getFont());

        g.drawString(
                "Score: " + score,
                (SCREEN_WIDTH - metrics1.stringWidth("Score: " + score)) / 2,
                g.getFont().getSize()
        );

        // Game Over Text
        g.setColor(Color.red);

        g.setFont(new Font("Ink Free", Font.BOLD, 75));

        FontMetrics metrics2 =
                getFontMetrics(g.getFont());

        g.drawString(
                "Game Over",
                (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2,
                SCREEN_HEIGHT / 2
        );
        showHighscores(g);
    }

    public void showHighscores(Graphics g) {

        ArrayList<String> scores =
                highscoreManager.loadScores();

        g.setColor(Color.white);

        g.setFont(new Font("Arial", Font.PLAIN, 20));

        int yPosition = 350;

        g.drawString("Highscores:", 200, 320);

        for (int i = 0;
             i < scores.size() && i < 5;
             i++) {

            g.drawString(
                    scores.get(i),
                    200,
                    yPosition
            );

            yPosition += 30;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        move();
        checkFood();
        checkCollisions();
        repaint();
    }
}
