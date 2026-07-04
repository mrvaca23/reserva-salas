package com.centroempresarial.reservasalas.model;

import com.centroempresarial.reservasalas.estructuras.ListaEnlazada;

/**
 * Representa una solicitud de reserva de sala de reuniones.
 * Contiene la lista enlazada de asistentes convocados y la lista
 * enlazada de equipos/recursos requeridos para la reunión.
 */
public class Reserva {

    private static int contador = 1;

    private int id;
    private Sala sala;
    private String solicitante;
    private int hora; // hora solicitada (0-23)
    private int capacidadRequerida;
    private ListaEnlazada<Asistente> asistentes;
    private ListaEnlazada<Equipo> equipos;
    private EstadoReserva estado;

    public Reserva(String solicitante, int hora, int capacidadRequerida) {
        this.id = contador++;
        this.solicitante = solicitante;
        this.hora = hora;
        this.capacidadRequerida = capacidadRequerida;
        this.asistentes = new ListaEnlazada<>();
        this.equipos = new ListaEnlazada<>();
        this.estado = EstadoReserva.PENDIENTE;
        this.sala = null; // se asigna al momento de aprobar la reserva
    }

    public int getId() {
        return id;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public int getHora() {
        return hora;
    }

    public int getCapacidadRequerida() {
        return capacidadRequerida;
    }

    public ListaEnlazada<Asistente> getAsistentes() {
        return asistentes;
    }

    public ListaEnlazada<Equipo> getEquipos() {
        return equipos;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        String salaTxt = (sala != null) ? sala.getNombre() : "(sin asignar)";
        return String.format(
                "Solicitud #%d | Solicitante: %s | Hora: %02d:00 | Aforo requerido: %d | Sala: %s | Estado: %s",
                id, solicitante, hora, capacidadRequerida, salaTxt, estado);
    }
}
