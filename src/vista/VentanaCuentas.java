package vista;

import modelo.Cuenta;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


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

    private JPanel panelPrincipal;
    private CardLayout cardLayout;

    // ── Pestaña Agregar ──────────────────────────────────────
    private JComboBox<String> cboTipoAgregar;
    private JTextField txtNombreAgregar;
    private JButton btnGuardarAgregar;

    // ── Pestaña Modificar ────────────────────────────────────
    private JTable tablaModificar;
    private DefaultTableModel modeloModificar;
    private JComboBox<String> cboTipoModificar;
    private JTextField txtNombreModificar;
    private JButton btnAbrirFormularioModificar;
    private JButton btnGuardarModificacion;
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
        JPanel panelOpciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnNuevo = new JButton("Nueva Cuenta");
        JButton btnModificarOpcion = new JButton("Modificar Cuenta");
        JButton btnEliminarOpcion = new JButton("Eliminar Cuenta");
        JButton btnConsultar = new JButton("Consultar Cuentas");

        panelOpciones.add(btnNuevo);
        panelOpciones.add(btnModificarOpcion);
        panelOpciones.add(btnEliminarOpcion);
        panelOpciones.add(btnConsultar);

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        JPanel panelVacio = new JPanel();
        panelPrincipal.add(panelVacio, "Vacio");
        panelPrincipal.add(crearPanelAgregar(), "Agregar");
        panelPrincipal.add(crearPanelModificar(), "Modificar");
        panelPrincipal.add(crearPanelFormularioModificar(), "FormularioModificar");
        panelPrincipal.add(crearPanelEliminar(), "Eliminar");
        panelPrincipal.add(crearPanelConsultar(), "Consultar");

        JPanel panelFondo = new JPanel(new BorderLayout());
        panelFondo.add(panelOpciones, BorderLayout.NORTH);
        panelFondo.add(panelPrincipal, BorderLayout.CENTER);
        setContentPane(panelFondo);

        // Eventos de los botones
        btnNuevo.addActionListener(e -> cardLayout.show(panelPrincipal, "Agregar"));
        btnModificarOpcion.addActionListener(e -> {
            cargarTablaModificar();
            cardLayout.show(panelPrincipal, "Modificar");
        });
        btnEliminarOpcion.addActionListener(e -> {
            cargarTablaEliminar();
            cardLayout.show(panelPrincipal, "Eliminar");
        });
        btnConsultar.addActionListener(e -> cardLayout.show(panelPrincipal, "Consultar"));
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
        txtNombreAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    evt.consume();
                }
            }
        });        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(new JLabel("Tipo de Cuenta:"), gbc);
        gbc.gridx = 1;
        formulario.add(cboTipoAgregar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtNombreAgregar, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnGuardarAgregar = new JButton("💾 Guardar");
        btnGuardarAgregar.setBackground(new Color(46, 204, 113));
        btnGuardarAgregar.setForeground(Color.WHITE);
        btnGuardarAgregar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        btnGuardarAgregar.setFocusPainted(false);

        panelBotones.add(btnGuardarAgregar);

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
                new String[] { "ID", "Tipo", "Nombre" }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tablaModificar = new JTable(modeloModificar);
        JScrollPane scrollModificar = new JScrollPane(tablaModificar);

        btnAbrirFormularioModificar = new JButton("✏️ Modificar");
        btnAbrirFormularioModificar.setBackground(new Color(52, 152, 219));
        btnAbrirFormularioModificar.setForeground(Color.WHITE);
        btnAbrirFormularioModificar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        btnAbrirFormularioModificar.setFocusPainted(false);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(btnAbrirFormularioModificar);

        panel.add(scrollModificar, BorderLayout.CENTER);
        panel.add(panelBoton, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Crea el panel del formulario para modificar.
     *
     * @return JPanel con el formulario
     */
    private JPanel crearPanelFormularioModificar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cboTipoModificar = new JComboBox<>(Cuenta.TIPOS_CUENTA);
        txtNombreModificar = new JTextField(20);
        txtNombreModificar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    evt.consume();
                }
            }
        });

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

        btnGuardarModificacion = new JButton("💾 Guardar");
        btnGuardarModificacion.setBackground(new Color(46, 204, 113));
        btnGuardarModificacion.setForeground(Color.WHITE);
        btnGuardarModificacion.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        btnGuardarModificacion.setFocusPainted(false);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(btnGuardarModificacion);

        panel.add(formulario, BorderLayout.CENTER);
        panel.add(panelBoton, BorderLayout.SOUTH);
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
                new String[] { "ID", "Tipo", "Nombre" }, 0) {
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
        btnEliminar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
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
        btnBuscarConsultar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        btnBuscarConsultar.setFocusPainted(false);
        panelFiltros.add(btnBuscarConsultar);

        modeloConsultar = new DefaultTableModel(
                new String[] { "ID", "Tipo", "Nombre" }, 0) {
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
            cuenta.setNombre(txtNombreAgregar.getText().trim().toLowerCase());

            if (!cuenta.verificarDP()) {
                this.mostrarMensaje(
                        "Complete todos los campos obligatorios.");
                return;
            }

            if (cuenta.existeNombre()) {
                this.mostrarMensaje(
                        "Ya existe una cuenta con ese nombre. "
                                + "Ingrese otro nombre.");
                return;
            }

            if (cuenta.grabarDP()) {
                this.mostrarMensaje(
                        "Cuenta registrada exitosamente.");
                cuenta.limpiarDatos();
                limpiarFormularioAgregar();
            } else {
                this.mostrarMensaje(
                        "Problemas con bases de datos. "
                                + "Contactar a soporte.");
            }
        });

        btnAbrirFormularioModificar.addActionListener(e -> {
            int fila = tablaModificar.getSelectedRow();
            if (fila < 0) {
                this.mostrarMensaje("Seleccione una cuenta de la tabla.");
                return;
            }
            idCuentaSeleccionadaModificar = Integer.parseInt(
                    modeloModificar.getValueAt(fila, 0).toString());
            cboTipoModificar.setSelectedItem(
                    modeloModificar.getValueAt(fila, 1).toString());
            txtNombreModificar.setText(
                    modeloModificar.getValueAt(fila, 2).toString());
            
            cardLayout.show(panelPrincipal, "FormularioModificar");
        });

        btnGuardarModificacion.addActionListener(e -> {
            Cuenta cuenta = new Cuenta();
            cuenta.setIdCuenta(idCuentaSeleccionadaModificar);
            cuenta.setIdUsuario(MenuPrincipal.ID_USUARIO_ACTIVO);
            cuenta.setTipoCuenta(cboTipoModificar.getSelectedItem().toString());
            cuenta.setNombre(txtNombreModificar.getText());

            if (!cuenta.verificarDP()) {
                this.mostrarMensaje(
                        "Complete todos los campos obligatorios.");
                return;
            }

            if (cuenta.existeNombreExcluyendo()) {
                this.mostrarMensaje(
                        "Ya existe otra cuenta con ese nombre para su usuario. "
                                + "Ingrese un nombre diferente.");
                return;
            }

            if (cuenta.modificarDP()) {
                this.mostrarMensaje(
                        "Cuenta modificada exitosamente.");
                cargarTablaModificar();
                limpiarFormularioModificar();
                cardLayout.show(panelPrincipal, "Modificar");
            } else {
                this.mostrarMensaje(
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
                this.mostrarMensaje(
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

            if (cuenta.verificarMovimientos()) {
                this.mostrarMensaje(
                        "No se puede eliminar la cuenta porque "
                                + "tiene movimientos asociados.");
                return;
            }

            if (cuenta.borrarDP()) {
                this.mostrarMensaje(
                        "Cuenta eliminada exitosamente.");
                cargarTablaEliminar();
                idCuentaSeleccionadaEliminar = 0;
            } else {
                this.mostrarMensaje(
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
        Cuenta cuenta = new Cuenta();
        cuenta.setIdUsuario(MenuPrincipal.ID_USUARIO_ACTIVO);
        List<Cuenta> lista = cuenta.buscarCuentas();
        if (lista != null) {
            for (Cuenta c : lista) {
                modeloModificar.addRow(new Object[] {
                        c.getIdCuenta(),
                        c.getTipoCuenta(),
                        c.getNombre()
                });
            }
        }
    }

    /**
     * Carga la tabla de la pestaña Eliminar con las cuentas del usuario.
     */
    private void cargarTablaEliminar() {
        modeloEliminar.setRowCount(0);
        Cuenta cuenta = new Cuenta();
        cuenta.setIdUsuario(MenuPrincipal.ID_USUARIO_ACTIVO);
        List<Cuenta> lista = cuenta.buscarCuentas();
        if (lista != null) {
            for (Cuenta c : lista) {
                modeloEliminar.addRow(new Object[] {
                        c.getIdCuenta(),
                        c.getTipoCuenta(),
                        c.getNombre()
                });
            }
        }
    }

    /**
     * Busca cuentas según los filtros de la pestaña Consultar.
     * Filtra siempre por el usuario autenticado.
     */
    private void buscarCuentas() {
        modeloConsultar.setRowCount(0);
        lblMensajeConsulta.setText(" ");
        Cuenta cuenta = new Cuenta();
        List<Cuenta> rs;

        String nombreFiltro = txtNombreConsultar.getText().trim();
        if (nombreFiltro.isEmpty()) {
            cuenta.setIdUsuario(MenuPrincipal.ID_USUARIO_ACTIVO);
            rs = cuenta.buscarCuentas();
        } else {
            Cuenta filtro = new Cuenta();
            filtro.setIdUsuario(MenuPrincipal.ID_USUARIO_ACTIVO);
            filtro.setNombre(nombreFiltro);
            rs = cuenta.buscarCuentas(filtro);
        }

        if (rs != null) {
            boolean hayResultados = false;
            for (Cuenta c : rs) {
                hayResultados = true;
                modeloConsultar.addRow(new Object[] {
                        c.getIdCuenta(),
                        c.getTipoCuenta(),
                        c.getNombre()
                });
            }
            if (!hayResultados) {
                if (nombreFiltro.isEmpty()) {
                    lblMensajeConsulta.setText("No hay cuentas registradas.");
                } else {
                    lblMensajeConsulta.setText("No se encontraron resultados para su búsqueda.");
                }
            }
        }
    }

    /**
     * Limpia los campos del formulario de agregar.
     */
    private void limpiarFormularioAgregar() {
        cboTipoAgregar.setSelectedIndex(0);
        txtNombreAgregar.setText("");
    }

    /**
     * Limpia los campos del formulario de modificar.
     */
    private void limpiarFormularioModificar() {
        cboTipoModificar.setSelectedIndex(0);
        txtNombreModificar.setText("");
        idCuentaSeleccionadaModificar = 0;
        tablaModificar.clearSelection();
    }

    public void mostrarListado() {
        // Método para cumplir con el diagrama de clases
    }

    public void mostrarMensaje(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }
}
