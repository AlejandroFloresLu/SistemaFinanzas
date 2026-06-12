package vista;

import javax.swing.*;
import java.awt.*;

/**
 * Clase principal del sistema de gestión financiera.
 * Capa: Vista (GUI).
 * Proporciona el menú de navegación principal con acceso a los
 * módulos de Usuarios, Cuentas, Categorías y Servicios.
 *
 * @author Samantha Nuñez
 */
public class MenuPrincipal extends JFrame {

    /** Identificador del usuario autenticado (hardcodeado). */
    public static final int ID_USUARIO_ACTIVO = 1;

    private JButton btnUsuarios;
    private JButton btnCuentas;
    private JButton btnCategorias;
    private JButton btnServicios;
    private JLabel lblTitulo;
    private JLabel lblSubtitulo;

    /**
     * Constructor del menú principal.
     * Inicializa los componentes gráficos y los eventos.
     */
    public MenuPrincipal() {
        setTitle("Sistema de Gestión Financiera");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponentes();
        initEventos();
    }

    /**
     * Inicializa y organiza todos los componentes gráficos del menú.
     */
    private void initComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(new Color(30, 39, 46));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(
                30, 30, 30, 30));

        // ── Panel superior (título) ──────────────────────────
        JPanel panelTitulo = new JPanel(new GridLayout(2, 1, 0, 5));
        panelTitulo.setOpaque(false);

        lblTitulo = new JLabel("💰 Sistema de Gestión Financiera",
                SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);

        lblSubtitulo = new JLabel(
                "Seleccione un módulo para comenzar",
                SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(178, 190, 195));

        panelTitulo.add(lblTitulo);
        panelTitulo.add(lblSubtitulo);

        // ── Panel central (botones) ──────────────────────────
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 20, 20));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(
                30, 40, 30, 40));

        btnUsuarios = crearBoton("👤 Usuarios",
                new Color(52, 152, 219));
        btnCuentas = crearBoton("💰 Cuentas",
                new Color(46, 204, 113));
        btnCategorias = crearBoton("📂 Categorías",
                new Color(155, 89, 182));
        btnServicios = crearBoton("🔧 Servicios",
                new Color(230, 126, 34));

        panelBotones.add(btnUsuarios);
        panelBotones.add(btnCuentas);
        panelBotones.add(btnCategorias);
        panelBotones.add(btnServicios);

        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        setContentPane(panelPrincipal);
    }

    /**
     * Crea un botón estilizado para el menú principal.
     *
     * @param texto texto a mostrar en el botón
     * @param color color de fondo del botón
     * @return JButton estilizado
     */
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        return boton;
    }

    /**
     * Inicializa los eventos de los botones del menú.
     * Cada botón abre la ventana correspondiente.
     */
    private void initEventos() {
        btnUsuarios.addActionListener(e -> {
            new VentanaUsuarios().setVisible(true);
        });

        btnCuentas.addActionListener(e -> {
            new VentanaCuentas().setVisible(true);
        });

        btnCategorias.addActionListener(e -> {
            new VentanaCategoria().setVisible(true);
        });

        btnServicios.addActionListener(e -> {
            new VentanaServicios().setVisible(true);
        });
    }

    /**
     * Muestra un mensaje informativo al usuario mediante JOptionPane.
     *
     * @param texto mensaje a mostrar
     */
    public static void mostrarMensaje(String texto) {
        JOptionPane.showMessageDialog(null, texto,
                "Sistema de Gestión Financiera",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Punto de entrada de la aplicación.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MenuPrincipal().setVisible(true);
        });
    }
}
