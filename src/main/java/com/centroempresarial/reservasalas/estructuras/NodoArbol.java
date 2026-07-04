package com.centroempresarial.reservasalas.estructuras;

import com.centroempresarial.reservasalas.model.Sala;

/**
 * Nodo del Árbol Binario de Búsqueda de salas.
 * La clave de ordenamiento es la capacidad de la sala.
 * Como puede haber varias salas con la misma capacidad, cada nodo
 * guarda una Lista Enlazada de salas que comparten esa capacidad.
 */
public class NodoArbol {

    private int capacidad;
    private ListaEnlazada<Sala> salas;
    private NodoArbol izquierdo;
    private NodoArbol derecho;

    public NodoArbol(Sala sala) {
        this.capacidad = sala.getCapacidad();
        this.salas = new ListaEnlazada<>();
        this.salas.agregar(sala);
        this.izquierdo = null;
        this.derecho = null;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public ListaEnlazada<Sala> getSalas() {
        return salas;
    }

    public NodoArbol getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoArbol izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoArbol getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoArbol derecho) {
        this.derecho = derecho;
    }
}
