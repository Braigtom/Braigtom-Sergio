package files;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Archivo {
    
    private ArrayList<String> lineas = new ArrayList<>();
    
    public Archivo(String ruta){
        try(BufferedReader br = new BufferedReader(new FileReader(ruta))){
            String linea;
            while((linea = br.readLine()) != null){
                lineas.add(linea);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Archivo no encontrado");
        } catch (IOException ex) {
            System.out.println("Error de lectura");
        }
    }

    public ArrayList<String> getLineas() {
        return lineas;
    }
        
}
