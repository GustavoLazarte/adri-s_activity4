import {Grafo} from "./graph.js";

const nodes = new vis.DataSet([]);
const edges = new vis.DataSet([]);

const container = document.getElementById('graphContainer');
const data = {
    nodes: nodes,
    edges: edges
};

const options = {
    interaction: {
        dragNodes: true, // Asegúrate de que el arrastre esté habilitado
        dragView: true // Permite mover la vista si es necesario
    },
    layout: {
        hierarchical: {
            direction: 'RL',
            sortMethod: 'directed'
        }
    },
    physics: {
        enabled: false // Desactiva la física para permitir movimiento libre
    }
};

const network = new vis.Network(container, data, options);

// Inicializar grafo
const grafo = new Grafo();

// Datos de prueba
const testNodes = [
    "Anzaldo",
    "Arani",
    "Ucureña",
    "Arbieto",
    "Tarata",
    "Tolata",
    "Cliza",
    "Punata",
    "San Benito",
    "San Antonio de Tiraque"
];

const testEdges = [
    { from: "Anzaldo", to: "Arani", cost: 20 },
    { from: "Anzaldo", to: "Punata", cost: 16 },
    { from: "Arani", to: "Ucureña", cost: 40 },
    { from: "Arani", to: "Cliza", cost: 12 },
    { from: "Arani", to: "San Benito", cost: 18 },
    { from: "Cliza", to: "San Benito", cost: -1 },
    { from: "Cliza", to: "Tolata", cost: 6 },
    { from: "Cliza", to: "Arbieto", cost: 20 },
    { from: "Punata", to: "San Benito", cost: 14 },
    { from: "Punata", to: "Ucureña", cost: 13 },
    { from: "Ucureña", to: "San Benito", cost: -1 },
    { from: "Ucureña", to: "Tolata", cost: 5 },
    { from: "Ucureña", to: "Arbieto", cost: 27 },
    { from: "Ucureña", to: "Tarata", cost: 10 },
    { from: "San Benito", to: "Tolata", cost: 12 },
    { from: "San Benito", to: "Tarata", cost: 23 },
    { from: "Tarata", to: "San Antonio de Tiraque", cost: 30 },
    { from: "Tolata", to: "San Antonio de Tiraque", cost: 18 },
    { from: "Arbieto", to: "San Antonio de Tiraque", cost: 22 }
];


// Función para agregar nodos y aristas de prueba
function addTestData() {
    // Agregar nodos de prueba
    testNodes.forEach(nodeName => {
        grafo.agregarNodo(nodeName);
        nodes.add({ id: nodes.length + 1, label: nodeName });
    });

    // Agregar aristas de prueba
    testEdges.forEach(edge => {
        grafo.agregarArista(edge.from, edge.to, edge.cost);
        edges.add({ from: getNodeId(edge.from), to: getNodeId(edge.to), label: edge.cost.toString(), color: '#848484' });
    });
}

// Llamar a la función para agregar datos de prueba al iniciar
addTestData();

// Función para agregar un nodo desde la UI
document.getElementById('addNodeBtn').addEventListener('click', () => {
    const nodeName = document.getElementById('nodeName').value;
    if (nodeName) {
        grafo.agregarNodo(nodeName);
        nodes.add({ id: nodes.length + 1, label: nodeName });
        document.getElementById('nodeName').value = ''; // Limpiar el campo de entrada
    } else {
        alert('Por favor, ingresa un nombre de nodo.');
    }
});

// Función para agregar una arista desde la UI
document.getElementById('addEdgeBtn').addEventListener('click', () => {
    const fromNode = document.getElementById('fromNode').value;
    const toNode = document.getElementById('toNode').value;
    const edgeCost = parseInt(document.getElementById('edgeCost').value);

    if (fromNode && toNode && !isNaN(edgeCost)) {
        grafo.agregarArista(fromNode, toNode, edgeCost);
        edges.add({ from: getNodeId(fromNode), to: getNodeId(toNode), label: edgeCost.toString(), color: '#848484' });
        document.getElementById('fromNode').value = '';
        document.getElementById('toNode').value = '';
        document.getElementById('edgeCost').value = '';
    } else {
        alert('Por favor, completa todos los campos.');
    }
});

// Función para obtener el ID de un nodo por su nombre
function getNodeId(nodeName) {
    const node = nodes.get().find(n => n.label === nodeName);
    return node ? node.id : null;
}

// Función para maximizar el valor
document.getElementById('maximizeBtn').addEventListener('click', () => {
    const startNode = document.getElementById('startNode').value;
    const endNode = document.getElementById('endNode').value;
    try {
        const result = grafo.maximizarValor(startNode, endNode);
        alert('Mejor camino para maximizar valor: ' + result.map(n => n.nombre).join(' -> '));

        // Cambiar el color de las aristas en el camino
        const aristas = grafo.obtenerAristasDelCamino(result);
        aristas.forEach(({ from, to }) => {
            const fromId = getNodeId(from.nombre);
            const toId = getNodeId(to.nombre);
            edges.update({ from: fromId, to: toId, color: { color: 'green' } }); // Cambia el color a verde
        });

    } catch (error) {
        alert(error.message);
    }
});

// Función para minimizar el costo
document.getElementById('minimizeBtn').addEventListener('click', () => {
    const startNode = document.getElementById('startNode').value;
    const endNode = document.getElementById('endNode').value;
    try {
        const result = grafo.minimizarCosto(startNode, endNode);
        alert('Mejor camino para minimizar costo: ' + result.map(n => n.nombre).join(' -> '));

        // Cambiar el color de las aristas en el camino
        const aristas = grafo.obtenerAristasDelCamino(result);
        aristas.forEach(({ from, to }) => {
            const fromId = getNodeId(from.nombre);
            const toId = getNodeId(to.nombre);
            edges.update({ from: fromId, to: toId, color: { color: 'red' } }); // Cambia el color a rojo
        });

    } catch (error) {
        alert(error.message);
    }
});
