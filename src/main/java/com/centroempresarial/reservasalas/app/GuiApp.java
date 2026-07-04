package com.centroempresarial.reservasalas.app;

import com.centroempresarial.reservasalas.estructuras.ListaEnlazada;
import com.centroempresarial.reservasalas.exception.ReservaInvalidaException;
import com.centroempresarial.reservasalas.exception.SalaNoDisponibleException;
import com.centroempresarial.reservasalas.model.Asistente;
import com.centroempresarial.reservasalas.model.Equipo;
import com.centroempresarial.reservasalas.model.EstadoReserva;
import com.centroempresarial.reservasalas.model.Reserva;
import com.centroempresarial.reservasalas.model.Sala;
import com.centroempresarial.reservasalas.service.GestorReservas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Interfaz Gráfica (Java Swing) del Sistema de Reserva de Salas de Reuniones.
 * Corresponde a los "Puntos Extra" solicitados en el documento del proyecto.
 *
 * Esta clase es únicamente la capa de presentación: toda la lógica de
 * negocio y las estructuras de datos dinámicas (Árbol, Lista Enlazada,
 * Cola) residen en GestorReservas, exactamente igual que en la versión
 * de consola (Main.java). Ambas interfaces pueden usarse indistintamente.
 */
public class GuiApp extends JFrame {

    private final GestorReservas gestor = new GestorReservas();

    // Referencia a la reserva que se está armando en la pestaña "Realizar reserva"
    private Reserva reservaEnCurso;

    // --- Componentes pestaña 1: Consultar disponibilidad ---
    private JSpinner spHoraConsulta;
    private JSpinner spAforoConsulta;
    private JTable tablaDisponibilidad;
    private DefaultTableModel modeloDisponibilidad;

    // --- Componentes pestaña 2: Realizar reserva ---
    private JTextField txtSolicitante;
    private JSpinner spHoraReserva;
    private JSpinner spAforoReserva;
    private JLabel lblReservaActual;
    private JTextField txtAsistNombre, txtAsistCorreo, txtAsistArea;
    private JTextField txtEquipoNombre;
    private JSpinner spEquipoCantidad;
    private DefaultListModel<String> modeloAsistentes;
    private DefaultListModel<String> modeloEquipos;

    // --- Componentes pestaña 3: Aprobar reserva ---
    private JTextArea areaAprobacion;
    private JLabel lblPendientes;

    // --- Componentes pestaña 4: Reporte de uso ---
    private JLabel lblResumen;
    private JTable tablaHistorial;
    private DefaultTableModel modeloHistorial;
    private JTable tablaSalas;
    private DefaultTableModel modeloSalas;

    public GuiApp() {
        super("Sistema de Reserva de Salas de Reuniones - Centro Empresarial");
        cargarDatosIniciales();
        construirInterfaz();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(880, 620);
        setLocationRelativeTo(null);
    }

    private void cargarDatosIniciales() {
        gestor.registrarSala(new Sala("S01", "Sala Andes", 4, "Piso 1"));
        gestor.registrarSala(new Sala("S02", "Sala Pacífico", 8, "Piso 1"));
        gestor.registrarSala(new Sala("S03", "Sala Amazonas", 12, "Piso 2"));
        gestor.registrarSala(new Sala("S04", "Sala Cusco", 20, "Piso 2"));
        gestor.registrarSala(new Sala("S05", "Sala Lima", 6, "Piso 3"));
        gestor.registrarSala(new Sala("S06", "Sala Titicaca", 30, "Piso 3"));
    }

