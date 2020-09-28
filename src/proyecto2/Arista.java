package proyecto2;

public class Arista {
    
    private String inicio;
    private String fin;

    public Arista(String inicio, String fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }
}