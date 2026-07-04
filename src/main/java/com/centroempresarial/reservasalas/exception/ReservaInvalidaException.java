package com.centroempresarial.reservasalas.exception;

/**
 * Se lanza cuando los datos ingresados para una solicitud de reserva
 * son inválidos (por ejemplo, hora fuera de rango, aforo negativo,
 * o no existe ninguna solicitud pendiente para aprobar/rechazar).
 */
public class ReservaInvalidaException extends Exception {
    public ReservaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
