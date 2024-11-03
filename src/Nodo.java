import java.util.ArrayList;

public class Nodo{
    String nombre;
    ArrayList<Arista> conexiones = new ArrayList<>();

    public Nodo(String nombre) {
        this.nombre = nombre;
    }
}
