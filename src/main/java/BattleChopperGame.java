import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.net.*;

public class BattleChopperGame extends JPanel implements ActionListener {
    private Timer timer;
    private Player player;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    private Timer shootTimer;

    private JLabel ipLabel;
    private JTextField ipField;
    private JLabel portLabel;
    private JTextField portField;
    private JButton connectButton;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public BattleChopperGame() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(new PlayerControls(this));

        player = new Player(players.size() + 1, this);
        players.add(player);

        // Agrega enemigos a la lista en posiciones aleatorias
        for (int i = 0; i < 3; i++) {
            int randomX = (int) (Math.random() * 760);
            int randomY = (int) (Math.random() * 560);
            enemies.add(new Enemy(randomX, randomY));
        }
        timer = new Timer(20, this);
        timer.start();
        shootTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.shoot();
            }
        });
        shootTimer.setInitialDelay(0);
        shootTimer.start();

        // Componentes para la conexión
        ipLabel = new JLabel("IP:");
        ipField = new JTextField(15);
        portLabel = new JLabel("Puerto:");
        portField = new JTextField(5);
        connectButton = new JButton("Conectar");

        // Configuración de la disposición
        JPanel connectionPanel = new JPanel();
        connectionPanel.add(ipLabel);
        connectionPanel.add(ipField);
        connectionPanel.add(portLabel);
        connectionPanel.add(portField);
        connectionPanel.add(connectButton);

        setLayout(new BorderLayout());
        add(connectionPanel, BorderLayout.NORTH);

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String serverIP = ipField.getText();
                int serverPort = Integer.parseInt(portField.getText());

                try {
                    // Intenta conectar al servidor
                    clientSocket = new Socket(serverIP, serverPort);
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    // Deshabilita la edición de dirección IP y puerto después de la conexión
                    ipField.setEnabled(false);
                    portField.setEnabled(false);
                    connectButton.setEnabled(false);

                    // Crea una nueva instancia de Player con un número único
                    Player newPlayer = new Player(players.size() + 1, BattleChopperGame.this);
                    players.add(newPlayer);

                    // Comienza a escuchar al servidor en un hilo separado
                    Thread receiveThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String message;
                                while ((message = in.readLine()) != null) {
                                    // Procesa los mensajes recibidos del servidor aquí
                                    // Supongamos que el servidor envía mensajes "Jugador X:XValue,Y:YValue" para la posición
                                    // Actualiza la posición de los jugadores locales en el juego
                                    if (message.startsWith("Jugador")) {
                                        String[] parts = message.split(",");
                                        for (Player p : players) {
                                            int playerNumber = p.getPlayerNumber(); // Asegúrate de tener un método getPlayerNumber en la clase Player
                                            if (message.contains("Jugador " + playerNumber)) {
                                                int x = Integer.parseInt(parts[1].split(":")[1]);
                                                int y = Integer.parseInt(parts[2].split(":")[1]);
                                                p.setPosition(x, y);
                                            }
                                        }
                                        repaint();
                                    }
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }

                    });
                    receiveThread.start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al conectar al servidor", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    public void sendPlayerPosition() {
        // Obtén la posición actual del jugador
        int x = player.getX();
        int y = player.getY();

        // Construye un mensaje con la posición
        String message = "X:" + x + ",Y:" + y;

        // Verifica si la conexión al servidor se ha establecido antes de enviar el mensaje
        if (out != null) {
            out.println(message);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        for (Enemy enemy : enemies) {
            enemy.update();
        }


        for (Bullet bullet : player.getBullets()) {
            for (Enemy enemy : new ArrayList<>(enemies)) {
                if (enemy.checkCollision(bullet)) {
                    enemies.remove(enemy);
                    bullet.setInactive();
                }
            }
        }
        for (Player p : players) {
            p.update(enemies);
        }

        if (enemies.isEmpty()) {
            enemies.clear();
            for (int i = 0; i < 3; i++) {
                int randomX = (int) (Math.random() * 760);
                int randomY = (int) (Math.random() * 560);
                enemies.add(new Enemy(randomX, randomY));
            }
        }
        sendPlayerPosition();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Player p : players) {
            p.draw(g);
        }
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Battle Chopper");
        BattleChopperGame game = new BattleChopperGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}