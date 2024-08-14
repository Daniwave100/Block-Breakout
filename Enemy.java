import java.awt.Color;
import java.awt.Graphics;

public class Enemy {
    private int x, y;
    private int width, height;
    private int speed;


    public Enemy(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public void update() {
        // Move the enemy downward
        y += speed;
    }

    // enemy graphics
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // Check for collision with the player
    public boolean intersects(int playerX, int playerY, int playerWidth, int playerHeight) {
        return (x < playerX + playerWidth && x + width > playerX && y < playerY + playerHeight && y + height > playerY);
    }
}
