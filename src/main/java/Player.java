import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private int number; // Número único de jugador
    private int x;
    private int y;
    private int speed;

    private List<Bullet> bullets;

    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;
    private BattleChopperGame game;

    private double direction = 0;

    public Player(int number, BattleChopperGame game) {
        this.number = number;
        this.game = game;
        x = 100;
        y = 300;
        speed = 3;
        bullets = new ArrayList<>();
    }

    public int getPlayerNumber() {
        return number;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setMoveUp(boolean moveUp) {
        this.moveUp = moveUp;
    }

    public void setMoveDown(boolean moveDown) {
        this.moveDown = moveDown;
    }

    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void update(List<Enemy> enemies) {
        // Implementa la lógica para mover al jugador aquí usando las variables de dirección
        if (moveUp) {
            y -= speed; // Mover hacia arriba
        }
        if (moveDown) {
            y += speed; // Mover hacia abajo
        }
        if (moveLeft) {
            x -= speed; // Mover hacia la izquierda
            direction = 180;
        }
        if (moveRight) {
            x += speed; // Mover hacia la derecha
            direction = 0;
        }
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            if (bullet.isActive()) {
                bullet.update();
            } else {
                bullets.remove(i); // Elimina balas inactivas de la lista
            }
        }
    }

    public void shoot() {
        int startX = x + 20 / 2; // Posición inicial de la bala en el centro del jugador
        int startY = y;
        int bulletSpeed = 10; // Velocidad de la bala (ajustada)

        // Crear una nueva instancia de la clase Bullet y configurar su dirección
        Bullet bullet = new Bullet(startX, startY, bulletSpeed);
        bullet.setDirection(direction);

        // Agregar la bala a la lista de balas
        bullets.add(bullet);
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, 20, 20);
        // Dibujar todas las balas activas
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }
}
