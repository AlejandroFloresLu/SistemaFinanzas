package vista;


import modelo.Categoria;
import modelo.Servicio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

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

    private JPanel panelPrincipal;
    private CardLayout cardLayout;

    // ── Pestaña Agregar ──────────────────────────────────────
    private JComboBox<String> cboCategoriaAgregar;
    private JTextField txtNombreAgregar;
    private JButton btnGuardarAgregar;
    private List<Categoria> listaCategorias;

    // ── Pestaña Modificar ────────────────────────────────────
    private JTable tablaModificar;
    private DefaultTableModel modeloModificar;
    private JComboBox<String> cboCategoriaModificar;
    private JTextField txtNombreModificar;
    private JButton btnAbrirFormularioModificar;
    private JButton btnGuardarModificacion;
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
        JPanel panelOpciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnNuevo = new JButton("Nuevo Servicio");
        JButton btnModificarOpcion = new JButton("Modificar Servicio");
        JButton btnEliminarOpcion = new JButton("Eliminar Servicio");
        JButton btnConsultar = new JButton("Consultar Servicios");

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
        btnNuevo.addActionListener(e -> {
            listaCategorias = cargarCategorias(cboCategoriaAgregar);
            cardLayout.show(panelPrincipal, "Agregar");
        });
        btnModificarOpcion.addActionListener(e -> {
            listaCategoriasModificar = cargarCategorias(cboCategoriaModificar);
            cargarTablaModificar();
            cardLayout.show(panelPrincipal, "Modificar");
        });
        btnEliminarOpcion.addActionListener(e -> {
            cargarTablaEliminar();
            cardLayout.show(panelPrincipal, "Eliminar");
        });
        btnConsultar.addActionListener(e -> {
            cargarCategoriasConsultar();
            cardLayout.show(panelPrincipal, "Consultar");
        });
    }

    /**
     * Carga las categorías disponibles en un combo box.
     *
     * @param combo JComboBox a llenar con las categorías
     * @return lista de categorías cargadas
     */
    private List<Categoria> cargarCategorias(JComboBox<String> combo) {
        combo.removeAllItems();
        modelo.Categoria categoria = new modelo.Categoria();
        List<Categoria> categorias = categoria.buscarCategorias();
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
        txtNombreAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    evt.consume();
                }
            }
        });

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
     * Crea el panel de la pestaña Modificar.
     *
     * @return JPanel con los componentes de modificar
     */
    private JPanel crearPanelModificar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        modeloModificar = new DefaultTableModel(
                new String[] { "ID", "Categoría", "Nombre" },
                0) {
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

        cboCategoriaModificar = new JComboBox<>();
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
        formulario.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1;
        formulario.add(cboCategoriaModificar, gbc);

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
     * Crea el panel de la pestaña Eliminar.
     *
     * @return JPanel con los componentes de eliminar
     */
    private JPanel crearPanelEliminar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        modeloEliminar = new DefaultTableModel(
                new String[] { "ID", "Categoría", "Nombre" },
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
        btnEliminar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
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
        btnBuscarConsultar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        btnBuscarConsultar.setFocusPainted(false);
        panelFiltros.add(btnBuscarConsultar);

        modeloConsultar = new DefaultTableModel(
                new String[] { "ID", "Categoría", "Nombre" },
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
                this.mostrarMensaje(
                        "No hay categorías disponibles. "
                                + "Registre al menos una categoría primero.");
                return;
            }

            Servicio servicio = new Servicio();
            servicio.setIdCategoria(
                    obtenerIdCategoriaSeleccionada(
                            cboCategoriaAgregar, listaCategorias));
            servicio.setNombre(txtNombreAgregar.getText().trim().toLowerCase());

            if (!servicio.verificarDP()) {
                this.mostrarMensaje(
                        "Complete todos los campos obligatorios "
                                + "(categoría y nombre).");
                return;
            }

            if (servicio.existeNombre()) {
                this.mostrarMensaje(
                        "Ya existe un servicio con ese nombre en "
                                + "la categoría seleccionada. "
                                + "Ingrese otro nombre.");
                return;
            }

            if (servicio.grabarDP()) {
                this.mostrarMensaje(
                        "Servicio registrado exitosamente.");
                servicio.limpiarDatos();
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
                this.mostrarMensaje("Seleccione un servicio de la tabla.");
                return;
            }
            idServicioSeleccionadoModificar = Integer.parseInt(
                    modeloModificar.getValueAt(fila, 0).toString());
            String categoriaNombre = modeloModificar.getValueAt(fila, 1).toString();
            cboCategoriaModificar.setSelectedItem(categoriaNombre);
            txtNombreModificar.setText(
                    modeloModificar.getValueAt(fila, 2).toString());
            
            cardLayout.show(panelPrincipal, "FormularioModificar");
        });

        btnGuardarModificacion.addActionListener(e -> {
            Servicio servicio = new Servicio();
            servicio.setIdServicio(idServicioSeleccionadoModificar);
            servicio.setIdCategoria(
                    obtenerIdCategoriaSeleccionada(
                            cboCategoriaModificar,
                            listaCategoriasModificar));
            servicio.setNombre(txtNombreModificar.getText().trim().toLowerCase());

            if (!servicio.verificarDP()) {
                this.mostrarMensaje(
                        "Complete todos los campos obligatorios.");
                return;
            }

            if (servicio.existeNombreExcluyendo()) {
                this.mostrarMensaje(
                        "Ya existe otro servicio con ese nombre "
                                + "en la categoría seleccionada. "
                                + "Ingrese un nombre diferente.");
                return;
            }

            if (servicio.modificarDP()) {
                this.mostrarMensaje(
                        "Servicio modificado exitosamente.");
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
                    idServicioSeleccionadoEliminar = Integer.parseInt(
                            modeloEliminar.getValueAt(fila, 0).toString());
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            if (idServicioSeleccionadoEliminar == 0) {
                this.mostrarMensaje(
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

            if (servicio.verificarMovimientos(servicio)) {
                this.mostrarMensaje(
                        "No se puede eliminar el servicio porque "
                                + "tiene movimientos asociados.");
                return;
            }

            if (servicio.borrarDP()) {
                this.mostrarMensaje(
                        "Servicio eliminado exitosamente.");
                cargarTablaEliminar();
                idServicioSeleccionadoEliminar = 0;
            } else {
                this.mostrarMensaje(
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
        Categoria categoria = new Categoria();
        listaCategoriasConsultar = categoria.buscarCategorias();
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
        List<Servicio> lista = servicio.buscarServicios();
        if (lista != null) {
            for (Servicio s : lista) {
                modeloModificar.addRow(new Object[] {
                        s.getIdServicio(),
                        s.getNombreCategoria(),
                        s.getNombre()
                });
            }
        }
    }

    /**
     * Carga la tabla de la pestaña Eliminar.
     */
    private void cargarTablaEliminar() {
        modeloEliminar.setRowCount(0);
        Servicio servicio = new Servicio();
        List<Servicio> lista = servicio.buscarServicios();
        if (lista != null) {
            for (Servicio s : lista) {
                modeloEliminar.addRow(new Object[] {
                        s.getIdServicio(),
                        s.getNombreCategoria(),
                        s.getNombre()
                });
            }
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

        Servicio servicio = new Servicio();
        List<Servicio> rs;
        if (filtro.getIdCategoria() == 0
                && (filtro.getNombre() == null
                        || filtro.getNombre().isEmpty())) {
            rs = servicio.buscarServicios();
        } else {
            rs = servicio.buscarServicios(filtro);
        }

        if (rs != null) {
            boolean hayResultados = false;
            for (Servicio s : rs) {
                hayResultados = true;
                modeloConsultar.addRow(new Object[] {
                        s.getIdServicio(),
                        s.getNombreCategoria(),
                        s.getNombre()
                });
            }
            if (!hayResultados) {
                if (nombreFiltro.isEmpty() && indiceCategoria <= 0) {
                    lblMensajeConsulta.setText("No hay servicios registrados.");
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
        if (cboCategoriaAgregar.getItemCount() > 0) {
            cboCategoriaAgregar.setSelectedIndex(0);
        }
        txtNombreAgregar.setText("");
    }

    /**
     * Limpia los campos del formulario de modificar.
     */
    private void limpiarFormularioModificar() {
        if (cboCategoriaModificar.getItemCount() > 0) {
            cboCategoriaModificar.setSelectedIndex(0);
        }
        txtNombreModificar.setText("");
        idServicioSeleccionadoModificar = 0;
        tablaModificar.clearSelection();
    }

    public void mostrarListado() {
        // Método para cumplir con el diagrama de clases
    }

    public void mostrarMensaje(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }
}
