package com.centroempresarial.reservasalas.service;

import com.centroempresarial.reservasalas.estructuras.ArbolBinarioSalas;
import com.centroempresarial.reservasalas.estructuras.Cola;
import com.centroempresarial.reservasalas.estructuras.ListaEnlazada;
import com.centroempresarial.reservasalas.exception.ReservaInvalidaException;
import com.centroempresarial.reservasalas.exception.SalaNoDisponibleException;
import com.centroempresarial.reservasalas.model.EstadoReserva;
import com.centroempresarial.reservasalas.model.Reserva;
import com.centroempresarial.reservasalas.model.Sala;

/**
 * Clase de servicio que centraliza la lógica de negocio del sistema
 * y coordina las tres estructuras de datos dinámicas requeridas:
 *
 *  - Árbol Binario de Búsqueda (ArbolBinarioSalas): consulta de salas
 *    por capacidad y disponibilidad horaria.
 *  - Lista Enlazada (ListaEnlazada): asistentes y equipos por reserva,
 *    y el historial de reservas ya procesadas (para el reporte).
 *  - Cola (Cola): solicitudes de reserva pendientes de aprobación,
 *    atendidas en orden de llegada (FIFO).
 *
 * Todos los datos se mantienen en memoria durante la ejecución,
 * cumpliendo con el requisito de no usar base de datos ni archivos.
 */
public class GestorReservas {

    private ArbolBinarioSalas arbolSalas;
    private Cola<Reserva> solicitudesPendientes;
    private ListaEnlazada<Reserva> historialReservas; // aprobadas y rechazadas

    public GestorReservas() {
        this.arbolSalas = new ArbolBinarioSalas();
        this.solicitudesPendientes = new Cola<>();
        this.historialReservas = new ListaEnlazada<>();
    }

    // ---------- Administración de salas ----------

    public void registrarSala(Sala sala) {
        arbolSalas.insertar(sala);
    }

    /** Funcionalidad 1: Consultar disponibilidad (búsqueda en el árbol). */
    public ListaEnlazada<Sala> consultarDisponibilidad(int capacidadRequerida, int hora)
            throws SalaNoDisponibleException {
        ListaEnlazada<Sala> encontradas = arbolSalas.buscarPorCapacidadYHora(capacidadRequerida, hora);
        if (encontradas.estaVacia()) {
            throw new SalaNoDisponibleException(
                    "No hay salas disponibles con capacidad >= " + capacidadRequerida +
                    " a las " + String.format("%02d:00", hora) + ".");
        }
        return encontradas;
    }

    // ---------- Funcionalidad 2: Realizar reserva (encolar solicitud) ----------

    public Reserva realizarReserva(String solicitante, int hora, int capacidadRequerida)
            throws ReservaInvalidaException {
        if (solicitante == null || solicitante.isBlank()) {
            throw new ReservaInvalidaException("El nombre del solicitante no puede estar vacío.");
        }
        if (hora < 0 || hora > 23) {
            throw new ReservaInvalidaException("La hora debe estar entre 0 y 23.");
        }
        if (capacidadRequerida <= 0) {
            throw new ReservaInvalidaException("El aforo requerido debe ser mayor a cero.");
        }
        Reserva nueva = new Reserva(solicitante, hora, capacidadRequerida);
        solicitudesPendientes.encolar(nueva);
        return nueva;
    }

    // ---------- Funcionalidad 3: Aprobar reserva (desencolar y asignar sala) ----------

    /**
     * Desencola la solicitud más antigua (FIFO), busca en el árbol una
     * sala que cumpla el aforo y esté libre a la hora solicitada, y de
     * existir, se la asigna y marca la hora como ocupada.
     * Si no hay sala disponible, la solicitud queda RECHAZADA.
     */
    public Reserva aprobarSiguienteReserva() throws ReservaInvalidaException {
        if (solicitudesPendientes.estaVacia()) {
            throw new ReservaInvalidaException("No hay solicitudes pendientes de aprobación.");
        }
        Reserva solicitud = solicitudesPendientes.desencolar();

        ListaEnlazada<Sala> candidatas =
                arbolSalas.buscarPorCapacidadYHora(solicitud.getCapacidadRequerida(), solicitud.getHora());

        if (candidatas.estaVacia()) {
            solicitud.setEstado(EstadoReserva.RECHAZADA);
        } else {
            // Se asigna la primera candidata (la de menor capacidad que cumple, gracias al in-orden del BST).
            Sala asignada = candidatas.iterator().next();
            asignada.ocuparHora(solicitud.getHora());
            solicitud.setSala(asignada);
            solicitud.setEstado(EstadoReserva.APROBADA);
        }

        historialReservas.agregar(solicitud);
        return solicitud;
    }

    // ---------- Funcionalidad 4: Reporte de uso ----------

    public ListaEnlazada<Reserva> getHistorialReservas() {
        return historialReservas;
    }

    public int contarPorEstado(EstadoReserva estado) {
        int contador = 0;
        for (Reserva r : historialReservas) {
            if (r.getEstado() == estado) contador++;
        }
        return contador;
    }

    public ListaEnlazada<Sala> getTodasLasSalas() {
        return arbolSalas.listarTodasEnOrden();
    }

    public Cola<Reserva> getSolicitudesPendientes() {
        return solicitudesPendientes;
    }
}
