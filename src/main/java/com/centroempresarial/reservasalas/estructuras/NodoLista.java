package com.centroempresarial.reservasalas.estructuras;

/**
 * Nodo utilizado por ListaEnlazada&lt;T&gt;.
 * Cada nodo almacena un dato de tipo genérico T y una referencia
 * al siguiente nodo de la lista (lista simplemente enlazada).
 */
public class NodoLista<T> {

    private T dato;
    private NodoLista<T> siguiente;

    public NodoLista(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public NodoLista<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoLista<T> siguiente) {
        this.siguiente = siguiente;
    }
}
