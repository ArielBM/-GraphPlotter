package proyecto2;

import java.util.ArrayList;

public class Vertice {
    
    private String nombre;
    private ArrayList<Adyacencia> conexiones;

    public Vertice(String nombre) {
        this.nombre = nombre;
        this.conexiones = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Adyacencia> getConexiones() {
        return conexiones;
    }

    public void setConexiones(ArrayList<Adyacencia> conexiones) {
        this.conexiones = conexiones;
    }
}
