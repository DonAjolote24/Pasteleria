package component;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Estilo.Estilo;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class MenuLateral extends JPanel {

    private JPanel panelBotones;

    public MenuLateral(CardLayout cardLayout, JPanel contenido, JLabel tituloSeccion, String rolUsuario) {
        setLayout(new java.awt.BorderLayout());
        setBackground(Estilo.BLANCO);
        setPreferredSize(new Dimension(260, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Estilo.BORDE_SUAVE));

        panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBackground(Estilo.BLANCO);

        panelBotones.add(Box.createRigidArea(new Dimension(0, 16)));

        seccion("SISTEMA");
        
        // LÓGICA DE ROLES
        if (rolUsuario != null && (rolUsuario.equals("Administrador") || rolUsuario.equals("Gerente"))) {
            // ACCESO TOTAL
            boton("Gestión de usuarios",       "usuarios",  "👥", cardLayout, contenido, tituloSeccion);
            boton("Gestión de ventas",         "ventas",    "🛒", cardLayout, contenido, tituloSeccion);
            boton("Gestión de pedidos",        "pedidos",   "📝", cardLayout, contenido, tituloSeccion);
            boton("Gestión de productos",      "productos", "📦", cardLayout, contenido, tituloSeccion);
            // CAMBIO AQUÍ: De insumos a materias primas
            boton("Gestión de materias primas", "insumos",   "🥣", cardLayout, contenido, tituloSeccion);
            boton("Gestión de recetas",        "recetas",   "📖", cardLayout, contenido, tituloSeccion);
            
        } else if (rolUsuario != null && rolUsuario.equals("Empleado")) {
            // ACCESO RESTRINGIDO
            boton("Gestión de ventas",         "ventas",    "🛒", cardLayout, contenido, tituloSeccion);
            boton("Gestión de pedidos",        "pedidos",   "📝", cardLayout, contenido, tituloSeccion);
            boton("Gestión de productos",      "productos", "📦", cardLayout, contenido, tituloSeccion);
            // CAMBIO AQUÍ: De insumos a materias primas
            boton("Gestión de materias primas", "insumos",   "🥣", cardLayout, contenido, tituloSeccion);
        }

        panelBotones.add(Box.createVerticalGlue());

        JLabel version = new JLabel("v3.0  ·  Pastelería Premium");
        version.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        version.setForeground(new Color(180, 160, 155));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);
        version.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        panelBotones.add(version);

        JScrollPane scrollPane = new JScrollPane(panelBotones);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Estilo.BLANCO);
        scrollPane.getViewport().setBackground(Estilo.BLANCO);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));

        add(scrollPane, java.awt.BorderLayout.CENTER);
    }

    private void seccion(String titulo) {
        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 10));
        etiqueta.setForeground(new Color(180, 160, 155));
        etiqueta.setBorder(BorderFactory.createEmptyBorder(12, 26, 6, 10));
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelBotones.add(etiqueta);
    }

    private void boton(String texto, String tarjeta, String icono, CardLayout cardLayout, JPanel contenido, JLabel tituloSeccion) {
        JButton boton = new JButton(icono + "   " + texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover() || getModel().isPressed()) {
                    g2.setColor(Estilo.FRESA_CLARO);
                    g2.fillRoundRect(8, 2, getWidth() - 16, getHeight() - 4, 10, 10);
                    g2.setColor(Estilo.FRESA);
                    g2.fillRoundRect(8, 8, 4, getHeight() - 16, 3, 3);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };

        boton.setFont(Estilo.F_MENU);
        boton.setForeground(Estilo.CHOCOLATE);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 26, 10, 15));
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { boton.setForeground(Estilo.FRESA); }
            @Override
            public void mouseExited(MouseEvent e) { boton.setForeground(Estilo.CHOCOLATE); }
        });

        boton.addActionListener(evento -> {
            cardLayout.show(contenido, tarjeta);
            tituloSeccion.setText(texto);
        });

        panelBotones.add(boton);
    }
}