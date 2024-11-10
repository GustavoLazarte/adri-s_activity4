import java.util.*;

public class Grafo {
    Map<String, Nodo> nodos = new HashMap<>();

    public void agregarNodo(String nombre) {
        nodos.putIfAbsent(nombre, new Nodo(nombre));
    }

    public void agregarArista(String origen, String destino, int coste, Object edgeView) {
        Nodo nodoOrigen = nodos.get(origen);
        Nodo nodoDestino = nodos.get(destino);
        if (nodoOrigen != null && nodoDestino != null) {
            // Arista de origen a destino
            nodoOrigen.conexiones.add(new Arista(nodoDestino, coste,edgeView));

            // Arista de destino a origen (bidireccional)
            nodoDestino.conexiones.add(new Arista(nodoOrigen, coste,edgeView));
        }
    }


    public List<Nodo> maximizarValor(String inicio, String destinoFinal) {
        Nodo nodoInicio = nodos.get(inicio);
        Map<Nodo, Integer> valor = new HashMap<>();
        Map<Nodo, Nodo> camino = new HashMap<>();
        PriorityQueue<Nodo> queue = new PriorityQueue<>((a, b) -> valor.get(b) - valor.get(a));
        Set<Nodo> visitados = new HashSet<>();  // Para evitar ciclos

        valor.put(nodoInicio, 0);
        queue.add(nodoInicio);
        visitados.add(nodoInicio);  // Marcamos el nodo inicio como visitado

        while (!queue.isEmpty()) {
            Nodo nodoActual = queue.poll();

            for (Arista arista : nodoActual.conexiones) {
                if (arista.coste == -1) {
                    continue;
                }

                // Si el nodo destino ya ha sido visitado, saltamos esta arista
                if (visitados.contains(arista.destino)) {
                    continue;
                }

                int nuevoValor = valor.get(nodoActual) + arista.coste;

                if (nuevoValor > valor.getOrDefault(arista.destino, 0)) {
                    valor.put(arista.destino, nuevoValor);
                    camino.put(arista.destino, nodoActual);
                    queue.add(arista.destino);
                    visitados.add(arista.destino);  // Marcamos como visitado el nodo destino
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

    public Object bloquearRuta(String origen, String destino) {
        Object target = null;
        if (nodos.containsKey(origen) && nodos.containsKey(destino)) {
            // Buscar las aristas entre origen y destino
            for (Arista arista : nodos.get(origen).conexiones) {
                if (arista.destino.nombre.equals(destino)) {
                    // Cambiar el coste de la arista a -1 para bloquearla
                    arista.coste = -1; // Bloqueada
                    break;
                }
            }

            // También bloquear la ruta en la dirección contraria
            for (Arista arista : nodos.get(destino).conexiones) {
                if (arista.destino.nombre.equals(origen)) {
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
