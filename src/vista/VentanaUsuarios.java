package vista;

import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


/**
 * Ventana de gestión de usuarios del sistema.
 * Capa: Vista (GUI).
 * Proporciona las pestañas CRUD (Agregar, Modificar, Eliminar, Consultar)
 * para la entidad Usuario.
 *
 * @author Samantha Nuñez
 */
public class VentanaUsuarios extends JFrame {

    private JPanel panelPrincipal;
    private CardLayout cardLayout;

    // ── Pestaña Agregar ──────────────────────────────────────
    private JTextField txtNombreAgregar;
    private JTextField txtEmailAgregar;
    private JTextField txtTelefonoAgregar;
    private JTextField txtPasswordAgregar;
    private JButton btnGuardarAgregar;

    // ── Pestaña Modificar ────────────────────────────────────
    private JTable tablaModificar;
    private DefaultTableModel modeloModificar;
    private JTextField txtNombreModificar;
    private JTextField txtEmailModificar;
    private JTextField txtTelefonoModificar;
    private JTextField txtPasswordModificar;
    private JButton btnAbrirFormularioModificar;
    private JButton btnGuardarModificacion;
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
        JPanel panelOpciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnNuevo = new JButton("Nuevo Usuario");
        JButton btnModificarOpcion = new JButton("Modificar Usuario");
        JButton btnEliminarOpcion = new JButton("Eliminar Usuario");
        JButton btnConsultar = new JButton("Consultar Usuarios");

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

