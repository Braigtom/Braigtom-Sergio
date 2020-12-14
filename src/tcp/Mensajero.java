package tcp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Mensajero {

    private static final int PUERTO = 9999;
    private Socket socket;
    private DataOutputStream output;
    private final String IP;
    private final CopyOnWriteArrayList<String> firmas = new CopyOnWriteArrayList<>();

    public Mensajero(String IP) {
        this.IP = IP;
        gestionarConexion(IP);
    }

    public final void gestionarConexion(String IP) {
        Thread threadConexion = new Thread(() -> {
            int estado = 1;
            while (true) {
                switch (estado) {
                    case 1:
                        try {
                            socket = new Socket(IP, PUERTO);
                            output = new DataOutputStream(socket.getOutputStream());
                            estado = 2;
                        } catch (IOException ex) {
                            System.out.println("Esperando al Reportador");
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException ex1) {
                                System.out.println("Error al esperar");
                            }
                        }
                        break;
                    case 2:
                        if (!firmas.isEmpty()) {
                            String firma = firmas.remove(0);
                            try {
                                output.writeUTF(firma);
                                System.out.println("El Mensajero ha enviado la siguiente firma al Reportador:\n[ " + firma + " ]\n");
                            } catch (IOException ex) {
                                System.out.println("Error de envío\n");
                                firmas.add(firma);
                                try {
                                    socket.close();
                                } catch (IOException ex1) {
                                    System.out.println("No se pudo cerrar el socket\n");
                                }
                                estado = 1;
                            } 
                        }
                        break;
                    default:
                        break;
                }

            }
        });
        threadConexion.start();
    }

    public void agendarFirma(String firma) {
        firmas.add(firma);
    }

}