    private void construirInterfaz() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("1. Consultar disponibilidad", construirPanelDisponibilidad());
        tabs.addTab("2. Realizar reserva", construirPanelReserva());
        tabs.addTab("3. Aprobar reserva", construirPanelAprobacion());
        tabs.addTab("4. Reporte de uso", construirPanelReporte());
        add(tabs);
    }

    // ================= PESTAÑA 1 =================
    private JPanel construirPanelDisponibilidad() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        spHoraConsulta = new JSpinner(new SpinnerNumberModel(9, 0, 23, 1));
        spAforoConsulta = new JSpinner(new SpinnerNumberModel(5, 1, 200, 1));
        JButton btnConsultar = new JButton("Consultar disponibilidad");

        form.add(new JLabel("Hora (0-23):"));
        form.add(spHoraConsulta);
        form.add(new JLabel("Aforo requerido:"));
        form.add(spAforoConsulta);
        form.add(btnConsultar);

        modeloDisponibilidad = new DefaultTableModel(
                new Object[]{"Código", "Nombre", "Capacidad", "Piso"}, 0);
        tablaDisponibilidad = new JTable(modeloDisponibilidad);

        btnConsultar.addActionListener(e -> {
            int hora = (int) spHoraConsulta.getValue();
            int aforo = (int) spAforoConsulta.getValue();
            modeloDisponibilidad.setRowCount(0);
            try {
                ListaEnlazada<Sala> salas = gestor.consultarDisponibilidad(aforo, hora);
                for (Sala s : salas) {
                    modeloDisponibilidad.addRow(new Object[]{
                            s.getCodigo(), s.getNombre(), s.getCapacidad(), s.getPiso()});
                }
            } catch (SalaNoDisponibleException ex) {
                mostrarAviso(ex.getMessage());
            }
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaDisponibilidad), BorderLayout.CENTER);
        return panel;
    }

    // ================= PESTAÑA 2 =================
    private JPanel construirPanelReserva() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Sub-panel: datos de la solicitud ---
        JPanel panelSolicitud = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSolicitante = new JTextField(15);
        spHoraReserva = new JSpinner(new SpinnerNumberModel(9, 0, 23, 1));
        spAforoReserva = new JSpinner(new SpinnerNumberModel(5, 1, 200, 1));
        JButton btnCrearSolicitud = new JButton("Crear solicitud (encolar)");

        panelSolicitud.add(new JLabel("Solicitante:"));
        panelSolicitud.add(txtSolicitante);
        panelSolicitud.add(new JLabel("Hora:"));
        panelSolicitud.add(spHoraReserva);
        panelSolicitud.add(new JLabel("Aforo:"));
        panelSolicitud.add(spAforoReserva);
        panelSolicitud.add(btnCrearSolicitud);

        lblReservaActual = new JLabel("Aún no se ha creado ninguna solicitud.");

        // --- Sub-panel: asistentes ---
        JPanel panelAsistentes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtAsistNombre = new JTextField(10);
        txtAsistCorreo = new JTextField(10);
        txtAsistArea = new JTextField(8);
        JButton btnAgregarAsistente = new JButton("Agregar asistente");
        panelAsistentes.add(new JLabel("Nombre:"));
        panelAsistentes.add(txtAsistNombre);
        panelAsistentes.add(new JLabel("Correo:"));
        panelAsistentes.add(txtAsistCorreo);
        panelAsistentes.add(new JLabel("Área:"));
        panelAsistentes.add(txtAsistArea);
        panelAsistentes.add(btnAgregarAsistente);

        modeloAsistentes = new DefaultListModel<>();
        JList<String> listaAsistentes = new JList<>(modeloAsistentes);

        // --- Sub-panel: equipos ---
        JPanel panelEquipos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtEquipoNombre = new JTextField(12);
        spEquipoCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        JButton btnAgregarEquipo = new JButton("Agregar equipo");
        panelEquipos.add(new JLabel("Equipo:"));
        panelEquipos.add(txtEquipoNombre);
        panelEquipos.add(new JLabel("Cantidad:"));
        panelEquipos.add(spEquipoCantidad);
        panelEquipos.add(btnAgregarEquipo);

        modeloEquipos = new DefaultListModel<>();
        JList<String> listaEquipos = new JList<>(modeloEquipos);

        JPanel panelListas = new JPanel(new GridLayout(1, 2, 10, 0));
        panelListas.add(envolverConTitulo("Asistentes convocados", new JScrollPane(listaAsistentes)));
        panelListas.add(envolverConTitulo("Equipos requeridos", new JScrollPane(listaEquipos)));

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.add(panelSolicitud);
        panelSuperior.add(lblReservaActual);
        panelSuperior.add(new JSeparator());
        panelSuperior.add(panelAsistentes);
        panelSuperior.add(panelEquipos);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(panelListas, BorderLayout.CENTER);

        // --- Listeners ---
        btnCrearSolicitud.addActionListener(e -> {
            try {
                String solicitante = txtSolicitante.getText().trim();
                int hora = (int) spHoraReserva.getValue();
                int aforo = (int) spAforoReserva.getValue();
                reservaEnCurso = gestor.realizarReserva(solicitante, hora, aforo);
                modeloAsistentes.clear();
                modeloEquipos.clear();
                lblReservaActual.setText("Solicitud creada: " + reservaEnCurso +
                        " | En cola: " + gestor.getSolicitudesPendientes().getTamanio());
            } catch (ReservaInvalidaException ex) {
                mostrarAviso(ex.getMessage());
            }
        });

        btnAgregarAsistente.addActionListener(e -> {
            if (reservaEnCurso == null) {
                mostrarAviso("Primero debe crear la solicitud de reserva.");
                return;
            }
            String nombre = txtAsistNombre.getText().trim();
            if (nombre.isEmpty()) {
                mostrarAviso("Ingrese el nombre del asistente.");
                return;
            }
            Asistente a = new Asistente(nombre, txtAsistCorreo.getText().trim(), txtAsistArea.getText().trim());
            reservaEnCurso.getAsistentes().agregar(a);
            modeloAsistentes.addElement(a.toString());
            txtAsistNombre.setText("");
            txtAsistCorreo.setText("");
            txtAsistArea.setText("");
        });

        btnAgregarEquipo.addActionListener(e -> {
            if (reservaEnCurso == null) {
                mostrarAviso("Primero debe crear la solicitud de reserva.");
                return;
            }
            String nombreEq = txtEquipoNombre.getText().trim();
            if (nombreEq.isEmpty()) {
                mostrarAviso("Ingrese el nombre del equipo.");
                return;
            }
            Equipo eq = new Equipo(nombreEq, (int) spEquipoCantidad.getValue());
            reservaEnCurso.getEquipos().agregar(eq);
            modeloEquipos.addElement(eq.toString());
            txtEquipoNombre.setText("");
        });

        return panel;
    }

    // ================= PESTAÑA 3 =================
    private JPanel construirPanelAprobacion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblPendientes = new JLabel();
        actualizarLabelPendientes();

        JButton btnAprobar = new JButton("Aprobar siguiente solicitud en cola");
        areaAprobacion = new JTextArea(18, 60);
        areaAprobacion.setEditable(false);
        areaAprobacion.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(btnAprobar);
        top.add(lblPendientes);

        btnAprobar.addActionListener(e -> {
            try {
                Reserva procesada = gestor.aprobarSiguienteReserva();
                StringBuilder sb = new StringBuilder();
                if (procesada.getEstado() == EstadoReserva.APROBADA) {
                    sb.append("APROBADA. Sala asignada: ").append(procesada.getSala()).append("\n");
                } else {
                    sb.append("RECHAZADA: no hay sala disponible que cumpla los requisitos.\n");
                }
                sb.append(procesada).append("\n");
                if (!procesada.getAsistentes().estaVacia()) {
                    sb.append("Asistentes:\n").append(procesada.getAsistentes()).append("\n");
                }
                if (!procesada.getEquipos().estaVacia()) {
                    sb.append("Equipos:\n").append(procesada.getEquipos()).append("\n");
                }
                sb.append("----------------------------------------\n");
                areaAprobacion.append(sb.toString());
                actualizarLabelPendientes();
            } catch (ReservaInvalidaException ex) {
                mostrarAviso(ex.getMessage());
            }
        });

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaAprobacion), BorderLayout.CENTER);
        return panel;
    }

    private void actualizarLabelPendientes() {
        lblPendientes.setText("Solicitudes pendientes en cola: " + gestor.getSolicitudesPendientes().getTamanio());
    }

    // ================= PESTAÑA 4 =================
    private JPanel construirPanelReporte() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnGenerar = new JButton("Generar reporte de uso");
        lblResumen = new JLabel("Presione el botón para generar el reporte.");

        modeloHistorial = new DefaultTableModel(
                new Object[]{"#", "Solicitante", "Hora", "Aforo", "Sala", "Estado"}, 0);
        tablaHistorial = new JTable(modeloHistorial);

        modeloSalas = new DefaultTableModel(
                new Object[]{"Código", "Nombre", "Capacidad", "Piso"}, 0);
        tablaSalas = new JTable(modeloSalas);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                envolverConTitulo("Historial de solicitudes procesadas", new JScrollPane(tablaHistorial)),
                envolverConTitulo("Salas registradas (ordenadas por capacidad)", new JScrollPane(tablaSalas)));
        split.setResizeWeight(0.5);

        JPanel top = new JPanel(new BorderLayout());
        top.add(btnGenerar, BorderLayout.WEST);
        top.add(lblResumen, BorderLayout.CENTER);

        btnGenerar.addActionListener(e -> {
            modeloHistorial.setRowCount(0);
            modeloSalas.setRowCount(0);

            for (Reserva r : gestor.getHistorialReservas()) {
                modeloHistorial.addRow(new Object[]{
                        r.getId(), r.getSolicitante(), String.format("%02d:00", r.getHora()),
                        r.getCapacidadRequerida(),
                        r.getSala() != null ? r.getSala().getNombre() : "-",
                        r.getEstado()});
            }
            for (Sala s : gestor.getTodasLasSalas()) {
                modeloSalas.addRow(new Object[]{s.getCodigo(), s.getNombre(), s.getCapacidad(), s.getPiso()});
            }

            int aprobadas = gestor.contarPorEstado(EstadoReserva.APROBADA);
            int rechazadas = gestor.contarPorEstado(EstadoReserva.RECHAZADA);
            int total = gestor.getHistorialReservas().getTamanio();
            lblResumen.setText(String.format(
                    "Total procesadas: %d | Aprobadas: %d | Rechazadas: %d | Pendientes en cola: %d",
                    total, aprobadas, rechazadas, gestor.getSolicitudesPendientes().getTamanio()));
        });

        panel.add(top, BorderLayout.NORTH);
        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    // ================= Utilidades =================
    private JPanel envolverConTitulo(String titulo, Component comp) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(titulo));
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private void mostrarAviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        // Usa el Look and Feel nativo del sistema operativo si está disponible.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Si falla, se usa el Look and Feel por defecto de Swing sin interrumpir el arranque.
        }
        SwingUtilities.invokeLater(() -> new GuiApp().setVisible(true));
    }
}
