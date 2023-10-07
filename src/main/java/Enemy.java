import java.awt.*;

public class Enemy {
    private int x;
    private int y;

    public Enemy(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void update() {
        // Implementa la lógica de movimiento del enemigo aquí
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 20, 20);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public boolean checkCollision(Bullet bullet) {
        return x < bullet.getX() + 20 &&
                x + 20 > bullet.getX() &&
                y < bullet.getY() + 20 &&
                y + 20 > bullet.getY();
    }
}
