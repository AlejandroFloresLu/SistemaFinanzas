package vista;

import datos.CategoriaMD;
import modelo.Categoria;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Ventana de gestión de categorías del sistema.
 * Capa: Vista (GUI).
 * Proporciona las pestañas CRUD (Agregar, Modificar, Eliminar, Consultar)
 * para la entidad Categoria.
 *
 * @author Joan Santacruz
 */
public class VentanaCategoria extends JFrame {

    private JTabbedPane tabbedPane;

    // ── Pestaña Agregar ──────────────────────────────────────
    private JTextField txtNombreAgregar;
    private JTextField txtDescripcionAgregar;
    private JButton btnGuardarAgregar;
    private JButton btnLimpiarAgregar;

    // ── Pestaña Modificar ────────────────────────────────────
    private JTable tablaModificar;
    private DefaultTableModel modeloModificar;
    private JTextField txtNombreModificar;
    private JTextField txtDescripcionModificar;
    private JButton btnModificar;
    private int idCategoriaSeleccionadaModificar = 0;

    // ── Pestaña Eliminar ─────────────────────────────────────
    private JTable tablaEliminar;
    private DefaultTableModel modeloEliminar;
    private JButton btnEliminar;
    private int idCategoriaSeleccionadaEliminar = 0;

    // ── Pestaña Consultar ────────────────────────────────────
    private JTextField txtNombreConsultar;
    private JButton btnBuscarConsultar;
    private JTable tablaConsultar;
    private DefaultTableModel modeloConsultar;
    private JLabel lblMensajeConsulta;

