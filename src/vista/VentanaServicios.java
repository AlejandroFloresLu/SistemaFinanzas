package vista;

import datos.CategoriaMD;
import datos.ServiciosMD;
import modelo.Categoria;
import modelo.Servicio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Ventana de gestión de servicios del sistema.
 * Capa: Vista (GUI).
 * Proporciona las pestañas CRUD (Agregar, Modificar, Eliminar, Consultar)
 * para la entidad Servicio. Requiere categorías existentes para operar.
 *
 * @author Alejandro Flores
 */
public class VentanaServicios extends JFrame {

    private JTabbedPane tabbedPane;

    // ── Pestaña Agregar ──────────────────────────────────────
    private JComboBox<String> cboCategoriaAgregar;
    private JTextField txtNombreAgregar;
    private JTextField txtMontoAgregar;
    private JButton btnGuardarAgregar;
    private JButton btnLimpiarAgregar;
    private List<Categoria> listaCategorias;

    // ── Pestaña Modificar ────────────────────────────────────
    private JTable tablaModificar;
    private DefaultTableModel modeloModificar;
    private JComboBox<String> cboCategoriaModificar;
    private JTextField txtNombreModificar;
    private JTextField txtMontoModificar;
    private JButton btnModificar;
    private int idServicioSeleccionadoModificar = 0;
    private List<Categoria> listaCategoriasModificar;

    // ── Pestaña Eliminar ─────────────────────────────────────
    private JTable tablaEliminar;
    private DefaultTableModel modeloEliminar;
    private JButton btnEliminar;
    private int idServicioSeleccionadoEliminar = 0;

    // ── Pestaña Consultar ────────────────────────────────────
    private JComboBox<String> cboCategoriaConsultar;
    private JTextField txtNombreConsultar;
    private JButton btnBuscarConsultar;
    private JTable tablaConsultar;
    private DefaultTableModel modeloConsultar;
    private JLabel lblMensajeConsulta;
    private List<Categoria> listaCategoriasConsultar;

