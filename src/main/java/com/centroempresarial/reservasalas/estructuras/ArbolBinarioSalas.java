package com.centroempresarial.reservasalas.estructuras;

import com.centroempresarial.reservasalas.model.Sala;

/**
 * Árbol Binario de Búsqueda (BST) de salas, ordenado por capacidad.
 *
 * Se utiliza para responder de forma eficiente a la consulta principal
 * del sistema: "¿qué salas cumplen con al menos cierta capacidad y
 * están disponibles a cierta hora?". El recorrido in-orden entrega
 * las salas ordenadas de menor a mayor capacidad, lo que permite
 * encontrar rápidamente la sala más ajustada al requerimiento
 * (evitando desperdiciar salas grandes en reuniones pequeñas).
 *
 * Todas las salas quedan en memoria (no se usan archivos ni BD).
 */
public class ArbolBinarioSalas {

    private NodoArbol raiz;
    private int totalSalas;

    public ArbolBinarioSalas() {
        this.raiz = null;
        this.totalSalas = 0;
    }

    /** Inserta una nueva sala en el árbol, según su capacidad. */
    public void insertar(Sala sala) {
        raiz = insertarRec(raiz, sala);
        totalSalas++;
    }

    private NodoArbol insertarRec(NodoArbol nodo, Sala sala) {
        if (nodo == null) {
            return new NodoArbol(sala);
        }
        if (sala.getCapacidad() < nodo.getCapacidad()) {
            nodo.setIzquierdo(insertarRec(nodo.getIzquierdo(), sala));
        } else if (sala.getCapacidad() > nodo.getCapacidad()) {
            nodo.setDerecho(insertarRec(nodo.getDerecho(), sala));
        } else {
            // Misma capacidad exacta: se agrega a la lista de salas del nodo.
            nodo.getSalas().agregar(sala);
        }
        return nodo;
    }

    /**
     * Busca salas cuya capacidad sea mayor o igual a la requerida y que
     * además estén disponibles en la hora indicada.
     * Recorre el árbol en-orden (izquierda, raíz, derecha) para que el
     * resultado quede ordenado de menor a mayor capacidad; así la
     * primera coincidencia es la sala "más ajustada" al aforo pedido.
     */
    public ListaEnlazada<Sala> buscarPorCapacidadYHora(int capacidadRequerida, int hora) {
        ListaEnlazada<Sala> resultado = new ListaEnlazada<>();
        buscarRec(raiz, capacidadRequerida, hora, resultado);
        return resultado;
    }

    private void buscarRec(NodoArbol nodo, int capacidadRequerida, int hora, ListaEnlazada<Sala> resultado) {
        if (nodo == null) return;

        // Si la capacidad requerida es menor, aún puede haber coincidencias
        // en el subárbol izquierdo (capacidades menores) que igual cumplan,
        // por eso siempre se explora izquierda primero en un in-orden completo
        // cuando la capacidad del nodo es mayor o igual a la requerida.
        buscarRec(nodo.getIzquierdo(), capacidadRequerida, hora, resultado);

        if (nodo.getCapacidad() >= capacidadRequerida) {
            for (Sala s : nodo.getSalas()) {
                if (s.estaDisponible(hora)) {
                    resultado.agregar(s);
                }
            }
        }

        buscarRec(nodo.getDerecho(), capacidadRequerida, hora, resultado);
    }

    /** Recorrido in-orden completo: todas las salas ordenadas por capacidad. */
    public ListaEnlazada<Sala> listarTodasEnOrden() {
        ListaEnlazada<Sala> resultado = new ListaEnlazada<>();
        inOrdenRec(raiz, resultado);
        return resultado;
    }

    private void inOrdenRec(NodoArbol nodo, ListaEnlazada<Sala> resultado) {
        if (nodo == null) return;
        inOrdenRec(nodo.getIzquierdo(), resultado);
        for (Sala s : nodo.getSalas()) resultado.agregar(s);
        inOrdenRec(nodo.getDerecho(), resultado);
    }

    public int getTotalSalas() {
        return totalSalas;
    }
}
