package com.centroempresarial.reservasalas.exception;

/**
 * Se lanza cuando no existe ninguna sala que cumpla con el aforo
 * requerido y esté disponible en la hora solicitada.
 */
public class SalaNoDisponibleException extends Exception {
    public SalaNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
