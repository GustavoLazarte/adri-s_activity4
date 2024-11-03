class Nodo {
    constructor(nombre) {
        this.nombre = nombre;
        this.conexiones = [];
    }
}

class Arista {
    constructor(destino, coste) {
        this.destino = destino;
        this.coste = coste;
    }
}

export class Grafo {
    constructor() {
        this.nodos = new Map();
    }

    agregarNodo(nombre) {
        this.nodos.set(nombre, new Nodo(nombre));
    }

    agregarArista(origen, destino, coste) {
        const nodoOrigen = this.nodos.get(origen);
        const nodoDestino = this.nodos.get(destino);
        if (nodoOrigen && nodoDestino) {
            nodoOrigen.conexiones.push(new Arista(nodoDestino, coste));
        }
    }

    maximizarValor(inicio, destinoFinal) {
        const nodoInicio = this.nodos.get(inicio);
        const valor = new Map();
        const camino = new Map();
        const queue = new PriorityQueue((a, b) => valor.get(b) - valor.get(a));

        valor.set(nodoInicio, 0);
        queue.add(nodoInicio);

        while (!queue.isEmpty()) {
            const nodoActual = queue.poll();

            for (const arista of nodoActual.conexiones) {
                if (arista.coste === -1) {
                    continue;
                }

                const nuevoValor = valor.get(nodoActual) + arista.coste;

                if (nuevoValor > (valor.get(arista.destino) || 0)) {
                    valor.set(arista.destino, nuevoValor);
                    camino.set(arista.destino, nodoActual);
                    queue.add(arista.destino);
                }
            }
        }

        return this.reconstruirCamino(camino, destinoFinal);
    }

    minimizarCosto(inicio, destinoFinal) {
        const nodoInicio = this.nodos.get(inicio);
        const costo = new Map();
        const camino = new Map();
        const queue = new PriorityQueue((a, b) => costo.get(a) - costo.get(b));

        costo.set(nodoInicio, 0);
        queue.add(nodoInicio);

        while (!queue.isEmpty()) {
            const nodoActual = queue.poll();

            for (const arista of nodoActual.conexiones) {
                if (arista.coste === -1) {
                    continue;
                }

                const nuevoCosto = costo.get(nodoActual) + arista.coste;

                if (nuevoCosto < (costo.get(arista.destino) || Infinity)) {
                    costo.set(arista.destino, nuevoCosto);
                    camino.set(arista.destino, nodoActual);
                    queue.add(arista.destino);
                }
            }
        }

        return this.reconstruirCamino(camino, destinoFinal);
    }

    reconstruirCamino(camino, destinoFinal) {
        const mejorCamino = [];
        let nodoActual = this.nodos.get(destinoFinal);
        while (nodoActual) {
            mejorCamino.push(nodoActual);
            nodoActual = camino.get(nodoActual);
        }
        return mejorCamino.reverse(); // Invertir para obtener el camino correcto
    }
    obtenerAristasDelCamino(camino) {
        const aristas = [];
        for (let i = 0; i < camino.length - 1; i++) {
            const origen = camino[i];
            const destino = camino[i + 1];
            const arista = origen.conexiones.find(a => a.destino === destino);
            if (arista) {
                aristas.push({ from: origen, to: destino });
            }
        }
        return aristas;
    }
}

// Clase para manejar la cola de prioridad
class PriorityQueue {
    constructor(comparator) {
        this.comparator = comparator;
        this.elements = [];
    }

    add(element) {
        this.elements.push(element);
        this.elements.sort(this.comparator);
    }

    poll() {
        return this.elements.shift();
    }

    isEmpty() {
        return this.elements.length === 0;
    }
}
