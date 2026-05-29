package game;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import player.Player;

public class GameFrame extends JFrame {
    public GameFrame() {

        String username = JOptionPane.showInputDialog(
                null,
                "Enter your username:"
        );

        if(username == null || username.trim().isEmpty()) {
            username = "Player";
        }

        Player player = new Player(username);

        this.add(new GamePanel(player));

        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pack();

        this.setLocationRelativeTo(null);

        this.setResizable(false);

        this.setVisible(true);
    }
}