        txtNombreAgregar = new JTextField(20);
        txtNombreAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    evt.consume();
                }
                if (txtNombreAgregar.getText().length() >= 100) {
                    evt.consume();
                }
            }
        });
        txtEmailAgregar = new JTextField(20);
        txtEmailAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (txtEmailAgregar.getText().length() >= 100) {
                    evt.consume();
                }
                if (c == '@' && txtEmailAgregar.getText().contains("@")) {
                    evt.consume();
                }
            }
        });
        txtTelefonoAgregar = new JTextField(20);
        txtTelefonoAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c)) {
                    evt.consume();
                }
                if (txtTelefonoAgregar.getText().length() >= 10) {
                    evt.consume();
                }
            }
        });
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
                new String[] { "ID", "Nombre", "Email", "Teléfono" }, 0) {
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

        txtNombreModificar = new JTextField(20);
        txtNombreModificar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    evt.consume();
                }
                if (txtNombreModificar.getText().length() >= 100) {
                    evt.consume();
                }
            }
        });
        txtEmailModificar = new JTextField(20);
        txtEmailModificar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (txtEmailModificar.getText().length() >= 100) {
                    evt.consume();
                }
                if (c == '@' && txtEmailModificar.getText().contains("@")) {
                    evt.consume();
                }
            }
        });
        txtTelefonoModificar = new JTextField(20);
        txtTelefonoModificar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c)) {
                    evt.consume();
                }
                if (txtTelefonoModificar.getText().length() >= 10) {
                    evt.consume();
                }
            }
        });
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
        panelFiltros.add(new JLabel("Nombre:"));
        txtNombreConsultar = new JTextField(20);
        panelFiltros.add(txtNombreConsultar);

        btnBuscarConsultar = new JButton("🔍 Buscar");
        btnBuscarConsultar.setBackground(new Color(52, 152, 219));
        btnBuscarConsultar.setForeground(Color.WHITE);
        btnBuscarConsultar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
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
            usuario.setNombre(txtNombreAgregar.getText().trim());
            usuario.setEmail(txtEmailAgregar.getText().trim().toLowerCase());
            usuario.setTelefono(txtTelefonoAgregar.getText());
            usuario.setPassword(txtPasswordAgregar.getText());

            if (!usuario.verificarDP()) {
                this.mostrarMensaje(
                        "Complete todos los campos obligatorios "
                                + "(nombre, email, teléfono).");
                return;
            }

            if (!usuario.verificarFormatoEmail()) {
                this.mostrarMensaje(
                        "El email ingresado no tiene un formato válido.");
                return;
            }

            if (usuario.existeEmail()) {
                this.mostrarMensaje(
                        "Ya existe un usuario con ese nombre. "
                                + "Ingrese otro nombre.");
                return;
            }

            if (usuario.grabarDP()) {
                this.mostrarMensaje(
                        "Usuario registrado exitosamente.");
                usuario.limpiarDatos();
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
                this.mostrarMensaje("Seleccione un usuario de la tabla.");
                return;
            }
            idUsuarioSeleccionadoModificar = Integer.parseInt(
                    modeloModificar.getValueAt(fila, 0).toString());
            txtNombreModificar.setText(
                    modeloModificar.getValueAt(fila, 1).toString());
            txtEmailModificar.setText(
                    modeloModificar.getValueAt(fila, 2).toString());
            txtTelefonoModificar.setText(
                    modeloModificar.getValueAt(fila, 3).toString());
            txtPasswordModificar.setText("");
            
            cardLayout.show(panelPrincipal, "FormularioModificar");
        });

        btnGuardarModificacion.addActionListener(e -> {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(idUsuarioSeleccionadoModificar);
            usuario.setNombre(txtNombreModificar.getText().trim());
            usuario.setEmail(txtEmailModificar.getText().trim().toLowerCase());
            usuario.setTelefono(txtTelefonoModificar.getText());
            usuario.setPassword(txtPasswordModificar.getText());

            if (!usuario.verificarDP()) {
                this.mostrarMensaje(
                        "Complete todos los campos obligatorios.");
                return;
            }

            if (!usuario.verificarFormatoEmail()) {
                this.mostrarMensaje(
                        "El email ingresado no tiene un formato válido.");
                return;
            }

            if (usuario.existeEmailExcluyendo()) {
                this.mostrarMensaje(
                        "El email ingresado ya pertenece a un usuario registrado. "
                                + "Ingrese un nombre diferente.");
                return;
            }

            if (usuario.modificarDP()) {
                this.mostrarMensaje(
                        "Usuario modificado exitosamente.");
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
                    idUsuarioSeleccionadoEliminar = Integer.parseInt(
                            modeloEliminar.getValueAt(fila, 0).toString());
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            if (idUsuarioSeleccionadoEliminar == 0) {
                this.mostrarMensaje(
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

            if (usuario.verificarMovimientos()) {
                this.mostrarMensaje(
                        "No se puede eliminar el usuario porque "
                                + "tiene registros asociados.");
                return;
            }

            if (usuario.borrarDP()) {
                this.mostrarMensaje(
                        "Usuario eliminado exitosamente.");
                cargarTablaEliminar();
                idUsuarioSeleccionadoEliminar = 0;
            } else {
                this.mostrarMensaje(
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
        List<Usuario> lista = usuario.buscarUsuarios();
        if (lista != null) {
            for (Usuario u : lista) {
                modeloModificar.addRow(new Object[] {
                        u.getIdUsuario(),
                        u.getNombre(),
                        u.getEmail(),
                        u.getTelefono()
                });
            }
        }
    }

    /**
     * Carga la tabla de la pestaña Eliminar con los usuarios activos.
     */
    private void cargarTablaEliminar() {
        modeloEliminar.setRowCount(0);
        Usuario usuario = new Usuario();
        List<Usuario> lista = usuario.buscarUsuarios();
        if (lista != null) {
            for (Usuario u : lista) {
                modeloEliminar.addRow(new Object[] {
                        u.getIdUsuario(),
                        u.getNombre(),
                        u.getEmail(),
                        u.getTelefono()
                });
            }
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
        List<Usuario> rs;
        String nombreFiltro = txtNombreConsultar.getText().trim();
        if (nombreFiltro.isEmpty()) {
            rs = usuario.buscarUsuarios();
        } else {
            Usuario filtro = new Usuario();
            filtro.setNombre(nombreFiltro);
            rs = usuario.buscarUsuarios(filtro);
        }

        if (rs != null) {
            boolean hayResultados = false;
            for (Usuario u : rs) {
                hayResultados = true;
                modeloConsultar.addRow(new Object[] {
                        u.getIdUsuario(),
                        u.getNombre(),
                        u.getEmail(),
                        u.getTelefono()
                });
            }
            if (!hayResultados) {
                if (nombreFiltro.isEmpty()) {
                    lblMensajeConsulta.setText("No hay usuarios registrados.");
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

    public void mostrarListado() {
        // Método para cumplir con el diagrama de clases
    }

    public void mostrarMensaje(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }
}
