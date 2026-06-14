package vista;

import datos.UsuarioMD;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Ventana de gestión de usuarios del sistema.
 * Capa: Vista (GUI).
 * Proporciona las pestañas CRUD (Agregar, Modificar, Eliminar, Consultar)
 * para la entidad Usuario.
 *
 * @author Samantha Nuñez
 */
public class VentanaUsuarios extends JFrame {

    private JTabbedPane tabbedPane;

    // ── Pestaña Agregar ──────────────────────────────────────
    private JTextField txtNombreAgregar;
    private JTextField txtEmailAgregar;
    private JTextField txtTelefonoAgregar;
    private JTextField txtPasswordAgregar;
    private JButton btnGuardarAgregar;
    private JButton btnLimpiarAgregar;

    // ── Pestaña Modificar ────────────────────────────────────
    private JTable tablaModificar;
    private DefaultTableModel modeloModificar;
    private JTextField txtNombreModificar;
    private JTextField txtEmailModificar;
    private JTextField txtTelefonoModificar;
    private JTextField txtPasswordModificar;
    private JButton btnModificar;
    private int idUsuarioSeleccionadoModificar = 0;

    // ── Pestaña Eliminar ─────────────────────────────────────
    private JTable tablaEliminar;
    private DefaultTableModel modeloEliminar;
    private JButton btnEliminar;
    private int idUsuarioSeleccionadoEliminar = 0;

    // ── Pestaña Consultar ────────────────────────────────────
    private JTextField txtNombreConsultar;
    private JButton btnBuscarConsultar;
    private JTable tablaConsultar;
    private DefaultTableModel modeloConsultar;
    private JLabel lblMensajeConsulta;

