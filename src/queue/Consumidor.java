package queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import cryptography.Crypt;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import tcp.Mensajero;

public class Consumidor {

    private static final String TASK_QUEUE_NAME = "WorkQueue";

    private static final String QUEUE_IP = "localhost";

    private static String REPORTADOR_IP = "localhost";
    
    private static boolean verificarArgs(String[] args) {
        for (String arg : args) {
            if (arg.equalsIgnoreCase("-h") || arg.equalsIgnoreCase("--help")) {
                System.out.println("AYUDA");
                return false;
            }
        }
        if (args.length == 0) {
            System.out.println("Necesita ingresar la dirección IP");
            System.out.println("'java -jar consumidor.jar <IP>'");
        } else if (args.length > 1) {
            System.out.println("¡Demasiados argumentos!");
            System.out.println("Para más información pruebe:\n'java -jar consumidor.jar --help'");
        } else {
            if (args[0].equalsIgnoreCase("localhost")) {
                args[0] = "127.0.0.1";
            }
            Matcher matcher = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})").matcher(args[0]);
            if (matcher.find()) {
                REPORTADOR_IP = matcher.group();
                for (String b : REPORTADOR_IP.split("\\.")) {
                    if (Integer.parseInt(b) > 255) {
                        System.out.println("¡IP incorrecta!");
                        return false;
                    }
                }
                return true;
            } else {
                System.out.println("¡IP incorrecta!");
            }
        }
        return false;
    }

    public static void main(String[] argv) {
        
        if (!verificarArgs(argv)){
            return;
        }
        
        Mensajero mensajero = new Mensajero(REPORTADOR_IP);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(QUEUE_IP);
        try {
            final Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();

            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            System.out.println("[ CONSUMIDOR ]");
            System.out.println("** Esperando mensajes **\n");

            channel.basicQos(1);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");

                System.out.println("Recibido: [ " + message + " ]");

                String firma = Crypt.stringToSHA512(message);

                System.out.println("Firma generada: [ " + firma + " ]\n");

                mensajero.agendarFirma(firma);

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };
            channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {
            });
        } catch (IOException ex) {
            System.out.println("Error de conexión");
        } catch (TimeoutException ex) {
            System.out.println("Error en el tiempo de espera");
        }
    }

}
