package queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import files.Archivo;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Productor {

    private static final String TASK_QUEUE_NAME = "WorkQueue";
    
    private static final String QUEUE_IP = "localhost";
    
    private static String FILE_PATH;
    
    private static int nLineas =0;

    public static void main(String[] argv) {
        if (argv.length == 0){
            System.out.println("Ingrese el nombre del archivo");
            return;
        }else{
            FILE_PATH = argv[0];
        }
        
        
        System.out.println("[ PRODUCTOR ]");
        Archivo archivo = new Archivo(FILE_PATH);
        if (archivo.getLineas().isEmpty()){
            return;
        }
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(QUEUE_IP);
        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            
            for (String linea : archivo.getLineas()) {
                encolarMensaje(linea, channel);
                nLineas++;
            }

            System.out.println("\nLíneas procesdas: " + nLineas);
        } catch (IOException ex) {
            System.out.println("Error en la conexión");
        } catch (TimeoutException ex) {
            System.out.println("Error en el tiempo de espera");
        }
    }
    
    public static void encolarMensaje(String mensaje, Channel channel) throws IOException{
        channel.basicPublish("", TASK_QUEUE_NAME,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        mensaje.getBytes("UTF-8"));
                System.out.println("Enviado: [ " + mensaje + " ]");
    }

}
