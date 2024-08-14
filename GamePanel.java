import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.Timer;

public class GamePanel extends JPanel implements Runnable {
    // Variable initializations
    Thread gameThread;
    KeyHandler keyH = new KeyHandler();
    ArrayList<Enemy> enemies = new ArrayList<>();
    Random random = new Random();
    Timer timer;
    int elapsedTime = 0;
    boolean gameOver = false;

    // Player start position
    int playerX = 400;
    int playerY = 400;
    int playerSpeed = 4;

    // Player dimensions
    int playerWidth = 30;
    int playerHeight = 30;

    public GamePanel() {
        setBackground(Color.black);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        // Initializes and start the timer using Java Swing gui
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                repaint(); // Redraw the screen to update the clock display
            }
        });
        timer.start();
    }

    // Start game thread method
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override // Overrides run() method of runnable interface
    public void run() {
        double FPS = 60;
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            if (!gameOver) {
                // Update game state
                update();
                // Repaint/draws screen with updated game state
                repaint();
            } else {
                if (keyH.gPressed) {
                    restartGame();
                }
            }
            // frame timing
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        // Moves player according to which key is pressed
        if (keyH.upPressed) {
            if (playerY - playerSpeed < 0) {
                playerY = 0;
            } else {
                playerY -= playerSpeed;
            }
        }
        if (keyH.downPressed) {
            if (playerY + playerSpeed > getHeight() - playerHeight) {
                playerY = getHeight() - playerHeight;
            } else {
                playerY += playerSpeed;
            }
        }
        if (keyH.leftPressed) {
            if (playerX - playerSpeed < 0) {
                playerX = 0;
            } else {
                playerX -= playerSpeed;
            }
        }
        if (keyH.rightPressed) {
            if (playerX + playerSpeed > getWidth() - playerWidth) {
                playerX = getWidth() - playerWidth;
            } else {
                playerX += playerSpeed;
            }
        }

        // Randomly generates enemies
            if (random.nextInt(60) < (1 + (elapsedTime / 10))) {  // Adjust this value to control enemy generation frequency
                int enemyX = random.nextInt(getWidth() - 30); // ensures enemy wont spawn partially off screen
                int enemyY = 0;
                int enemyWidth = 30;
                int enemyHeight = 30;
                int enemySpeed = 3 + random.nextInt(3);  // Random speed between 3 and 5
                enemies.add(new Enemy(enemyX, enemyY, enemyWidth, enemyHeight, enemySpeed));
            }


        // Update enemies and check for collisions
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            enemy.update();

            // Remove enemy if it goes off screen
            if (enemy.getY() > getHeight()) {
                it.remove();
                continue;
            }

            // Check for collision
            if (enemy.intersects(playerX, playerY, playerWidth, playerHeight)) {
                // Handles collision
                System.out.println("Collision detected!");
                gameOver = true;
                timer.stop();
                it.remove();
            }
        }
    }

    // Graphics for the game. Draws rectangles for player and enemies as well as adds timer and end screen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!gameOver) {
        g.setColor(Color.white);
        g.fillRect(playerX, playerY, playerWidth, playerHeight);

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Time Elapsed: " + elapsedTime + "s", 10, 30);

        // Draw enemies
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
    } else {
            // Draws "You Lost" screen
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("YOU LOST!", getWidth() / 2 - 100, getHeight() / 2 - 20);

            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Time Elapsed: " + elapsedTime + "s", getWidth() / 2 - 80, getHeight() / 2 + 20);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press G to play again", 307, getHeight() / 2 + 60);
        }
        }
    // Method to restart the game
    public void restartGame() {
        playerX = 400;
        playerY = 400;
        elapsedTime = 0;
        enemies.clear();
        gameOver = false;
        timer.start(); // Restart the timer
    }
}
