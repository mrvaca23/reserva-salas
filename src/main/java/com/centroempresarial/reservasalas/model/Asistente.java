package com.centroempresarial.reservasalas.model;

/**
 * Representa a una persona que asistirá a una reunión.
 * Las instancias de esta clase se almacenan en una Lista Enlazada
 * dentro de cada Reserva.
 */
public class Asistente {

    private String nombre;
    private String correo;
    private String area;

    public Asistente(String nombre, String correo, String area) {
        this.nombre = nombre;
        this.correo = correo;
        this.area = area;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getArea() {
        return area;
    }

    @Override
    public String toString() {
        return nombre + " (" + area + " - " + correo + ")";
    }
}
