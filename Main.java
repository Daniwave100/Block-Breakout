import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // creates JFrame instance (basically just the gui window)
        JFrame frame = new JFrame(); // creates frame
        frame.setTitle("Block Breakout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true); // makes frame visible
        frame.setResizable(false);
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.setVisible(true);

        // starts the game thread
        gamePanel.startGameThread();
        }
    }
