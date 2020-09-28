
package proyecto2;

public class Adyacencia {
    
    private String vecino;
    private int cantidad;

    public Adyacencia(String vecino) {
        this.vecino = vecino;
        this.cantidad = 0;
    }

    public String getVecino() {
        return vecino;
    }

    public void setVecino(String vecino) {
        this.vecino = vecino;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    
    
    
    
}
