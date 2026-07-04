package com.centroempresarial.reservasalas.estructuras;

/**
 * Nodo utilizado por Cola&lt;T&gt;.
 */
public class NodoCola<T> {

    private T dato;
    private NodoCola<T> siguiente;

    public NodoCola(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public T getDato() {
        return dato;
    }

    public NodoCola<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoCola<T> siguiente) {
        this.siguiente = siguiente;
    }
}
