package tcp;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Reportador {

    private static final int PUERTO = 9999;

    private static Double TIEMPO_INICIO;

    private static final ArrayList<String> firmas = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(PUERTO);
            System.out.println("[ REPORTADOR ]");
            System.out.println("** Esperando firmas **\n");
            recibirFirma(server);

        } catch (IOException ex) {
            System.out.println("Error en la creación del servidor");
        }
    }

    public static void recibirFirma(ServerSocket server) {
        Thread threadRecibir = new Thread(() -> {
            try (Socket socket = server.accept();) {
                recibirFirma(server);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                while (true) {
                    String firma = input.readUTF();
                    if (TIEMPO_INICIO == null) {
                        TIEMPO_INICIO = System.currentTimeMillis() / 1000.0;
                    }
                    System.out.println("Firma Recibida: [ " + firma + " ]");
                    firmas.add(firma);
                    System.out.println("Número de firmas recibidas: " + firmas.size());
                    double tiempoActual = System.currentTimeMillis() / 1000.0;
                    System.out.println("Total transcurrido: " + (tiempoActual - TIEMPO_INICIO) + " segundos\n");
                }
            } catch (IOException ex) {
                System.out.println("Error de conexion");
            }
        });
        threadRecibir.start();
    }
}
