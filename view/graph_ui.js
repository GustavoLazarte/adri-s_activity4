// ui.js
import { Grafo } from './graph.js';

const grafo = new Grafo();
const container = document.getElementById('network');

// Inicializar la red de Vis.js
const network = new vis.Network(container, grafo.obtenerDatosParaVisualizar(), {});

// Funciones para actualizar la visualización
export function agregarNodoDesdeScript(nombre) {
    grafo.agregarNodo(nombre);
    actualizarVisualizacion();
}

export function agregarAristaDesdeScript(origen, destino, coste) {
    grafo.agregarArista(origen, destino, coste);
    actualizarVisualizacion();
}

// Función para actualizar la visualización
function actualizarVisualizacion() {
    network.setData(grafo.obtenerDatosParaVisualizar());
}

// Lógica de la UI
const addNodeBtn = document.getElementById('addNodeBtn');
const addEdgeBtn = document.getElementById('addEdgeBtn');
const optimizeBtn = document.getElementById('optimizeBtn');
const maximizeBtn = document.getElementById('maximizeBtn');

addNodeBtn.addEventListener('click', () => {
    const nodeName = document.getElementById('nodeName').value;
    if (nodeName) {
        agregarNodoDesdeScript(nodeName);
        document.getElementById('nodeName').value = ''; // Limpiar campo
    }
});

addEdgeBtn.addEventListener('click', () => {
    const fromNode = document.getElementById('fromNode').value;
    const toNode = document.getElementById('toNode').value;
    const cost = document.getElementById('cost').value;

    if (fromNode && toNode && cost) {
        agregarAristaDesdeScript(fromNode, toNode, parseInt(cost));
        document.getElementById('fromNode').value = '';
        document.getElementById('toNode').value = '';
        document.getElementById('cost').value = '';
    }
});

// optimizeBtn.addEventListener('click', () => {
//     const inicio = document.getElementById('fromNode').value;
//     const destinoFinal = document.getElementById('toNode').value;
//
//     if (inicio && destinoFinal) {
//         grafo.maximizarValor(inicio, destinoFinal);
//         actualizarVisualizacion(); // Actualizar visualización
//     }
// });
//
// maximizeBtn.addEventListener('click', () => {
//     const inicio = document.getElementById('fromNode').value;
//     const destinoFinal = document.getElementById('toNode').value;
//
//     if (inicio && destinoFinal) {
//         grafo.maximizarValor(inicio, destinoFinal);
//         actualizarVisualizacion(); // Actualizar visualización
//     }
// });
