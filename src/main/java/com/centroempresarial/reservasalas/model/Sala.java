package com.centroempresarial.reservasalas.model;

/**
 * Representa una sala de reuniones del Centro Empresarial.
 * Cada sala tiene una capacidad (usada como clave de orden en el
 * Árbol Binario de Búsqueda) y una disponibilidad horaria simple
 * representada por un conjunto de horas del día (0 a 23) libres.
 */
public class Sala {

    private String codigo;
    private String nombre;
    private int capacidad;
    private String piso;
    // Disponibilidad horaria: true = libre, false = ocupada. Todo en memoria.
    private boolean[] disponibilidadHoraria;

    public Sala(String codigo, String nombre, int capacidad, String piso) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.piso = piso;
        this.disponibilidadHoraria = new boolean[24];
        // Por defecto, todas las horas entre 08:00 y 20:00 están libres.
        for (int h = 8; h <= 20; h++) {
            disponibilidadHoraria[h] = true;
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public String getPiso() {
        return piso;
    }

    /** Indica si la sala está libre en la hora indicada (formato 24h, 0-23). */
    public boolean estaDisponible(int hora) {
        if (hora < 0 || hora > 23) return false;
        return disponibilidadHoraria[hora];
    }

    /** Marca la hora indicada como ocupada. */
    public void ocuparHora(int hora) {
        if (hora >= 0 && hora <= 23) disponibilidadHoraria[hora] = false;
    }

    /** Libera la hora indicada (por ejemplo si una reserva es rechazada/cancelada). */
    public void liberarHora(int hora) {
        if (hora >= 0 && hora <= 23) disponibilidadHoraria[hora] = true;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Capacidad: %d personas | Piso: %s",
                codigo, nombre, capacidad, piso);
    }
}