    /**
     * Constructor de la ventana de categorías.
     * Inicializa los componentes gráficos y los eventos.
     */
    public VentanaCategoria() {
        setTitle("📂 Gestión de Categorías");
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

        txtNombreAgregar = new JTextField(20);
        txtDescripcionAgregar = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtNombreAgregar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formulario.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtDescripcionAgregar, gbc);

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
                new String[] { "ID", "Nombre", "Descripción" }, 0) {
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
        txtDescripcionModificar = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtNombreModificar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formulario.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtDescripcionModificar, gbc);

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
                new String[] { "ID", "Nombre", "Descripción" }, 0) {
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
                new String[] { "ID", "Nombre", "Descripción" }, 0) {
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
            Categoria categoria = new Categoria();
            categoria.setNombre(txtNombreAgregar.getText());
            categoria.setDescripcion(txtDescripcionAgregar.getText());

            if (!categoria.verificarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "El nombre de la categoría es obligatorio.");
                return;
            }

            CategoriaMD categoriaMD = new CategoriaMD();
            if (categoriaMD.existeNombre(categoria)) {
                MenuPrincipal.mostrarMensaje(
                        "Ya existe una categoría con ese nombre. "
                                + "Ingrese otro nombre.");
                return;
            }

            if (categoria.grabarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Categoría registrada exitosamente.");
                categoria.limpiarDatos();
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
                    idCategoriaSeleccionadaModificar = Integer.parseInt(
                            modeloModificar.getValueAt(fila, 0).toString());
                    txtNombreModificar.setText(
                            modeloModificar.getValueAt(fila, 1).toString());
                    Object desc = modeloModificar.getValueAt(fila, 2);
                    txtDescripcionModificar.setText(
                            desc != null ? desc.toString() : "");
                }
            }
        });

        btnModificar.addActionListener(e -> {
            if (idCategoriaSeleccionadaModificar == 0) {
                MenuPrincipal.mostrarMensaje(
                        "Seleccione una categoría de la tabla.");
                return;
            }

            Categoria categoria = new Categoria();
            categoria.setIdCategoria(idCategoriaSeleccionadaModificar);
            categoria.setNombre(txtNombreModificar.getText());
            categoria.setDescripcion(txtDescripcionModificar.getText());

            if (!categoria.verificarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "El nombre de la categoría es obligatorio.");
                return;
            }

            CategoriaMD categoriaMD = new CategoriaMD();
            if (categoriaMD.existeNombreExcluyendo(categoria)) {
                MenuPrincipal.mostrarMensaje(
                        "Ya existe otra categoría con ese nombre. "
                                + "Ingrese un nombre diferente.");
                return;
            }

            if (categoria.modificarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Categoría modificada exitosamente.");
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
                    idCategoriaSeleccionadaEliminar = Integer.parseInt(
                            modeloEliminar.getValueAt(fila, 0).toString());
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            if (idCategoriaSeleccionadaEliminar == 0) {
                MenuPrincipal.mostrarMensaje(
                        "Seleccione una categoría de la tabla.");
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar esta categoría?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            Categoria categoria = new Categoria();
            categoria.setIdCategoria(idCategoriaSeleccionadaEliminar);

            CategoriaMD categoriaMD = new CategoriaMD();
            if (categoriaMD.verificarMovimientos(categoria)) {
                MenuPrincipal.mostrarMensaje(
                        "No se puede eliminar la categoría porque "
                                + "tiene servicios asociados.");
                return;
            }

            if (categoria.borrarDP()) {
                MenuPrincipal.mostrarMensaje(
                        "Categoría eliminada exitosamente.");
                cargarTablaEliminar();
                idCategoriaSeleccionadaEliminar = 0;
            } else {
                MenuPrincipal.mostrarMensaje(
                        "Problemas con bases de datos. "
                                + "Contactar a soporte.");
            }
        });

        // ── Pestaña Consultar ────────────────────────────────
        btnBuscarConsultar.addActionListener(e -> {
            buscarCategorias();
        });
    }

    /**
     * Carga la tabla de la pestaña Modificar.
     */
    private void cargarTablaModificar() {
        modeloModificar.setRowCount(0);
        Categoria categoria = new Categoria();
        ResultSet rs = categoria.buscarCategorias();
        try {
            if (rs != null) {
                while (rs.next()) {
                    modeloModificar.addRow(new Object[] {
                            rs.getInt("id_categoria"),
                            rs.getString("nombre"),
                            rs.getString("descripcion")
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
        Categoria categoria = new Categoria();
        ResultSet rs = categoria.buscarCategorias();
        try {
            if (rs != null) {
                while (rs.next()) {
                    modeloEliminar.addRow(new Object[] {
                            rs.getInt("id_categoria"),
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    });
                }
            }
        } catch (SQLException ex) {
            MenuPrincipal.mostrarMensaje(
                    "Problemas con bases de datos. Contactar a soporte.");
        }
    }

    /**
     * Busca categorías según los filtros de la pestaña Consultar.
     */
    private void buscarCategorias() {
        modeloConsultar.setRowCount(0);
        lblMensajeConsulta.setText(" ");
        Categoria categoria = new Categoria();
        ResultSet rs;

        String nombreFiltro = txtNombreConsultar.getText().trim();
        if (nombreFiltro.isEmpty()) {
            rs = categoria.buscarCategorias();
        } else {
            Categoria filtro = new Categoria();
            filtro.setNombre(nombreFiltro);
            rs = categoria.buscarCategorias(filtro);
        }

        try {
            if (rs != null) {
                boolean hayResultados = false;
                while (rs.next()) {
                    hayResultados = true;
                    modeloConsultar.addRow(new Object[] {
                            rs.getInt("id_categoria"),
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    });
                }
                if (!hayResultados) {
                    if (nombreFiltro.isEmpty()) {
                        lblMensajeConsulta.setText(
                                "No hay categorías registradas.");
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
        txtDescripcionAgregar.setText("");
    }

    /**
     * Limpia los campos del formulario de modificar.
     */
    private void limpiarFormularioModificar() {
        txtNombreModificar.setText("");
        txtDescripcionModificar.setText("");
        idCategoriaSeleccionadaModificar = 0;
        tablaModificar.clearSelection();
    }
}
