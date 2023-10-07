import java.io.*;
import java.net.Socket;

public class BattleChopperClient {
    private static final String SERVER_IP = "127.0.0.1"; // Cambia esta dirección al IP del servidor
    private static final int SERVER_PORT = 4444;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader userInput;

    public BattleChopperClient() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            userInput = new BufferedReader(new InputStreamReader(System.in));

            // Crea un hilo para recibir mensajes del servidor de manera continua
            Thread receiveThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String serverResponse;
                        while ((serverResponse = in.readLine()) != null) {
                            System.out.println("Servidor: " + serverResponse);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receiveThread.start();

            // Implementa la lógica para enviar mensajes desde el usuario aquí
            String userInputMessage;
            while ((userInputMessage = userInput.readLine()) != null) {
                sendMessage(userInputMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public static void main(String[] args) {
        new BattleChopperClient();
    }
}
