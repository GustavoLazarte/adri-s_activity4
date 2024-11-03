import java.util.*;

public class Grafo {
    Map<String, Nodo> nodos = new HashMap<>();

    public void agregarNodo(String nombre) {
        nodos.putIfAbsent(nombre, new Nodo(nombre));
    }

    public void agregarArista(String origen, String destino, int coste) {
        Nodo nodoOrigen = nodos.get(origen);
        Nodo nodoDestino = nodos.get(destino);
        if (nodoOrigen != null && nodoDestino != null) {
            nodoOrigen.conexiones.add(new Arista(nodoDestino, coste));
        }
    }

    public List<Nodo> maximizarValor(String inicio, String destinoFinal) {
        Nodo nodoInicio = nodos.get(inicio);
        Map<Nodo, Integer> valor = new HashMap<>();
        Map<Nodo, Nodo> camino = new HashMap<>();
        PriorityQueue<Nodo> queue = new PriorityQueue<>((a, b) -> valor.get(b) - valor.get(a));

        valor.put(nodoInicio, 0);
        queue.add(nodoInicio);

        while (!queue.isEmpty()) {
            Nodo nodoActual = queue.poll();

            for (Arista arista : nodoActual.conexiones) {
                if (arista.coste == -1) {
                    continue;
                }

                int nuevoValor = valor.get(nodoActual) + arista.coste;

                if (nuevoValor > valor.getOrDefault(arista.destino, 0)) {
                    valor.put(arista.destino, nuevoValor);
                    camino.put(arista.destino, nodoActual);
                    queue.add(arista.destino);
                }
            }
        }

        return reconstruirCamino(camino, destinoFinal);
    }

    public List<Nodo> minimizarCosto(String inicio, String destinoFinal) {
        Nodo nodoInicio = nodos.get(inicio);
        Map<Nodo, Integer> costo = new HashMap<>();
        Map<Nodo, Nodo> camino = new HashMap<>();
        PriorityQueue<Nodo> queue = new PriorityQueue<>(Comparator.comparingInt(costo::get));

        costo.put(nodoInicio, 0);
        queue.add(nodoInicio);

        while (!queue.isEmpty()) {
            Nodo nodoActual = queue.poll();

            for (Arista arista : nodoActual.conexiones) {
                if (arista.coste == -1) {
                    continue;
                }

                int nuevoCosto = costo.get(nodoActual) + arista.coste;

                if (nuevoCosto < costo.getOrDefault(arista.destino, Integer.MAX_VALUE)) {
                    costo.put(arista.destino, nuevoCosto);
                    camino.put(arista.destino, nodoActual);
                    queue.add(arista.destino);
                }
            }
        }

        return reconstruirCamino(camino, destinoFinal);
    }

    private List<Nodo> reconstruirCamino(Map<Nodo, Nodo> camino, String destinoFinal) {
        List<Nodo> mejorCamino = new ArrayList<>();
        Nodo nodoActual = nodos.get(destinoFinal);
        if (nodoActual != null && camino.containsKey(nodoActual)) {
            while (nodoActual != null) {
                mejorCamino.add(nodoActual);
                nodoActual = camino.get(nodoActual);
            }
            Collections.reverse(mejorCamino); // Invertir para obtener el camino correcto
        }
        return mejorCamino;
    }
}
