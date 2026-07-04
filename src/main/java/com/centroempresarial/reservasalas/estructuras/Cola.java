package com.centroempresarial.reservasalas.estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementación propia de una Cola (FIFO - First In, First Out) genérica,
 * basada en nodos enlazados.
 *
 * En el sistema se utiliza para gestionar las solicitudes de reserva
 * pendientes de aprobación: cada nueva solicitud se encola (enqueue)
 * y el administrador las procesa en el mismo orden en que llegaron
 * (dequeue), tal como se atendería una fila física.
 *
 * @param <T> tipo de dato almacenado en la cola
 */
public class Cola<T> implements Iterable<T> {

    private NodoCola<T> frente;
    private NodoCola<T> final_;
    private int tamanio;

    public Cola() {
        this.frente = null;
        this.final_ = null;
        this.tamanio = 0;
    }

    /** Inserta un elemento al final de la cola. Complejidad O(1). */
    public void encolar(T dato) {
        NodoCola<T> nuevo = new NodoCola<>(dato);
        if (estaVacia()) {
            frente = nuevo;
            final_ = nuevo;
        } else {
            final_.setSiguiente(nuevo);
            final_ = nuevo;
        }
        tamanio++;
    }

    /** Extrae y retorna el elemento en el frente de la cola. Complejidad O(1). */
    public T desencolar() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola de solicitudes está vacía.");
        }
        T dato = frente.getDato();
        frente = frente.getSiguiente();
        if (frente == null) final_ = null;
        tamanio--;
        return dato;
    }

    /** Retorna el elemento en el frente sin extraerlo. */
    public T verFrente() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola de solicitudes está vacía.");
        }
        return frente.getDato();
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private NodoCola<T> actual = frente;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException("No hay más elementos en la cola.");
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }
}
