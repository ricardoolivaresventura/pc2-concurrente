import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class BattleChopperServer {
    private static final int PORT = 4444;
    private List<Player> players = new ArrayList<>();
    private int playerCount = 0;
    private BattleChopperGame game;

    public BattleChopperServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                playerCount++;
                System.out.println("Jugador " + playerCount + " conectado");

                Player player = new Player(playerCount, clientSocket);

                players.add(player);
                player.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Player extends Thread {
        private int number; // Número único de jugador
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public Player(int number, Socket socket) {
            this.number = number;
            this.socket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received message from Jugador " + number + ": " + message);

                    // Procesa los mensajes recibidos de los clientes aquí
                    // Supongamos que el cliente envía mensajes "X:100,Y:200" para la posición
                    // Actualiza la posición del jugador en todos los clientes, incluido el propio servidor

                    for (Player otherPlayer : players) {
                        if (otherPlayer != this) {
                            // Reenvía el mensaje a todos los otros clientes (excepto el propio cliente que lo envió)
                            otherPlayer.sendMessage("X:" + otherPlayer.getX() + ",Y:" + otherPlayer.getY());
                        }
                    }

                    // También puedes imprimir el mensaje en la consola del servidor para verificar
                    System.out.println("Jugador " + number + " se movió a la posición: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Handle client disconnection
                players.remove(this);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public int getX() {
            // Agrega la lógica para obtener la posición X del jugador
            return 0; // Debes implementar esto según tu diseño de juego
        }

        public int getY() {
            // Agrega la lógica para obtener la posición Y del jugador
            return 0; // Debes implementar esto según tu diseño de juego
        }
    }

    public static void main(String[] args) {
        new BattleChopperServer();
    }
}
