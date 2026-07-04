package com.centroempresarial.reservasalas.estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementación propia de una Lista Simplemente Enlazada genérica.
 * Se utiliza en el sistema para mantener, dentro de cada reserva,
 * la lista de asistentes convocados y la lista de equipos requeridos.
 *
 * No se utiliza java.util.LinkedList: la estructura se construye
 * manualmente con nodos (NodoLista) enlazados por referencia,
 * cumpliendo con el requisito de "Estructuras de Datos Dinámicas".
 *
 * @param <T> tipo de dato almacenado en la lista
 */
public class ListaEnlazada<T> implements Iterable<T> {

    private NodoLista<T> cabeza;
    private NodoLista<T> cola;
    private int tamanio;

    public ListaEnlazada() {
        this.cabeza = null;
        this.cola = null;
        this.tamanio = 0;
    }

    /** Inserta un elemento al final de la lista. Complejidad O(1). */
    public void agregar(T dato) {
        NodoLista<T> nuevo = new NodoLista<>(dato);
        if (estaVacia()) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.setSiguiente(nuevo);
            cola = nuevo;
        }
        tamanio++;
    }

    /** Elimina la primera ocurrencia de un elemento. Devuelve true si lo encontró. */
    public boolean eliminar(T dato) {
        if (estaVacia()) return false;

        if (cabeza.getDato().equals(dato)) {
            cabeza = cabeza.getSiguiente();
            if (cabeza == null) cola = null;
            tamanio--;
            return true;
        }

        NodoLista<T> actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato().equals(dato)) {
                NodoLista<T> eliminado = actual.getSiguiente();
                actual.setSiguiente(eliminado.getSiguiente());
                if (eliminado == cola) cola = actual;
                tamanio--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private NodoLista<T> actual = cabeza;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException("No hay más elementos en la lista.");
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }

    @Override
    public String toString() {
        if (estaVacia()) return "(vacía)";
        StringBuilder sb = new StringBuilder();
        NodoLista<T> actual = cabeza;
        while (actual != null) {
            sb.append("- ").append(actual.getDato().toString());
            if (actual.getSiguiente() != null) sb.append("\n");
            actual = actual.getSiguiente();
        }
        return sb.toString();
    }
}
