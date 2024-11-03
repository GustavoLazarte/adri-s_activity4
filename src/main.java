import java.util.List;
import java.util.Map;

public class main {
    public static void main(String[] args) {
        Grafo grafo = new Grafo();

        // Crear nodos
        grafo.agregarNodo("Anzaldo");
        grafo.agregarNodo("Arani");
        grafo.agregarNodo("Ucureña");
        grafo.agregarNodo("Arbieto");
        grafo.agregarNodo("Tarata");
        grafo.agregarNodo("Tolata");
        grafo.agregarNodo("Cliza");
        grafo.agregarNodo("Punata");
        grafo.agregarNodo("San Benito");
        grafo.agregarNodo("San Antonio de Tiraque");

        // Agregar aristas (ejemplo con valores hipotéticos)
        grafo.agregarArista("Anzaldo", "Arani", 20);
        grafo.agregarArista("Anzaldo", "Punata", 16);
        grafo.agregarArista("Arani", "Ucureña", 40);
        grafo.agregarArista("Arani", "Cliza", 12);
        grafo.agregarArista("Arani", "San Benito", 18);
        grafo.agregarArista("Cliza", "San Benito", -1);
        grafo.agregarArista("Cliza", "Tolata", 6);
        grafo.agregarArista("Cliza", "Arbieto", 20);
        grafo.agregarArista("Punata", "San Benito", 14);
        grafo.agregarArista("Punata", "Ucureña", 13);
        grafo.agregarArista("Ucureña", "San Benito", -1);
        grafo.agregarArista("Ucureña", "Tolata", 5);
        grafo.agregarArista("Ucureña", "Arbieto", 27);
        grafo.agregarArista("Ucureña", "Tarata", 10);
        grafo.agregarArista("San Benito", "Tolata", 12);
        grafo.agregarArista("San Benito", "Tarata", 23);
        grafo.agregarArista("Tolata", "San Antonio de Tiraque", 18);
        grafo.agregarArista("Arbieto", "San Antonio de Tiraque", 22);
        grafo.agregarArista("Arbieto", "San Antonio de Tiraque", 30);


        List<Nodo> resultadoMax = grafo.maximizarValor("Anzaldo", "San Antonio de Tiraque");
        List<Nodo> resultadoMin = grafo.minimizarCosto("Anzaldo", "San Antonio de Tiraque");

        // Mostrar resultados de maximización
        System.out.print("Ruta con máximo valor de Anzaldo a San Antonio de Tiraque: ");
        for (int i = 0; i < resultadoMax.size(); i++) {
            System.out.print(resultadoMax.get(i).nombre);
            if (i < resultadoMax.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();

        // Mostrar resultados de minimización
        System.out.print("Ruta con mínimo coste de Anzaldo a San Antonio de Tiraque: ");
        for (int i = 0; i < resultadoMin.size(); i++) {
            System.out.print(resultadoMin.get(i).nombre);
            if (i < resultadoMin.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }
}
