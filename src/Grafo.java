import java.util.*;

public class Grafo {
    Map<String, Nodo> nodos = new HashMap<>();

    public void agregarNodo(String nombre) {
        nodos.putIfAbsent(nombre, new Nodo(nombre));
    }

    public void agregarArista(String origen, String destino, int coste, Object view) {
        Nodo nodoOrigen = nodos.get(origen);
        Nodo nodoDestino = nodos.get(destino);
        if (nodoOrigen != null && nodoDestino != null) {
            nodoOrigen.conexiones.add(new Arista(nodoDestino, coste, view));
        }
    }

    public List<Nodo> minimizarValorBacktracking(String inicio, String destinoFinal) {
        Nodo nodoInicio = nodos.get(inicio);
        Nodo nodoDestino = nodos.get(destinoFinal);
        List<Nodo> mejorCamino = new ArrayList<>();
        List<Nodo> caminoActual = new ArrayList<>();
        Set<Nodo> visitados = new HashSet<>();
        int[] minValor = {Integer.MAX_VALUE}; // Usamos un valor muy alto para el mínimo

        backtrackMin(nodoInicio, nodoDestino, 0, minValor, caminoActual, mejorCamino, visitados);
        return mejorCamino;
    }

    private void backtrackMin(Nodo nodoActual, Nodo nodoDestino, int valorActual, int[] minValor,
                              List<Nodo> caminoActual, List<Nodo> mejorCamino, Set<Nodo> visitados) {
        // Añadir el nodo actual al camino y marcarlo como visitado
        caminoActual.add(nodoActual);
        visitados.add(nodoActual);

        // Si alcanzamos el destino, actualizamos el mejor camino si es el valor mínimo
        if (nodoActual.equals(nodoDestino)) {
            if (valorActual < minValor[0]) {
                minValor[0] = valorActual;
                mejorCamino.clear();
                mejorCamino.addAll(new ArrayList<>(caminoActual));
            }
        } else {
            // Explorar cada arista del nodo actual
            for (Arista arista : nodoActual.conexiones) {
                Nodo vecino = arista.destino;
                int costeArista = arista.coste;

                // Continuar solo si la arista es válida y el nodo vecino no está visitado
                if (costeArista != -1 && !visitados.contains(vecino)) {
                    backtrackMin(vecino, nodoDestino, valorActual + costeArista, minValor, caminoActual, mejorCamino, visitados);
                }
            }
        }

        // Retroceder: desmarcar el nodo actual y eliminarlo del camino
        visitados.remove(nodoActual);
        caminoActual.remove(caminoActual.size() - 1);
    }

    public List<Nodo> maximizarValorBacktracking(String inicio, String destinoFinal) {
        Nodo nodoInicio = nodos.get(inicio);
        Nodo nodoDestino = nodos.get(destinoFinal);
        List<Nodo> mejorCamino = new ArrayList<>();
        List<Nodo> caminoActual = new ArrayList<>();
        Set<Nodo> visitados = new HashSet<>();
        int[] maxValor = {0}; // Usamos un array para almacenar el valor máximo y que sea mutable

        backtrack(nodoInicio, nodoDestino, 0, maxValor, caminoActual, mejorCamino, visitados);
        return mejorCamino;
    }

    private void backtrack(Nodo nodoActual, Nodo nodoDestino, int valorActual, int[] maxValor,
                           List<Nodo> caminoActual, List<Nodo> mejorCamino, Set<Nodo> visitados) {
        // Añadir el nodo actual al camino y marcarlo como visitado
        caminoActual.add(nodoActual);
        visitados.add(nodoActual);

        // Si alcanzamos el destino, actualizamos el mejor camino si es el valor máximo
        if (nodoActual.equals(nodoDestino)) {
            if (valorActual > maxValor[0]) {
                maxValor[0] = valorActual;
                mejorCamino.clear();
                mejorCamino.addAll(new ArrayList<>(caminoActual));
            }
        } else {
            // Explorar cada arista del nodo actual
            for (Arista arista : nodoActual.conexiones) {
                Nodo vecino = arista.destino;
                int costeArista = arista.coste;

                // Continuar solo si la arista es válida y el nodo vecino no está visitado
                if (costeArista != -1 && !visitados.contains(vecino)) {
                    backtrack(vecino, nodoDestino, valorActual + costeArista, maxValor, caminoActual, mejorCamino, visitados);
                }
            }
        }

        // Retroceder: desmarcar el nodo actual y eliminarlo del camino
        visitados.remove(nodoActual);
        caminoActual.remove(caminoActual.size() - 1);
    }

    public Object bloquearRuta(String origen, String destino) {
        Object target = null;
        if (nodos.containsKey(origen) && nodos.containsKey(destino)) {
            // Buscar las aristas entre origen y destino
            for (Arista arista : nodos.get(origen).conexiones) {
                if (arista.destino.nombre.equals(destino)) {
                    // Cambiar el coste de la arista a -1 para bloquearla
                    arista.coste = -1; // Bloqueada
                    target = arista.view;
                    break;
                }
            }
        } else {
            System.out.println("Los nodos no existen.");
        }

        return target;
    }
}