    /**
     * Constructor de la ventana de servicios.
     * Inicializa los componentes gráficos y los eventos.
     */
    public VentanaServicios() {
        setTitle("🔧 Gestión de Servicios");
        setSize(900, 650);
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
     * Carga las categorías disponibles en un combo box.
     *
     * @param combo JComboBox a llenar con las categorías
     * @return lista de categorías cargadas
     */
    private List<Categoria> cargarCategorias(JComboBox<String> combo) {
        combo.removeAllItems();
        CategoriaMD categoriaMD = new CategoriaMD();
        List<Categoria> categorias = categoriaMD.cargarCategoriaList();
        for (Categoria cat : categorias) {
            combo.addItem(cat.getNombre());
        }
        return categorias;
    }

    /**
     * Obtiene el idCategoria de la categoría seleccionada en el combo.
     *
     * @param combo      JComboBox con las categorías
     * @param categorias lista de categorías asociada al combo
     * @return idCategoria de la categoría seleccionada, 0 si no hay selección
     */
    private int obtenerIdCategoriaSeleccionada(JComboBox<String> combo,
            List<Categoria> categorias) {
        int indice = combo.getSelectedIndex();
        if (indice >= 0 && indice < categorias.size()) {
            return categorias.get(indice).getIdCategoria();
        }
        return 0;
    }

    /**
     * Crea el panel de la pestaña Agregar.
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

        cboCategoriaAgregar = new JComboBox<>();
        txtNombreAgregar = new JTextField(20);
        txtMontoAgregar = new JTextField(20);
        txtMontoAgregar.setText("0.00");

        listaCategorias = cargarCategorias(cboCategoriaAgregar);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1;
        formulario.add(cboCategoriaAgregar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtNombreAgregar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formulario.add(new JLabel("Monto Estimado:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtMontoAgregar, gbc);

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
     * Crea el panel de la pestaña Modificar.
     *
     * @return JPanel con los componentes de modificar
     */
    private JPanel crearPanelModificar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        modeloModificar = new DefaultTableModel(
                new String[] { "ID", "Categoría", "Nombre",
                        "Monto Estimado" },
                0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tablaModificar = new JTable(modeloModificar);
        JScrollPane scrollModificar = new JScrollPane(tablaModificar);
        scrollModificar.setPreferredSize(new Dimension(850, 200));

        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cboCategoriaModificar = new JComboBox<>();
        txtNombreModificar = new JTextField(20);
        txtMontoModificar = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1;
        formulario.add(cboCategoriaModificar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtNombreModificar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formulario.add(new JLabel("Monto Estimado:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtMontoModificar, gbc);

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
     * Crea el panel de la pestaña Eliminar.
     *
     * @return JPanel con los componentes de eliminar
     */
    private JPanel crearPanelEliminar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        modeloEliminar = new DefaultTableModel(
                new String[] { "ID", "Categoría", "Nombre",
                        "Monto Estimado" },
                0) {
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
     * Crea el panel de la pestaña Consultar.
     *
     * @return JPanel con los componentes de consultar
     */
    private JPanel crearPanelConsultar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelFiltros = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.add(new JLabel("Categoría:"));
        cboCategoriaConsultar = new JComboBox<>();
        cboCategoriaConsultar.addItem("-- Todas --");
        panelFiltros.add(cboCategoriaConsultar);

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
                new String[] { "ID", "Categoría", "Nombre",
                        "Monto Estimado" },
                0) {
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
            if (listaCategorias == null || listaCategorias.isEmpty()) {
                MenuPrincipal.mostrarMensaje(
                        "No hay categorías disponibles. "
                                + "Registre al menos una categoría primero.");
                return;
            }

            Servicio servicio = new Servicio();
            servicio.setIdCategoria(
                    obtenerIdCategoriaSeleccionada(
                            cboCategoriaAgregar, listaCategorias));
            servicio.setNombre(txtNombreAgregar.getText());

            try {
                servicio.setMontoEstimado(
                        Double.parseDouble(
                                txtMontoAgregar.getText().trim()));
            } catch (NumberFormatException ex) {
                MenuPrincipal.mostrarMensaje(
                        "El monto estimado debe ser un número válido.");
                return;
            }

            if (!servicio.verificarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Complete todos los campos obligatorios "
                                + "(categoría y nombre).");
                return;
            }

            ServiciosMD serviciosMD = new ServiciosMD();
            if (serviciosMD.existeNombre(servicio)) {
                MenuPrincipal.mostrarMensaje(
                        "Ya existe un servicio con ese nombre en "
                                + "la categoría seleccionada. "
                                + "Ingrese otro nombre.");
                return;
            }

            if (servicio.grabarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Servicio registrado exitosamente.");
                servicio.limpiarDatos();
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
            if (indice == 0) {
                listaCategorias = cargarCategorias(cboCategoriaAgregar);
            } else if (indice == 1) {
                listaCategoriasModificar = cargarCategorias(
                        cboCategoriaModificar);
                cargarTablaModificar();
            } else if (indice == 2) {
                cargarTablaEliminar();
            } else if (indice == 3) {
                cargarCategoriasConsultar();
            }
        });

        tablaModificar.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaModificar.getSelectedRow();
                if (fila >= 0) {
                    idServicioSeleccionadoModificar = Integer.parseInt(
                            modeloModificar.getValueAt(fila, 0).toString());
                    String categoriaNombre = modeloModificar.getValueAt(fila, 1).toString();
                    cboCategoriaModificar.setSelectedItem(categoriaNombre);
                    txtNombreModificar.setText(
                            modeloModificar.getValueAt(fila, 2).toString());
                    txtMontoModificar.setText(
                            modeloModificar.getValueAt(fila, 3).toString());
                }
            }
        });

        btnModificar.addActionListener(e -> {
            if (idServicioSeleccionadoModificar == 0) {
                MenuPrincipal.mostrarMensaje(
                        "Seleccione un servicio de la tabla.");
                return;
            }

            Servicio servicio = new Servicio();
            servicio.setIdServicio(idServicioSeleccionadoModificar);
            servicio.setIdCategoria(
                    obtenerIdCategoriaSeleccionada(
                            cboCategoriaModificar,
                            listaCategoriasModificar));
            servicio.setNombre(txtNombreModificar.getText());

            try {
                servicio.setMontoEstimado(
                        Double.parseDouble(
                                txtMontoModificar.getText().trim()));
            } catch (NumberFormatException ex) {
                MenuPrincipal.mostrarMensaje(
                        "El monto estimado debe ser un número válido.");
                return;
            }

            if (!servicio.verificarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Complete todos los campos obligatorios.");
                return;
            }

            ServiciosMD serviciosMD = new ServiciosMD();
            if (serviciosMD.existeNombreExcluyendo(servicio)) {
                MenuPrincipal.mostrarMensaje(
                        "Ya existe otro servicio con ese nombre "
                                + "en la categoría seleccionada. "
                                + "Ingrese un nombre diferente.");
                return;
            }

            if (servicio.modificarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Servicio modificado exitosamente.");
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
                    idServicioSeleccionadoEliminar = Integer.parseInt(
                            modeloEliminar.getValueAt(fila, 0).toString());
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            if (idServicioSeleccionadoEliminar == 0) {
                MenuPrincipal.mostrarMensaje(
                        "Seleccione un servicio de la tabla.");
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar este servicio?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            Servicio servicio = new Servicio();
            servicio.setIdServicio(idServicioSeleccionadoEliminar);

            ServiciosMD serviciosMD = new ServiciosMD();
            if (serviciosMD.verificarMovimientos(servicio)) {
                MenuPrincipal.mostrarMensaje(
                        "No se puede eliminar el servicio porque "
                                + "tiene movimientos asociados.");
                return;
            }

            if (servicio.borrarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Servicio eliminado exitosamente.");
                cargarTablaEliminar();
                idServicioSeleccionadoEliminar = 0;
            } else {
                MenuPrincipal.mostrarMensaje(
                        "Problemas con bases de datos. "
                                + "Contactar a soporte.");
            }
        });

        // ── Pestaña Consultar ────────────────────────────────
        btnBuscarConsultar.addActionListener(e -> {
            buscarServicios();
        });
    }

    /**
     * Carga las categorías en el combo de la pestaña Consultar
     * incluyendo la opción "-- Todas --".
     */
    private void cargarCategoriasConsultar() {
        cboCategoriaConsultar.removeAllItems();
        cboCategoriaConsultar.addItem("-- Todas --");
        CategoriaMD categoriaMD = new CategoriaMD();
        listaCategoriasConsultar = categoriaMD.cargarCategoriaList();
        for (Categoria cat : listaCategoriasConsultar) {
            cboCategoriaConsultar.addItem(cat.getNombre());
        }
    }

    /**
     * Carga la tabla de la pestaña Modificar.
     */
    private void cargarTablaModificar() {
        modeloModificar.setRowCount(0);
        Servicio servicio = new Servicio();
        ResultSet rs = servicio.buscarServicios();
        try {
            if (rs != null) {
                while (rs.next()) {
                    modeloModificar.addRow(new Object[] {
                            rs.getInt("id_servicio"),
                            rs.getString("nombre_categoria"),
                            rs.getString("nombre"),
                            rs.getDouble("monto_estimado")
                    });
                }
            }
        } catch (SQLException ex) {
            MenuPrincipal.mostrarMensaje(
                    "Problemas con bases de datos. Contactar a soporte.");
        }
    }

    /**
     * Carga la tabla de la pestaña Eliminar.
     */
    private void cargarTablaEliminar() {
        modeloEliminar.setRowCount(0);
        Servicio servicio = new Servicio();
        ResultSet rs = servicio.buscarServicios();
        try {
            if (rs != null) {
                while (rs.next()) {
                    modeloEliminar.addRow(new Object[] {
                            rs.getInt("id_servicio"),
                            rs.getString("nombre_categoria"),
                            rs.getString("nombre"),
                            rs.getDouble("monto_estimado")
                    });
                }
            }
        } catch (SQLException ex) {
            MenuPrincipal.mostrarMensaje(
                    "Problemas con bases de datos. Contactar a soporte.");
        }
    }

    /**
     * Busca servicios según los filtros de la pestaña Consultar.
     */
    private void buscarServicios() {
        modeloConsultar.setRowCount(0);
        lblMensajeConsulta.setText(" ");

        Servicio filtro = new Servicio();
        int indiceCategoria = cboCategoriaConsultar.getSelectedIndex();
        if (indiceCategoria > 0 && listaCategoriasConsultar != null
                && indiceCategoria - 1 < listaCategoriasConsultar.size()) {
            filtro.setIdCategoria(
                    listaCategoriasConsultar.get(
                            indiceCategoria - 1).getIdCategoria());
        }

        String nombreFiltro = txtNombreConsultar.getText().trim();
        if (!nombreFiltro.isEmpty()) {
            filtro.setNombre(nombreFiltro);
        }

        ServiciosMD serviciosMD = new ServiciosMD();
        ResultSet rs;
        if (filtro.getIdCategoria() == 0
                && (filtro.getNombre() == null
                        || filtro.getNombre().isEmpty())) {
            rs = serviciosMD.consultar();
        } else {
            rs = serviciosMD.consultar(filtro);
        }

        try {
            if (rs != null) {
                boolean hayResultados = false;
                while (rs.next()) {
                    hayResultados = true;
                    modeloConsultar.addRow(new Object[] {
                            rs.getInt("id_servicio"),
                            rs.getString("nombre_categoria"),
                            rs.getString("nombre"),
                            rs.getDouble("monto_estimado")
                    });
                }
                if (!hayResultados) {
                    if (nombreFiltro.isEmpty() && indiceCategoria <= 0) {
                        lblMensajeConsulta.setText(
                                "No hay servicios registrados.");
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
        if (cboCategoriaAgregar.getItemCount() > 0) {
            cboCategoriaAgregar.setSelectedIndex(0);
        }
        txtNombreAgregar.setText("");
        txtMontoAgregar.setText("0.00");
    }

    /**
     * Limpia los campos del formulario de modificar.
     */
    private void limpiarFormularioModificar() {
        if (cboCategoriaModificar.getItemCount() > 0) {
            cboCategoriaModificar.setSelectedIndex(0);
        }
        txtNombreModificar.setText("");
        txtMontoModificar.setText("");
        idServicioSeleccionadoModificar = 0;
        tablaModificar.clearSelection();
    }
}
