package vista;

import datos.CuentaMD;
import modelo.Cuenta;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Ventana de gestión de cuentas del sistema financiero.
 * Capa: Vista (GUI).
 * Proporciona las pestañas CRUD (Agregar, Modificar, Eliminar, Consultar)
 * para la entidad Cuenta. Todas las operaciones filtran por el usuario
 * autenticado (ID_USUARIO_ACTIVO).
 *
 * @author Alejandro Flores
 */
public class VentanaCuentas extends JFrame {

    private JTabbedPane tabbedPane;

    // ── Pestaña Agregar ──────────────────────────────────────
    private JComboBox<String> cboTipoAgregar;
    private JTextField txtNombreAgregar;
    private JTextField txtSaldoAgregar;
    private JButton btnGuardarAgregar;
    private JButton btnLimpiarAgregar;

    // ── Pestaña Modificar ────────────────────────────────────
    private JTable tablaModificar;
    private DefaultTableModel modeloModificar;
    private JComboBox<String> cboTipoModificar;
    private JTextField txtNombreModificar;
    private JTextField txtSaldoModificar;
    private JButton btnModificar;
    private int idCuentaSeleccionadaModificar = 0;

    // ── Pestaña Eliminar ─────────────────────────────────────
    private JTable tablaEliminar;
    private DefaultTableModel modeloEliminar;
    private JButton btnEliminar;
    private int idCuentaSeleccionadaEliminar = 0;

    // ── Pestaña Consultar ────────────────────────────────────
    private JComboBox<String> cboTipoConsultar;
    private JTextField txtNombreConsultar;
    private JButton btnBuscarConsultar;
    private JTable tablaConsultar;
    private DefaultTableModel modeloConsultar;
    private JLabel lblMensajeConsulta;

    /**
     * Constructor de la ventana de cuentas.
     * Inicializa los componentes gráficos y los eventos.
     */
    public VentanaCuentas() {
        setTitle("💰 Gestión de Cuentas");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponentes();
        initEventos();
    }

    /**
     * Inicializa y organiza todos los componentes gráficos.
     */
    private void initComponentes() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabbedPane.addTab("➕ Agregar", crearPanelAgregar());
        tabbedPane.addTab("✏️ Modificar", crearPanelModificar());
        tabbedPane.addTab("🗑️ Eliminar", crearPanelEliminar());
        tabbedPane.addTab("🔍 Consultar", crearPanelConsultar());

