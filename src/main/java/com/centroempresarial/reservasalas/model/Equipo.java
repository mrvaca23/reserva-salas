package com.centroempresarial.reservasalas.model;

/**
 * Representa un recurso o equipo requerido para una reunión
 * (ej. proyector, laptop, ecran, pizarra digital).
 * Las instancias se almacenan en una Lista Enlazada dentro de cada Reserva.
 */
public class Equipo {

    private String nombre;
    private int cantidad;

    public Equipo(String nombre, int cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    @Override
    public String toString() {
        return cantidad + "x " + nombre;
    }
}
