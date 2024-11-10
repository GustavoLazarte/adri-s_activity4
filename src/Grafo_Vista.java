import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Grafo_Vista extends Grafo {
    private mxGraph graph;
    private Object parent;
    private Map<String, Object> nodosVisuales;
    private JFrame frame;

    public Grafo_Vista() {
        this.graph = new mxGraph() {
            @Override
            public boolean isCellConnectable(Object cell) {
                return false;
            }
        };
        this.parent = graph.getDefaultParent();
        this.nodosVisuales = new HashMap<>();
    }

    public void inicializarUI() {
        frame = new JFrame("Visualización del Grafo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Crear componente gráfico
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graph.setCellsEditable(false);
        graph.setCellsMovable(true);
        graph.setCellsResizable(false);
        graphComponent.setConnectable(false);

        frame.add(graphComponent, BorderLayout.CENTER);

        // Panel de controles
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campos para agregar nodo
        JPanel nodoPanel = new JPanel();
        nodoPanel.setLayout(new FlowLayout());
        JTextField nodoField = new JTextField(10);
        JButton agregarNodoBtn = new JButton("Agregar Nodo");
        agregarNodoBtn.addActionListener(e -> agregarNodo(nodoField.getText()));
        nodoPanel.add(new JLabel("Nombre del Nodo:"));
        nodoPanel.add(nodoField);
        nodoPanel.add(agregarNodoBtn);
        controlPanel.add(nodoPanel);

        // Campos para agregar arista
        JPanel aristaPanel = new JPanel();
        aristaPanel.setLayout(new FlowLayout());
        JTextField origenField = new JTextField(10);
        JTextField destinoField = new JTextField(10);
        JTextField costeField = new JTextField(10);
        JButton agregarAristaBtn = new JButton("Agregar Arista");
        agregarAristaBtn.addActionListener(e -> {
            String origen = origenField.getText();
            String destino = destinoField.getText();
            int coste;
            try {
                coste = Integer.parseInt(costeField.getText());
                agregarArista(origen, destino, coste);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor ingresa un número válido para el coste.");
            }
        });
        aristaPanel.add(new JLabel("Origen:"));
        aristaPanel.add(origenField);
        aristaPanel.add(new JLabel("Destino:"));
        aristaPanel.add(destinoField);
        aristaPanel.add(new JLabel("Coste:"));
        aristaPanel.add(costeField);
        aristaPanel.add(agregarAristaBtn);
        controlPanel.add(aristaPanel);

        // Campos para maximizar ruta
        JPanel maximizarPanel = new JPanel();
        maximizarPanel.setLayout(new FlowLayout());
        JTextField maximizarDesdeField = new JTextField(10);
        JTextField maximizarHastaField = new JTextField(10);
        JButton maximizarBtn = new JButton("Maximizar Ruta");
        maximizarBtn.addActionListener(e -> {
            resetearStyleAristas();
            String desde = maximizarDesdeField.getText();
            String hasta = maximizarHastaField.getText();
            List<Nodo> rutaMax = maximizarValor(desde, hasta);
            mostrarRuta("Máximo Valor", rutaMax);
        });
        maximizarPanel.add(new JLabel("Desde:"));
        maximizarPanel.add(maximizarDesdeField);
        maximizarPanel.add(new JLabel("Hasta:"));
        maximizarPanel.add(maximizarHastaField);
        maximizarPanel.add(maximizarBtn);
        controlPanel.add(maximizarPanel);


        // Campos para minimizar ruta
        JPanel minimizarPanel = new JPanel();
        minimizarPanel.setLayout(new FlowLayout());
        JTextField minimizarDesdeField = new JTextField(10);
        JTextField minimizarHastaField = new JTextField(10);
        JButton minimizarBtn = new JButton("Minimizar Ruta");
        minimizarBtn.addActionListener(e -> {
            resetearStyleAristas();
            String desde = minimizarDesdeField.getText();
            String hasta = minimizarHastaField.getText();
            List<Nodo> rutaMin = minimizarCosto(desde, hasta);
            mostrarRuta("Mínimo Costo", rutaMin);
        });
        minimizarPanel.add(new JLabel("Desde:"));
        minimizarPanel.add(minimizarDesdeField);
        minimizarPanel.add(new JLabel("Hasta:"));
        minimizarPanel.add(minimizarHastaField);
        minimizarPanel.add(minimizarBtn);
        controlPanel.add(minimizarPanel);

        // Agregar un panel para bloquear rutas
        JPanel bloquearPanel = new JPanel();
        bloquearPanel.setLayout(new FlowLayout());
        JTextField bloquearDesdeField = new JTextField(10);
        JTextField bloquearHastaField = new JTextField(10);
        JButton bloquearBtn = new JButton("Bloquear Ruta");
        bloquearBtn.addActionListener(e -> {
            String desde = bloquearDesdeField.getText();
            String hasta = bloquearHastaField.getText();
            Object edge = bloquearRuta(desde, hasta); // Bloquear la ruta en la estructura de datos
            actualizarAristaVista(edge); // Actualizar visualmente el grafo
        });
        bloquearPanel.add(new JLabel("Desde:"));
        bloquearPanel.add(bloquearDesdeField);
        bloquearPanel.add(new JLabel("Hasta:"));
        bloquearPanel.add(bloquearHastaField);
        bloquearPanel.add(bloquearBtn);
        controlPanel.add(bloquearPanel);

        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.setSize(800, 600);
        frame.setVisible(true);

    }

    // Método para mostrar la ruta en un cuadro de diálogo
    private void mostrarRuta(String tipo, List<Nodo> ruta) {
        StringBuilder sb = new StringBuilder();
        sb.append(tipo).append(": ");
        for (int i = 0; i < ruta.size(); i++) {
            sb.append(ruta.get(i).nombre);
            if (i < ruta.size() - 1) {
                sb.append(" -> ");
            }
        }
        JOptionPane.showMessageDialog(frame, sb.toString());

        // Cambiar el color de las aristas en la ruta
        cambiarColorAristasRuta(ruta, Color.RED); // Cambia "red" al color que desees
    }
    public void mostrarGrafo() {
        for (Nodo nodo : nodos.values()) {
            System.out.print("Nodo " + nodo.nombre + " tiene conexiones a: ");
            for (Arista arista : nodo.conexiones) {
                System.out.print(arista.destino.nombre + " (coste " + arista.coste + "), ");
            }
            System.out.println();
        }
    }
    private void cambiarColorAristasRuta(List<Nodo> mejorRuta, Color color) {
        graph.getModel().beginUpdate();
        try {
            for (int i = 0; i < mejorRuta.size() - 1; i++) {
                String origen = mejorRuta.get(i).nombre;
                String destino = mejorRuta.get(i + 1).nombre;
                Object origenVisual = nodosVisuales.get(origen);
                Object destinoVisual = nodosVisuales.get(destino);

                Object[] edges = graph.getEdges(origenVisual);
                boolean edgeFound = false;
                for (Object edge : edges) {
                    Object tfo = graph.getModel().getTerminal(edge, true);
                    Object tff = graph.getModel().getTerminal(edge, false);
                    if ((tfo == origenVisual && tff == destinoVisual) || (tfo == destinoVisual && tff == origenVisual)) {
                        // Convertir el color a formato hexadecimal
                        String hexColor = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

                        // Obtener el estilo actual de la arista
                        String currentStyle = graph.getModel().getStyle(edge);

                        // Cambiar solo el color del borde, manteniendo el resto del estilo
                        String newStyle = currentStyle + ";strokeColor=" + hexColor;

                        // Aplicar el nuevo estilo
                        graph.setCellStyle(newStyle, new Object[]{edge});

                        edgeFound = true;
                        break; // Salir del bucle una vez que se encontró la arista
                    }
                }
                if (!edgeFound) {
                    System.out.println("No se encontró la arista de " + origen + " a " + destino);
                }
            }
        } finally {
            graph.getModel().endUpdate();
        }
    }


    public void organizarGrafo() {
        mxCompactTreeLayout layout = new mxCompactTreeLayout(graph);
        layout.setHorizontal(false);
        graph.getModel().beginUpdate();
        try {
            layout.execute(parent);
        } finally {
            graph.getModel().endUpdate();
        }
    }



    // Método para agregar nodo visualmente y en la estructura de datos
    public void agregarNodo(String nombre) {
        if (!nodos.containsKey(nombre)) {
            super.agregarNodo(nombre); // Llamar al método de la superclase
            graph.getModel().beginUpdate();
            try {
                Object nodoVisual = graph.insertVertex(parent, null, nombre, 20, 20, 80, 30);
                nodosVisuales.put(nombre, nodoVisual);
            } finally {
                graph.getModel().endUpdate();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "El nodo ya existe.");
        }
    }

    // Método para agregar arista visualmente y en la estructura de datos
    public void agregarArista(String origen, String destino, int coste) {
        if (nodos.containsKey(origen) && nodos.containsKey(destino)) {
            // Agregar aristas bidireccionales en la estructura de datos lógica

            Object origenVisual = nodosVisuales.get(origen);
            Object destinoVisual = nodosVisuales.get(destino);
            graph.getModel().beginUpdate();
            try {
                // Define un estilo sin flechas
                String estilo = "endArrow=none;startArrow=none;";
                Object edge = graph.insertEdge(parent, null, coste, origenVisual, destinoVisual);
                if (coste == -1) {
                    // Cambiar el color a rojo si el valor es -1
                    graph.setCellStyle("endArrow=none;startArrow=none;strokeColor=#800080", new Object[]{edge});
                }else{
                    graph.setCellStyle(estilo, new Object[]{edge});
                }
                super.agregarArista(origen, destino, coste, edge); // De origen a destino
            } finally {
                graph.getModel().endUpdate();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Asegúrate de que ambos nodos existan.");
        }
    }

    private void actualizarAristaVista(Object edge) {
        graph.getModel().beginUpdate();
        try {
            graph.getModel().setValue(edge, -1);
            graph.setCellStyle("endArrow=none;startArrow=none;strokeColor=#800080", new Object[]{edge});
        } finally {
            graph.getModel().endUpdate();
        }
    }

    public void resetearStyleAristas(){
        graph.getModel().beginUpdate();
        try {
            // Definir el nuevo color (por ejemplo, azul)
            String edgeStyle = "endArrow=none;startArrow=none;noEdgeStyle=1"; // Código hexadecimal para azul

            // Obtener todos los vértices en el grafo
            Object[] vertices = graph.getChildVertices(parent);

            // Iterar sobre cada vértice y obtener sus aristas
            for (Object vertex : vertices) {
                Object[] edges = graph.getEdges(vertex);

                for (Object e : edges) {
                    Object edgeValue = graph.getModel().getValue(e);

                    if (edgeValue != null && edgeValue.toString().equals("-1")) {
                        // Cambiar el color a rojo si el valor es -1
                        graph.setCellStyle("endArrow=none;startArrow=none;strokeColor=#800080", new Object[]{e});
                    }else{
                        graph.setCellStyle(edgeStyle, new Object[]{e});
                    }

                }
            }
        } finally {
            // Terminar la edición del grafo
            graph.getModel().endUpdate();
        }
    }


}
