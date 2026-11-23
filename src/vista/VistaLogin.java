package vista;

import dao.UsuarioDAO; 
import modelo.Usuario; 
import Estilo.Estilo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class VistaLogin extends JFrame {

    private JTextField txtNombre;
    private JPasswordField txtPassword;
    private JButton btnIngresar;

    public VistaLogin() {
        setTitle("Pastelería Premium - Acceso");
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2));
        add(panelPrincipal);

        JPanel panelIzquierdo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, Estilo.FRESA, getWidth(), getHeight(), Estilo.CARAMELO);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(Estilo.BLANCO);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 42));
                g2.drawString("La", 60, 200);
                g2.drawString("Conchita", 60, 250);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                g2.drawString("Sistema de Gestión", 60, 290);
            }
        };
        panelPrincipal.add(panelIzquierdo);

        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setBackground(Estilo.BLANCO);
        panelPrincipal.add(panelDerecho);

        JPanel tarjeta = new JPanel();
        tarjeta.setBackground(Estilo.BLANCO);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel lblTitulo = new JLabel("¡Bienvenido!");
        lblTitulo.setFont(Estilo.F_TITULO);
        lblTitulo.setForeground(Estilo.CHOCOLATE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Ingresa tus credenciales para continuar");
        lblSubtitulo.setFont(Estilo.F_SUBTITULO);
        lblSubtitulo.setForeground(Estilo.TEXTO_GRIS);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitulo.setBorder(new EmptyBorder(5, 0, 30, 0));

        JLabel lblNombre = new JLabel("Nombre de Usuario");
        lblNombre.setFont(Estilo.F_TEXTO);
        lblNombre.setForeground(Estilo.CHOCOLATE);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        txtNombre = Estilo.campo(20);
        txtNombre.setMaximumSize(new Dimension(280, 35));
        txtNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblPass = new JLabel("Contraseña (No. Domicilio)");
        lblPass.setFont(Estilo.F_TEXTO);
        lblPass.setForeground(Estilo.CHOCOLATE);
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPass.setBorder(new EmptyBorder(15, 0, 0, 0));

        txtPassword = new JPasswordField(20);
        txtPassword.setFont(Estilo.F_TEXTO);
        txtPassword.setMaximumSize(new Dimension(280, 35));
        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDE_SUAVE, 1),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));

        // Enter en nombre pasa a contraseña
        txtNombre.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "moverAPassword");
        txtNombre.getActionMap().put("moverAPassword", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { txtPassword.requestFocusInWindow(); }
        });

        // Enter en contraseña hace login
        txtPassword.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "hacerLogin");
        txtPassword.getActionMap().put("hacerLogin", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { iniciarSesion(); }
        });

        btnIngresar = new JButton("Iniciar Sesión") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(Estilo.CHOCOLATE);
                else if (getModel().isRollover()) g2.setColor(Estilo.FRESA);
                else g2.setColor(Estilo.CARAMELO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        btnIngresar.setFont(Estilo.F_BOTON);
        btnIngresar.setForeground(Estilo.BLANCO);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setContentAreaFilled(false);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIngresar.setMaximumSize(new Dimension(280, 45));

        btnIngresar.addActionListener(e -> iniciarSesion());

        tarjeta.add(lblTitulo);
        tarjeta.add(lblSubtitulo);
        tarjeta.add(lblNombre);
        tarjeta.add(txtNombre);
        tarjeta.add(lblPass);
        tarjeta.add(txtPassword);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 25))); 
        tarjeta.add(btnIngresar);

        panelDerecho.add(tarjeta);
        SwingUtilities.invokeLater(() -> txtNombre.requestFocusInWindow());
    }

    private void iniciarSesion() {
        String nombre = txtNombre.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (nombre.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, llena todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- MENSAJES DE DEBUG ---
        System.out.println("=== INTENTO DE LOGIN ===");
        System.out.println("Nombre enviado: [" + nombre + "]");
        System.out.println("Password enviado: [" + password + "]");

        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.validarLogin(nombre, password);

        System.out.println("Resultado de la BD: " + (usuario != null ? "Usuario Encontrado (" + usuario.getNombre() + ")" : "NULO (No existe)"));

        if (usuario != null) {
            System.out.println("Rol obtenido: " + usuario.getRol());

            if (usuario.getRol() != null && usuario.getRol().equals("Cliente")) {
                JOptionPane.showMessageDialog(this, "Los clientes no tienen acceso al sistema administrativo.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "¡Bienvenido(a) " + usuario.getNombre() + "!", "Acceso Correcto", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); 
            
            VistaInicio ventanaPrincipal = new VistaInicio(usuario.getRol());
            ventanaPrincipal.setVisible(true);
            
        } else {
            JOptionPane.showMessageDialog(this, "Nombre o contraseña incorrectos.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtPassword.requestFocusInWindow();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new VistaLogin().setVisible(true));
    }
}