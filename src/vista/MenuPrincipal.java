package vista;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {
    
    public static final int ID_USUARIO_ACTIVO = 1;

    private JButton btnUsuarios;
    private JButton btnCuentas;
    private JButton btnCategorias;
    private JButton btnServicios;

    public MenuPrincipal() {
        setTitle("Menú Principal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponentes();
        initEventos();
    }

    public void initComponentes() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        btnUsuarios = new JButton("Usuarios");
        btnUsuarios.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        btnCuentas = new JButton("Cuentas");
        btnCuentas.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        btnCategorias = new JButton("Categorías");
        btnCategorias.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        btnServicios = new JButton("Servicios");
        btnServicios.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        panel.add(btnUsuarios);
        panel.add(btnCuentas);
        panel.add(btnCategorias);
        panel.add(btnServicios);
        
        setContentPane(panel);
    }

    public void initEventos() {
        btnUsuarios.addActionListener(e -> ventanaUsuarios());
        btnCuentas.addActionListener(e -> ventanaCuentas());
        btnCategorias.addActionListener(e -> ventanaCategorias());
        btnServicios.addActionListener(e -> ventanaServicios());
    }

    public void ventanaUsuarios() {
        new VentanaUsuarios().setVisible(true);
    }

    public void ventanaCuentas() {
        new VentanaCuentas().setVisible(true);
    }

    public void ventanaCategorias() {
        new VentanaCategoria().setVisible(true);
    }

    public void ventanaServicios() {
        new VentanaServicios().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
