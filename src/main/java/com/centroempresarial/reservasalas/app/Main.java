package com.centroempresarial.reservasalas.app;

import com.centroempresarial.reservasalas.estructuras.ListaEnlazada;
import com.centroempresarial.reservasalas.exception.ReservaInvalidaException;
import com.centroempresarial.reservasalas.exception.SalaNoDisponibleException;
import com.centroempresarial.reservasalas.model.Asistente;
import com.centroempresarial.reservasalas.model.EstadoReserva;
import com.centroempresarial.reservasalas.model.Equipo;
import com.centroempresarial.reservasalas.model.Reserva;
import com.centroempresarial.reservasalas.model.Sala;
import com.centroempresarial.reservasalas.service.GestorReservas;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Sistema de Reserva de Salas de Reuniones - Centro Empresarial.
 *
 * Punto de entrada de la aplicación de consola. Presenta un menú con
 * las 4 funcionalidades principales solicitadas:
 *   1. Consultar disponibilidad
 *   2. Realizar reserva
 *   3. Aprobar reserva
 *   4. Generar reporte de uso
 *
 * Toda la información vive en memoria durante la ejecución del programa.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final GestorReservas gestor = new GestorReservas();

    public static void main(String[] args) {
        cargarDatosIniciales();

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opción: ");
            try {
                switch (opcion) {
                    case 1 -> consultarDisponibilidad();
                    case 2 -> realizarReserva();
                    case 3 -> aprobarReserva();
                    case 4 -> generarReporteUso();
                    case 0 -> {
                        salir = true;
                        System.out.println("\nGracias por usar el Sistema de Reserva de Salas. ¡Hasta pronto!");
                    }
                    default -> System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (SalaNoDisponibleException | ReservaInvalidaException e) {
                // Manejo de excepciones propias del dominio del negocio.
                System.out.println("\n[AVISO] " + e.getMessage());
            } catch (Exception e) {
                // Red de seguridad ante cualquier error inesperado.
                System.out.println("\n[ERROR] Ocurrió un problema inesperado: " + e.getMessage());
            }
            System.out.println();
        }
        sc.close();
    }

    private static void cargarDatosIniciales() {
        gestor.registrarSala(new Sala("S01", "Sala Andes", 4, "Piso 1"));
        gestor.registrarSala(new Sala("S02", "Sala Pacífico", 8, "Piso 1"));
        gestor.registrarSala(new Sala("S03", "Sala Amazonas", 12, "Piso 2"));
        gestor.registrarSala(new Sala("S04", "Sala Cusco", 20, "Piso 2"));
        gestor.registrarSala(new Sala("S05", "Sala Lima", 6, "Piso 3"));
        gestor.registrarSala(new Sala("S06", "Sala Titicaca", 30, "Piso 3"));
        System.out.println("Sistema iniciado. " + gestor.getTodasLasSalas().getTamanio() + " salas registradas.\n");
    }

    private static void mostrarMenu() {
        System.out.println("=========================================");
        System.out.println(" SISTEMA DE RESERVA DE SALAS DE REUNIONES");
        System.out.println("           Centro Empresarial");
        System.out.println("=========================================");
        System.out.println("1. Consultar disponibilidad");
        System.out.println("2. Realizar reserva");
        System.out.println("3. Aprobar reserva");
        System.out.println("4. Generar reporte de uso");
        System.out.println("0. Salir");
    }

    // ---------- Opción 1 ----------
    private static void consultarDisponibilidad() throws SalaNoDisponibleException {
        System.out.println("\n--- Consultar Disponibilidad ---");
        int hora = leerEntero("Hora deseada (0-23): ");
        int capacidad = leerEntero("Aforo requerido (N° de personas): ");

        ListaEnlazada<Sala> disponibles = gestor.consultarDisponibilidad(capacidad, hora);
        System.out.println("\nSalas disponibles (ordenadas de menor a mayor capacidad):");
        System.out.println(disponibles);
    }

    // ---------- Opción 2 ----------
    private static void realizarReserva() throws ReservaInvalidaException {
        System.out.println("\n--- Realizar Reserva ---");
        System.out.print("Nombre del solicitante: ");
        String solicitante = sc.nextLine();
        int hora = leerEntero("Hora deseada (0-23): ");
        int capacidad = leerEntero("Aforo requerido (N° de personas): ");

        Reserva reserva = gestor.realizarReserva(solicitante, hora, capacidad);

        int numAsistentes = leerEntero("¿Cuántos asistentes desea registrar?: ");
        for (int i = 1; i <= numAsistentes; i++) {
            System.out.println("Asistente " + i + ":");
            System.out.print("  Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("  Correo: ");
            String correo = sc.nextLine();
            System.out.print("  Área: ");
            String area = sc.nextLine();
            reserva.getAsistentes().agregar(new Asistente(nombre, correo, area));
        }

        int numEquipos = leerEntero("¿Cuántos equipos/recursos requiere?: ");
        for (int i = 1; i <= numEquipos; i++) {
            System.out.print("  Nombre del equipo (ej. proyector): ");
            String nombreEq = sc.nextLine();
            int cantidad = leerEntero("  Cantidad: ");
            reserva.getEquipos().agregar(new Equipo(nombreEq, cantidad));
        }

        System.out.println("\nSolicitud registrada y encolada correctamente:");
        System.out.println(reserva);
        System.out.println("Solicitudes pendientes en cola: " + gestor.getSolicitudesPendientes().getTamanio());
    }

    // ---------- Opción 3 ----------
    private static void aprobarReserva() throws ReservaInvalidaException {
        System.out.println("\n--- Aprobar Reserva (siguiente en cola) ---");
        Reserva procesada = gestor.aprobarSiguienteReserva();

        if (procesada.getEstado() == EstadoReserva.APROBADA) {
            System.out.println("Solicitud APROBADA. Sala asignada: " + procesada.getSala());
        } else {
            System.out.println("Solicitud RECHAZADA: no hay sala disponible que cumpla los requisitos.");
        }
        System.out.println(procesada);

        if (!procesada.getAsistentes().estaVacia()) {
            System.out.println("Asistentes convocados:");
            System.out.println(procesada.getAsistentes());
        }
        if (!procesada.getEquipos().estaVacia()) {
            System.out.println("Equipos requeridos:");
            System.out.println(procesada.getEquipos());
        }
    }

    // ---------- Opción 4 ----------
    private static void generarReporteUso() {
        System.out.println("\n--- Reporte de Uso de Salas ---");
        ListaEnlazada<Reserva> historial = gestor.getHistorialReservas();

        if (historial.estaVacia()) {
            System.out.println("Aún no se ha procesado ninguna solicitud (historial vacío).");
            return;
        }

        int aprobadas = gestor.contarPorEstado(EstadoReserva.APROBADA);
        int rechazadas = gestor.contarPorEstado(EstadoReserva.RECHAZADA);

        System.out.println("Total de solicitudes procesadas: " + historial.getTamanio());
        System.out.println("  - Aprobadas : " + aprobadas);
        System.out.println("  - Rechazadas: " + rechazadas);
        System.out.println("Solicitudes aún pendientes en cola: " + gestor.getSolicitudesPendientes().getTamanio());

        System.out.println("\nDetalle de solicitudes procesadas:");
        System.out.println(historial);

        System.out.println("\nEstado actual de todas las salas (ordenadas por capacidad):");
        System.out.println(gestor.getTodasLasSalas());
    }

    // ---------- Utilidad de lectura segura ----------
    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                int valor = Integer.parseInt(sc.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor ingrese un número entero.");
            }
        }
    }
}