        setContentPane(tabbedPane);
    }

    /**
     * Crea el panel de la pestaña Agregar con formulario de ingreso.
     *
     * @return JPanel con los componentes de agregar
     */
    private JPanel crearPanelAgregar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cboTipoAgregar = new JComboBox<>(Cuenta.TIPOS_CUENTA);
        txtNombreAgregar = new JTextField(20);
        txtSaldoAgregar = new JTextField(20);
        txtSaldoAgregar.setText("0.00");

        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(new JLabel("Tipo de Cuenta:"), gbc);
        gbc.gridx = 1;
        formulario.add(cboTipoAgregar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtNombreAgregar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formulario.add(new JLabel("Saldo Inicial:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtSaldoAgregar, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnGuardarAgregar = new JButton("💾 Guardar");
        btnGuardarAgregar.setBackground(new Color(46, 204, 113));
        btnGuardarAgregar.setForeground(Color.WHITE);
        btnGuardarAgregar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardarAgregar.setFocusPainted(false);

        btnLimpiarAgregar = new JButton("🧹 Limpiar");
        btnLimpiarAgregar.setBackground(new Color(149, 165, 166));
        btnLimpiarAgregar.setForeground(Color.WHITE);
        btnLimpiarAgregar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLimpiarAgregar.setFocusPainted(false);

        panelBotones.add(btnGuardarAgregar);
        panelBotones.add(btnLimpiarAgregar);

        panel.add(formulario, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Crea el panel de la pestaña Modificar con tabla y formulario.
     *
     * @return JPanel con los componentes de modificar
     */
    private JPanel crearPanelModificar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        modeloModificar = new DefaultTableModel(
                new String[] { "ID", "Tipo", "Nombre", "Saldo Inicial" }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tablaModificar = new JTable(modeloModificar);
        JScrollPane scrollModificar = new JScrollPane(tablaModificar);
        scrollModificar.setPreferredSize(new Dimension(800, 200));

        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cboTipoModificar = new JComboBox<>(Cuenta.TIPOS_CUENTA);
        txtNombreModificar = new JTextField(20);
        txtSaldoModificar = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(new JLabel("Tipo de Cuenta:"), gbc);
        gbc.gridx = 1;
        formulario.add(cboTipoModificar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtNombreModificar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formulario.add(new JLabel("Saldo Inicial:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtSaldoModificar, gbc);

        btnModificar = new JButton("✏️ Modificar");
        btnModificar.setBackground(new Color(52, 152, 219));
        btnModificar.setForeground(Color.WHITE);
        btnModificar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnModificar.setFocusPainted(false);

        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.add(formulario, BorderLayout.CENTER);
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(btnModificar);
        panelInferior.add(panelBoton, BorderLayout.SOUTH);

        panel.add(scrollModificar, BorderLayout.NORTH);
        panel.add(panelInferior, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Crea el panel de la pestaña Eliminar con tabla y botón.
     *
     * @return JPanel con los componentes de eliminar
     */
    private JPanel crearPanelEliminar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        modeloEliminar = new DefaultTableModel(
                new String[] { "ID", "Tipo", "Nombre", "Saldo Inicial" }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tablaEliminar = new JTable(modeloEliminar);
        JScrollPane scrollEliminar = new JScrollPane(tablaEliminar);

        btnEliminar = new JButton("🗑️ Eliminar");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEliminar.setFocusPainted(false);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(btnEliminar);

        panel.add(scrollEliminar, BorderLayout.CENTER);
        panel.add(panelBoton, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Crea el panel de la pestaña Consultar con filtros y tabla de resultados.
     *
     * @return JPanel con los componentes de consultar
     */
    private JPanel crearPanelConsultar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.add(new JLabel("Tipo:"));
        String[] tiposConTodos = new String[Cuenta.TIPOS_CUENTA.length + 1];
        tiposConTodos[0] = "-- Todos --";
        System.arraycopy(Cuenta.TIPOS_CUENTA, 0, tiposConTodos, 1,
                Cuenta.TIPOS_CUENTA.length);
        cboTipoConsultar = new JComboBox<>(tiposConTodos);
        panelFiltros.add(cboTipoConsultar);

        panelFiltros.add(new JLabel("Nombre:"));
        txtNombreConsultar = new JTextField(15);
        panelFiltros.add(txtNombreConsultar);

        btnBuscarConsultar = new JButton("🔍 Buscar");
        btnBuscarConsultar.setBackground(new Color(52, 152, 219));
        btnBuscarConsultar.setForeground(Color.WHITE);
        btnBuscarConsultar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBuscarConsultar.setFocusPainted(false);
        panelFiltros.add(btnBuscarConsultar);

        modeloConsultar = new DefaultTableModel(
                new String[] { "ID", "Tipo", "Nombre", "Saldo Inicial" }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tablaConsultar = new JTable(modeloConsultar);
        JScrollPane scrollConsultar = new JScrollPane(tablaConsultar);

        lblMensajeConsulta = new JLabel(" ", SwingConstants.CENTER);
        lblMensajeConsulta.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblMensajeConsulta.setForeground(new Color(231, 76, 60));

        panel.add(panelFiltros, BorderLayout.NORTH);
        panel.add(scrollConsultar, BorderLayout.CENTER);
        panel.add(lblMensajeConsulta, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Inicializa todos los eventos de la ventana.
     */
    private void initEventos() {
        // ── Pestaña Agregar ──────────────────────────────────
        btnGuardarAgregar.addActionListener(e -> {
            Cuenta cuenta = new Cuenta();
            cuenta.setIdUsuario(MenuPrincipal.ID_USUARIO_ACTIVO);
            cuenta.setTipoCuenta(
                    (String) cboTipoAgregar.getSelectedItem());
            cuenta.setNombre(txtNombreAgregar.getText());

            try {
                cuenta.setSaldoInicial(
                        Double.parseDouble(txtSaldoAgregar.getText().trim()));
            } catch (NumberFormatException ex) {
                MenuPrincipal.mostrarMensaje(
                        "El saldo inicial debe ser un número válido.");
                return;
            }

            if (!cuenta.verificarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Complete todos los campos obligatorios.");
                return;
            }

            CuentaMD cuentaMD = new CuentaMD();
            if (cuentaMD.existeNombre(cuenta)) {
                MenuPrincipal.mostrarMensaje(
                        "Ya existe una cuenta con ese nombre. "
                                + "Ingrese otro nombre.");
                return;
            }

            if (cuenta.grabarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Cuenta registrada exitosamente.");
                cuenta.limpiarDatos();
                limpiarFormularioAgregar();
            } else {
                MenuPrincipal.mostrarMensaje(
                        "Problemas con bases de datos. "
                                + "Contactar a soporte.");
            }
        });

        btnLimpiarAgregar.addActionListener(e -> {
            limpiarFormularioAgregar();
        });

        // ── Pestaña Modificar ────────────────────────────────
        tabbedPane.addChangeListener(e -> {
            int indice = tabbedPane.getSelectedIndex();
            if (indice == 1) {
                cargarTablaModificar();
            } else if (indice == 2) {
                cargarTablaEliminar();
            }
        });

        tablaModificar.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaModificar.getSelectedRow();
                if (fila >= 0) {
                    idCuentaSeleccionadaModificar = Integer.parseInt(
                            modeloModificar.getValueAt(fila, 0).toString());
                    String tipo = modeloModificar.getValueAt(
                            fila, 1).toString();
                    cboTipoModificar.setSelectedItem(tipo);
                    txtNombreModificar.setText(
                            modeloModificar.getValueAt(fila, 2).toString());
                    txtSaldoModificar.setText(
                            modeloModificar.getValueAt(fila, 3).toString());
                }
            }
        });

        btnModificar.addActionListener(e -> {
            if (idCuentaSeleccionadaModificar == 0) {
                MenuPrincipal.mostrarMensaje(
                        "Seleccione una cuenta de la tabla.");
                return;
            }

            Cuenta cuenta = new Cuenta();
            cuenta.setIdCuenta(idCuentaSeleccionadaModificar);
            cuenta.setIdUsuario(MenuPrincipal.ID_USUARIO_ACTIVO);
            cuenta.setTipoCuenta(
                    (String) cboTipoModificar.getSelectedItem());
            cuenta.setNombre(txtNombreModificar.getText());

            try {
                cuenta.setSaldoInicial(
                        Double.parseDouble(
                                txtSaldoModificar.getText().trim()));
            } catch (NumberFormatException ex) {
                MenuPrincipal.mostrarMensaje(
                        "El saldo inicial debe ser un número válido.");
                return;
            }

            if (!cuenta.verificarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Complete todos los campos obligatorios.");
                return;
            }

            CuentaMD cuentaMD = new CuentaMD();
            if (cuentaMD.existeNombreExcluyendo(cuenta)) {
                MenuPrincipal.mostrarMensaje(
                        "Ya existe otra cuenta con ese nombre. "
                                + "Ingrese un nombre diferente.");
                return;
            }

            if (cuenta.modificarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Cuenta modificada exitosamente.");
                cargarTablaModificar();
                limpiarFormularioModificar();
            } else {
                MenuPrincipal.mostrarMensaje(
                        "Problemas con bases de datos. "
                                + "Contactar a soporte.");
            }
        });

        // ── Pestaña Eliminar ─────────────────────────────────
        tablaEliminar.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaEliminar.getSelectedRow();
                if (fila >= 0) {
                    idCuentaSeleccionadaEliminar = Integer.parseInt(
                            modeloEliminar.getValueAt(fila, 0).toString());
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            if (idCuentaSeleccionadaEliminar == 0) {
                MenuPrincipal.mostrarMensaje(
                        "Seleccione una cuenta de la tabla.");
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar esta cuenta?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            Cuenta cuenta = new Cuenta();
            cuenta.setIdCuenta(idCuentaSeleccionadaEliminar);

            CuentaMD cuentaMD = new CuentaMD();
            if (cuentaMD.verificarMovimientos(cuenta)) {
                MenuPrincipal.mostrarMensaje(
                        "No se puede eliminar la cuenta porque "
                                + "tiene movimientos asociados.");
                return;
            }

            if (cuenta.borrarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Cuenta eliminada exitosamente.");
                cargarTablaEliminar();
                idCuentaSeleccionadaEliminar = 0;
            } else {
                MenuPrincipal.mostrarMensaje(
                        "Problemas con bases de datos. "
                                + "Contactar a soporte.");
            }
        });

        // ── Pestaña Consultar ────────────────────────────────
        btnBuscarConsultar.addActionListener(e -> {
            buscarCuentas();
        });
    }

    /**
     * Carga la tabla de la pestaña Modificar con las cuentas del usuario.
     */
    private void cargarTablaModificar() {
        modeloModificar.setRowCount(0);
        CuentaMD cuentaMD = new CuentaMD();
        ResultSet rs = cuentaMD.consultar(MenuPrincipal.ID_USUARIO_ACTIVO);
        try {
            if (rs != null) {
                while (rs.next()) {
                    modeloModificar.addRow(new Object[] {
                            rs.getInt("id_cuenta"),
                            rs.getString("tipo_cuenta"),
                            rs.getString("nombre"),
                            rs.getDouble("saldo_inicial")
                    });
                }
            }
        } catch (SQLException ex) {
            MenuPrincipal.mostrarMensaje(
                    "Problemas con bases de datos. Contactar a soporte.");
        }
    }

    /**
     * Carga la tabla de la pestaña Eliminar con las cuentas del usuario.
     */
    private void cargarTablaEliminar() {
        modeloEliminar.setRowCount(0);
        CuentaMD cuentaMD = new CuentaMD();
        ResultSet rs = cuentaMD.consultar(MenuPrincipal.ID_USUARIO_ACTIVO);
        try {
            if (rs != null) {
                while (rs.next()) {
                    modeloEliminar.addRow(new Object[] {
                            rs.getInt("id_cuenta"),
                            rs.getString("tipo_cuenta"),
                            rs.getString("nombre"),
                            rs.getDouble("saldo_inicial")
                    });
                }
            }
        } catch (SQLException ex) {
            MenuPrincipal.mostrarMensaje(
                    "Problemas con bases de datos. Contactar a soporte.");
        }
    }

    /**
     * Busca cuentas según los filtros de la pestaña Consultar.
     * Filtra siempre por el usuario autenticado.
     */
    private void buscarCuentas() {
        modeloConsultar.setRowCount(0);
        lblMensajeConsulta.setText(" ");

        Cuenta filtro = new Cuenta();
        filtro.setIdUsuario(MenuPrincipal.ID_USUARIO_ACTIVO);

        String tipoSeleccionado = (String) cboTipoConsultar.getSelectedItem();
        if (tipoSeleccionado != null
                && !tipoSeleccionado.equals("-- Todos --")) {
            filtro.setTipoCuenta(tipoSeleccionado);
        }

        String nombreFiltro = txtNombreConsultar.getText().trim();
        if (!nombreFiltro.isEmpty()) {
            filtro.setNombre(nombreFiltro);
        }

        CuentaMD cuentaMD = new CuentaMD();
        ResultSet rs = cuentaMD.consultar(filtro);

        try {
            if (rs != null) {
                boolean hayResultados = false;
                while (rs.next()) {
                    hayResultados = true;
                    modeloConsultar.addRow(new Object[] {
                            rs.getInt("id_cuenta"),
                            rs.getString("tipo_cuenta"),
                            rs.getString("nombre"),
                            rs.getDouble("saldo_inicial")
                    });
                }
                if (!hayResultados) {
                    if (nombreFiltro.isEmpty()
                            && (tipoSeleccionado == null
                                    || tipoSeleccionado.equals("-- Todos --"))) {
                        lblMensajeConsulta.setText(
                                "No hay cuentas registradas.");
                    } else {
                        lblMensajeConsulta.setText(
                                "No se encontraron resultados "
                                        + "para su búsqueda.");
                    }
                }
            }
        } catch (SQLException ex) {
            MenuPrincipal.mostrarMensaje(
                    "Problemas con bases de datos. Contactar a soporte.");
        }
    }

    /**
     * Limpia los campos del formulario de agregar.
     */
    private void limpiarFormularioAgregar() {
        cboTipoAgregar.setSelectedIndex(0);
        txtNombreAgregar.setText("");
        txtSaldoAgregar.setText("0.00");
    }

    /**
     * Limpia los campos del formulario de modificar.
     */
    private void limpiarFormularioModificar() {
        cboTipoModificar.setSelectedIndex(0);
        txtNombreModificar.setText("");
        txtSaldoModificar.setText("");
        idCuentaSeleccionadaModificar = 0;
        tablaModificar.clearSelection();
    }
}
