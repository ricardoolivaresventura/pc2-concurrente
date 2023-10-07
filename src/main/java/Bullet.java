import java.awt.Graphics;
import java.awt.Color;
public class Bullet {
    private int x;
    private int y;
    private int speed;
    private boolean active;

    private double direction;
    private int startX;
    private int startY;
    private int maxdistance;


    public Bullet(int startX, int startY, int bulletSpeed) {
        x = startX;
        y = startY;
        this.startX = startX; // Inicializa startX
        this.startY = startY; // Inicializa startY
        speed = bulletSpeed;
        maxdistance = 200;
        active = true;
    }
    public void setDirection(double direction) {
        this.direction = direction;
    }
    public void update() {
        if (active) {
            double radians = Math.toRadians(direction);
            // Calcular la dirección de movimiento basada en la dirección actual
            double moveX = Math.cos(radians);
            double moveY = Math.sin(radians);

            // Si la dirección es hacia la izquierda (180 grados), invierte la dirección en X
            if (direction == Math.PI) {
                moveX = -moveX;
            }

            x += moveX * speed;
            y += moveY * speed;

            // Comprobar si la bala ha superado la distancia máxima permitida
            double distance = Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2));
            if (distance >= maxdistance) {
                active = false; // Desactivar la bala si supera la distancia máxima
            }
        }
    }




    public void draw(Graphics g) {
        if (active) {
            g.setColor(Color.YELLOW); // Puedes cambiar el color a tu preferencia
            g.fillOval(x, y, 20, 20); // Dibuja un círculo como representación de la bala
            // Dibujar la bala en la posición (x, y)
        }
    }
    public boolean isActive() {
        return active;
    }
    public void setInactive() {
        active = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Agregar métodos getter y setter según sea necesario
}
