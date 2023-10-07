import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
public class PlayerControls extends KeyAdapter {
    private BattleChopperGame game;

    public PlayerControls(BattleChopperGame game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP) {
            game.getPlayer().setMoveUp(true);
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            game.getPlayer().setMoveDown(true);
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            game.getPlayer().setMoveLeft(true);
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            game.getPlayer().setMoveRight(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Cuando se suelta una tecla, detener el movimiento en esa dirección
        if (keyCode == KeyEvent.VK_UP) {
            game.getPlayer().setMoveUp(false);
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            game.getPlayer().setMoveDown(false);
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            game.getPlayer().setMoveLeft(false);
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            game.getPlayer().setMoveRight(false);
        }
    }
}