    /**
     * Constructor de la ventana de usuarios.
     * Inicializa los componentes gráficos y los eventos.
     */
    public VentanaUsuarios() {
        setTitle("👤 Gestión de Usuarios");
        setSize(800, 600);
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

        txtNombreAgregar = new JTextField(20);
        txtEmailAgregar = new JTextField(20);
        txtTelefonoAgregar = new JTextField(20);
        txtPasswordAgregar = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtNombreAgregar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtEmailAgregar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formulario.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtTelefonoAgregar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formulario.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtPasswordAgregar, gbc);

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
                new String[] { "ID", "Nombre", "Email", "Teléfono" }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tablaModificar = new JTable(modeloModificar);
        JScrollPane scrollModificar = new JScrollPane(tablaModificar);
        scrollModificar.setPreferredSize(new Dimension(750, 200));

        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombreModificar = new JTextField(20);
        txtEmailModificar = new JTextField(20);
        txtTelefonoModificar = new JTextField(20);
        txtPasswordModificar = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtNombreModificar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtEmailModificar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formulario.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtTelefonoModificar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formulario.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtPasswordModificar, gbc);

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
     * Crea el panel de la pestaña Eliminar con tabla y botón de eliminación.
     *
     * @return JPanel con los componentes de eliminar
     */
    private JPanel crearPanelEliminar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        modeloEliminar = new DefaultTableModel(
                new String[] { "ID", "Nombre", "Email", "Teléfono" }, 0) {
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
        panelFiltros.add(new JLabel("Nombre:"));
        txtNombreConsultar = new JTextField(20);
        panelFiltros.add(txtNombreConsultar);

        btnBuscarConsultar = new JButton("🔍 Buscar");
        btnBuscarConsultar.setBackground(new Color(52, 152, 219));
        btnBuscarConsultar.setForeground(Color.WHITE);
        btnBuscarConsultar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBuscarConsultar.setFocusPainted(false);
        panelFiltros.add(btnBuscarConsultar);

        modeloConsultar = new DefaultTableModel(
                new String[] { "ID", "Nombre", "Email", "Teléfono" }, 0) {
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
            Usuario usuario = new Usuario();
            usuario.setNombre(txtNombreAgregar.getText());
            usuario.setEmail(txtEmailAgregar.getText());
            usuario.setTelefono(txtTelefonoAgregar.getText());
            usuario.setPassword(txtPasswordAgregar.getText());

            if (!usuario.verificarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Complete todos los campos obligatorios "
                                + "(nombre, email, teléfono).");
                return;
            }

            UsuarioMD usuarioMD = new UsuarioMD();
            if (usuarioMD.existeNombre(usuario)) {
                MenuPrincipal.mostrarMensaje(
                        "Ya existe un usuario con ese nombre. "
                                + "Ingrese otro nombre.");
                return;
            }

            if (usuario.grabarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Usuario registrado exitosamente.");
                usuario.limpiarDatos();
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
                    idUsuarioSeleccionadoModificar = Integer.parseInt(
                            modeloModificar.getValueAt(fila, 0).toString());
                    txtNombreModificar.setText(
                            modeloModificar.getValueAt(fila, 1).toString());
                    txtEmailModificar.setText(
                            modeloModificar.getValueAt(fila, 2).toString());
                    txtTelefonoModificar.setText(
                            modeloModificar.getValueAt(fila, 3).toString());
                    txtPasswordModificar.setText("");
                }
            }
        });

        btnModificar.addActionListener(e -> {
            if (idUsuarioSeleccionadoModificar == 0) {
                MenuPrincipal.mostrarMensaje(
                        "Seleccione un usuario de la tabla.");
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setIdUsuario(idUsuarioSeleccionadoModificar);
            usuario.setNombre(txtNombreModificar.getText());
            usuario.setEmail(txtEmailModificar.getText());
            usuario.setTelefono(txtTelefonoModificar.getText());
            usuario.setPassword(txtPasswordModificar.getText());

            if (!usuario.verificarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Complete todos los campos obligatorios.");
                return;
            }

            UsuarioMD usuarioMD = new UsuarioMD();
            if (usuarioMD.existeNombreExcluyendo(usuario)) {
                MenuPrincipal.mostrarMensaje(
                        "Ya existe otro usuario con ese nombre. "
                                + "Ingrese un nombre diferente.");
                return;
            }

            if (usuario.modificarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Usuario modificado exitosamente.");
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
                    idUsuarioSeleccionadoEliminar = Integer.parseInt(
                            modeloEliminar.getValueAt(fila, 0).toString());
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            if (idUsuarioSeleccionadoEliminar == 0) {
                MenuPrincipal.mostrarMensaje(
                        "Seleccione un usuario de la tabla.");
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar este usuario?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setIdUsuario(idUsuarioSeleccionadoEliminar);

            UsuarioMD usuarioMD = new UsuarioMD();
            if (usuarioMD.verificarMovimientos(usuario)) {
                MenuPrincipal.mostrarMensaje(
                        "No se puede eliminar el usuario porque "
                                + "tiene registros asociados.");
                return;
            }

            if (usuario.borrarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Usuario eliminado exitosamente.");
                cargarTablaEliminar();
                idUsuarioSeleccionadoEliminar = 0;
            } else {
                MenuPrincipal.mostrarMensaje(
                        "Problemas con bases de datos. "
                                + "Contactar a soporte.");
            }
        });

        // ── Pestaña Consultar ────────────────────────────────
        btnBuscarConsultar.addActionListener(e -> {
            buscarUsuarios();
        });
    }

    /**
     * Carga la tabla de la pestaña Modificar con los usuarios activos.
     */
    private void cargarTablaModificar() {
        modeloModificar.setRowCount(0);
        Usuario usuario = new Usuario();
        ResultSet rs = usuario.buscarUsuarios();
        try {
            if (rs != null) {
                while (rs.next()) {
                    modeloModificar.addRow(new Object[] {
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("telefono")
                    });
                }
            }
        } catch (SQLException ex) {
            MenuPrincipal.mostrarMensaje(
                    "Problemas con bases de datos. Contactar a soporte.");
        }
    }

    /**
     * Carga la tabla de la pestaña Eliminar con los usuarios activos.
     */
    private void cargarTablaEliminar() {
        modeloEliminar.setRowCount(0);
        Usuario usuario = new Usuario();
        ResultSet rs = usuario.buscarUsuarios();
        try {
            if (rs != null) {
                while (rs.next()) {
                    modeloEliminar.addRow(new Object[] {
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("telefono")
                    });
                }
            }
        } catch (SQLException ex) {
            MenuPrincipal.mostrarMensaje(
                    "Problemas con bases de datos. Contactar a soporte.");
        }
    }

    /**
     * Busca usuarios según los filtros de la pestaña Consultar.
     * Si el campo de nombre está vacío, muestra todos los usuarios.
     */
    private void buscarUsuarios() {
        modeloConsultar.setRowCount(0);
        lblMensajeConsulta.setText(" ");
        Usuario usuario = new Usuario();
        ResultSet rs;

        String nombreFiltro = txtNombreConsultar.getText().trim();
        if (nombreFiltro.isEmpty()) {
            rs = usuario.buscarUsuarios();
        } else {
            Usuario filtro = new Usuario();
            filtro.setNombre(nombreFiltro);
            rs = usuario.buscarUsuarios(filtro);
        }

        try {
            if (rs != null) {
                boolean hayResultados = false;
                while (rs.next()) {
                    hayResultados = true;
                    modeloConsultar.addRow(new Object[] {
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("telefono")
                    });
                }
                if (!hayResultados) {
                    if (nombreFiltro.isEmpty()) {
                        lblMensajeConsulta.setText(
                                "No hay usuarios registrados.");
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
        txtNombreAgregar.setText("");
        txtEmailAgregar.setText("");
        txtTelefonoAgregar.setText("");
        txtPasswordAgregar.setText("");
    }

    /**
     * Limpia los campos del formulario de modificar.
     */
    private void limpiarFormularioModificar() {
        txtNombreModificar.setText("");
        txtEmailModificar.setText("");
        txtTelefonoModificar.setText("");
        txtPasswordModificar.setText("");
        idUsuarioSeleccionadoModificar = 0;
        tablaModificar.clearSelection();
    }
}
