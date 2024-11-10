public class Arista {
    Nodo destino;
    int coste;
    Object view;

    public Arista(Nodo destino, int coste, Object edge) {
        this.destino = destino;
        this.coste = coste;
        this.view = edge;
    }
}
