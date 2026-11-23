package Estilo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public final class Estilo {

    public static final Color CHOCOLATE    = new Color(74, 44, 42);
    public static final Color FRESA        = new Color(224, 122, 155);
    public static final Color FRESA_CLARO  = new Color(252, 228, 236);
    public static final Color VAINILLA     = new Color(255, 248, 238);
    public static final Color MANTEQUILLA  = new Color(245, 230, 211);
    public static final Color CARAMELO     = new Color(212, 160, 95);
    public static final Color MENTA        = new Color(139, 201, 165);
    public static final Color CEREZA       = new Color(217, 99, 95);
    public static final Color LAVANDA      = new Color(183, 154, 217);
    public static final Color CIELO        = new Color(143, 184, 217);
    public static final Color BLANCO       = new Color(255, 255, 255);
    public static final Color TEXTO_GRIS   = new Color(140, 118, 115);
    public static final Color BORDE_SUAVE  = new Color(240, 228, 218);
    public static final Color SOMBRA_SUAVE = new Color(248, 240, 232);

    public static final Font F_TITULO    = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font F_SUBTITULO = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font F_SECCION   = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font F_TEXTO     = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font F_BOTON     = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font F_MENU      = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font F_LOGO      = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font F_NUMERO    = new Font("Segoe UI", Font.BOLD, 30);

    private Estilo() {
    }

    public static JButton primario(String t) {
        return boton(t, FRESA, BLANCO, CHOCOLATE);
    }

    public static JButton exito(String t) {
        return boton(t, MENTA, BLANCO, new Color(105, 170, 135));
    }

    public static JButton peligro(String t) {
        return boton(t, CEREZA, BLANCO, new Color(180, 70, 68));
    }

    public static JButton neutro(String t) {
        return boton(t, SOMBRA_SUAVE, CHOCOLATE, BORDE_SUAVE);
    }

    public static JButton caramelo(String t) {
        return boton(t, CARAMELO, BLANCO, new Color(170, 125, 70));
    }

    public static JButton boton(String t, Color fondo, Color texto, Color borde) {
        JButton b = new JButton(t);
        b.setFont(F_BOTON);
        b.setBackground(fondo);
        b.setForeground(texto);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borde, 1),
                BorderFactory.createEmptyBorder(9, 20, 9, 20)
        ));
        return b;
    }

    public static JTextField campo(int cols) {
        JTextField c = new JTextField(cols);
        c.setFont(F_TEXTO);
        c.setBackground(BLANCO);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_SUAVE, 1),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
        return c;
    }

    public static JTextArea area(int filas, int cols) {
        JTextArea a = new JTextArea(filas, cols);
        a.setFont(F_TEXTO);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBorder(BorderFactory.createEmptyBorder(7, 10, 7, 10));
        a.setBackground(BLANCO);
        return a;
    }

    public static JComboBox<String> combo(String... items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(F_TEXTO);
        c.setBackground(BLANCO);
        return c;
    }

    public static JTable tabla(String[] columnas, Object[][] datos) {
        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int f, int c) {
                return false;
            }
        };

        JTable t = new JTable(modelo);
        t.setRowHeight(32);
        t.setFont(F_TEXTO);
        t.setGridColor(SOMBRA_SUAVE);
        t.setShowVerticalLines(false);
        t.setBackground(BLANCO);
        t.setSelectionBackground(FRESA_CLARO);
        t.setSelectionForeground(CHOCOLATE);
        t.getTableHeader().setFont(F_SECCION);
        t.getTableHeader().setBackground(FRESA_CLARO);
        t.getTableHeader().setForeground(CHOCOLATE);
        t.getTableHeader().setReorderingAllowed(false);
        t.getTableHeader().setPreferredSize(new Dimension(0, 36));
        return t;
    }

    public static JScrollPane envolver(Component comp, String titulo) {
        JScrollPane scroll = new JScrollPane(comp);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDE_SUAVE, 1),
                titulo,
                0, 0,
                F_SECCION,
                CHOCOLATE
        ));
        scroll.getViewport().setBackground(BLANCO);
        return scroll;
    }

    public static JPanel encabezado(String titulo, String subtitulo) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel t = new JLabel(titulo);
        t.setFont(F_TITULO);
        t.setForeground(CHOCOLATE);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel s = new JLabel(subtitulo);
        s.setFont(F_SUBTITULO);
        s.setForeground(TEXTO_GRIS);
        s.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(t);
        p.add(Box.createRigidArea(new Dimension(0, 4)));
        p.add(s);

        return p;
    }

    public static JPanel base(String titulo, String subtitulo) {
        JPanel panel = new JPanel(new java.awt.BorderLayout(15, 15));
        panel.setBackground(VAINILLA);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        panel.add(encabezado(titulo, subtitulo), java.awt.BorderLayout.NORTH);
        return panel;
    }

    public static JPanel barraBusqueda(String placeholder, JButton... extras) {
        JPanel barra = new JPanel(new java.awt.BorderLayout(10, 0));
        barra.setOpaque(false);

        JTextField campo = campo(20);
        campo.setToolTipText(placeholder);

        JLabel etiqueta = new JLabel("Buscar:");
        etiqueta.setFont(F_TEXTO);
        etiqueta.setForeground(TEXTO_GRIS);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botones.setOpaque(false);
        botones.add(primario("Buscar"));
        botones.add(neutro("Mostrar todos"));
        for (JButton extra : extras) {
            botones.add(extra);
        }

        barra.add(etiqueta, java.awt.BorderLayout.WEST);
        barra.add(campo, java.awt.BorderLayout.CENTER);
        barra.add(botones, java.awt.BorderLayout.EAST);

        return barra;
    }

    public static JPanel acciones(JButton... botones) {
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pie.setOpaque(false);
        for (JButton b : botones) {
            pie.add(b);
        }
        return pie;
    }

    public static JLabel resumen(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(F_TEXTO);
        lbl.setForeground(TEXTO_GRIS);
        return lbl;
    }

    public static JPanel pie(JLabel resumen, JButton... botones) {
        JPanel pie = new JPanel(new java.awt.BorderLayout());
        pie.setOpaque(false);
        pie.add(resumen, java.awt.BorderLayout.WEST);
        pie.add(acciones(botones), java.awt.BorderLayout.EAST);
        return pie;
    }

    public static void agregarFila(
            JPanel panel,
            int f,
            int c,
            String etiqueta,
            Component comp
    ) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = f;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = c;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(F_TEXTO);
        lbl.setForeground(CHOCOLATE);
        panel.add(lbl, gbc);

        gbc.gridx = c + 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(comp, gbc);
    }

    public static javax.swing.border.Border bordeTitulo(String titulo) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDE_SUAVE, 1),
                titulo,
                0, 0,
                F_SECCION,
                CHOCOLATE
        );
    }

    public static JLabel chip(String texto, boolean activo) {
        JLabel chip = new JLabel("  " + texto + "  ");
        chip.setFont(F_BOTON);
        chip.setOpaque(true);
        chip.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        activo ? FRESA : BORDE_SUAVE, 1
                ),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        chip.setBackground(activo ? FRESA : BLANCO);
        chip.setForeground(activo ? BLANCO : TEXTO_GRIS);
        return chip;
    }

    public static JProgressBar barra(int valor, Color color) {
        return barra(valor, color, 10);
    }

    public static JProgressBar barra(int valor, Color color, int alto) {
        JProgressBar barra = new JProgressBar(0, 100);
        barra.setValue(valor);
        barra.setForeground(color);
        barra.setBackground(SOMBRA_SUAVE);
        barra.setBorderPainted(false);
        barra.setPreferredSize(new Dimension(0, alto));
        return barra;
    }

    public static void pintarDegradado(Graphics g, Color a, Color b) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );
        g2.setPaint(new java.awt.GradientPaint(
                0, 0, a,
                g.getClipBounds().width, g.getClipBounds().height, b
        ));
        g2.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        g2.dispose();
    }

    public static void sombra(Graphics g, int w, int h) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setColor(new Color(0, 0, 0, 10));
        g2.fillRoundRect(3, 5, w - 6, h - 6, 16, 16);
        g2.dispose();
    }

    public static JPanel panelTablaConPie(
            JTable tabla,
            String titulo,
            JLabel resumen,
            JButton... botones
    ) {
        JPanel contenedor = new JPanel(new java.awt.BorderLayout(0, 15));
        contenedor.setOpaque(false);
        contenedor.add(envolver(tabla, titulo), java.awt.BorderLayout.CENTER);
        contenedor.add(pie(resumen, botones), java.awt.BorderLayout.SOUTH);
        return contenedor;
    }

    public static JPanel panelTablaConBarraYPie(
            JTable tabla,
            String titulo,
            JPanel barra,
            JLabel resumen,
            JButton... botones
    ) {
        JPanel contenedor = new JPanel(new java.awt.BorderLayout(0, 15));
        contenedor.setOpaque(false);
        contenedor.add(barra, java.awt.BorderLayout.NORTH);
        contenedor.add(envolver(tabla, titulo), java.awt.BorderLayout.CENTER);
        contenedor.add(pie(resumen, botones), java.awt.BorderLayout.SOUTH);
        return contenedor;
    }
}