package vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import Estilo.Estilo;
import component.MenuLateral;

public class VistaInicio extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenido;
    private JLabel tituloSeccion;

    public VistaInicio(String rolUsuario) {
        setTitle("Pastelería Premium - Sistema");
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Inicializamos el CardLayout y el panel de contenido
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(Estilo.VAINILLA);

        // 2. Barra superior (Título dinámico)
        tituloSeccion = new JLabel("Inicio");
        tituloSeccion.setFont(Estilo.F_TITULO);
        tituloSeccion.setForeground(Estilo.CHOCOLATE);
        tituloSeccion.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Estilo.BLANCO);
        panelSuperior.add(tituloSeccion, BorderLayout.WEST);
        panelSuperior.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Estilo.BORDE_SUAVE));

        // 3. Instanciamos las Vistas
        // 3.1. El Dashboard (Inicio)
        JPanel panelDashboard = crearPanelDashboard();
        panelContenido.add(panelDashboard, "inicio");

        // 3.2. El Panel de Usuarios
        PanelUsuarios panelUsuarios = new PanelUsuarios();
        panelContenido.add(panelUsuarios, "usuarios");

        // 3.3. El Panel de Materias Primas (NUEVO)
        PanelMateriaPrima panelMaterias = new PanelMateriaPrima();
        panelContenido.add(panelMaterias, "insumos");

        // 4. Instanciamos el Menú Lateral con el rol del usuario
        MenuLateral menu = new MenuLateral(cardLayout, panelContenido, tituloSeccion, rolUsuario);

        // 5. Ensamblamos la ventana
        add(menu, BorderLayout.WEST);
        add(panelSuperior, BorderLayout.NORTH);
        add(panelContenido, BorderLayout.CENTER);

        // 6. Lógica de inicio: Si es Empleado, no mostramos el dashboard por defecto
        if (rolUsuario != null && rolUsuario.equals("Empleado")) {
            // Mandamos al empleado directo a su primera pantalla permitida (insumos, por ejemplo)
            cardLayout.show(panelContenido, "insumos");
            tituloSeccion.setText("Gestión de materias primas");
        } else {
            // Si es Admin o Gerente, se queda en el dashboard
            cardLayout.show(panelContenido, "inicio");
            tituloSeccion.setText("Inicio");
        }
    }

    private JPanel crearPanelDashboard() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 20));
        panelPrincipal.setBackground(Estilo.VAINILLA);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        panelPrincipal.add(Estilo.encabezado(
                "Panel de control",
                "Resumen general del sistema."
        ), BorderLayout.NORTH);

        JPanel tarjetas = new JPanel(new GridLayout(2, 4, 16, 16));
        tarjetas.setOpaque(false);
        tarjetas.add(tarjetaStats("💰", "Ventas hoy",      "$4,860", "▲ 12%", Estilo.MENTA,    true));
        tarjetas.add(tarjetaStats("📝", "Pedidos activos", "18",     "▲ 3",   Estilo.FRESA,    true));
        tarjetas.add(tarjetaStats("📦", "En stock",        "248",    "▼ 5",   Estilo.CARAMELO, false));
        tarjetas.add(tarjetaStats("👥", "Empleados",       "9",      "—",     Estilo.LAVANDA,  true));
        tarjetas.add(tarjetaStats("🧾", "Ticket promedio", "$285",   "▲ 8%",  Estilo.CIELO,    true));
        tarjetas.add(tarjetaStats("⚠",  "Bajo inventario", "5",      "▲ 2",   Estilo.CEREZA,   false));
        tarjetas.add(tarjetaStats("🚚", "Proveedores",     "14",     "—",     Estilo.CARAMELO, true));
        tarjetas.add(tarjetaStats("⭐", "Satisfacción",    "4.8",    "▲ 0.2", Estilo.MENTA,    true));

        panelPrincipal.add(tarjetas, BorderLayout.CENTER);

        JPanel inferior = new JPanel(new GridLayout(1, 2, 16, 16));
        inferior.setOpaque(false);
        inferior.add(graficoBarras());
        inferior.add(progresoMetas());

        panelPrincipal.add(inferior, BorderLayout.SOUTH);

        return panelPrincipal;
    }

    private JPanel tarjetaStats(String icono, String titulo, String valor, String variacion, Color color, boolean positivo) {
        JPanel tarjeta = new JPanel(new BorderLayout(10, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Estilo.sombra(g, getWidth(), getHeight());
            }
        };
        tarjeta.setBackground(Estilo.BLANCO);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDE_SUAVE, 1),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)
        ));

        JPanel circulo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 45));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        circulo.setOpaque(false);
        circulo.setPreferredSize(new Dimension(48, 48));
        circulo.setLayout(new BorderLayout());

        JLabel lblIcono = new JLabel(icono, SwingConstants.CENTER);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        circulo.add(lblIcono, BorderLayout.CENTER);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(Estilo.F_SUBTITULO);
        lblTitulo.setForeground(Estilo.TEXTO_GRIS);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(Estilo.F_NUMERO);
        lblValor.setForeground(Estilo.CHOCOLATE);

        JLabel lblVar = new JLabel(variacion);
        lblVar.setFont(Estilo.F_BOTON);
        lblVar.setForeground(positivo ? Estilo.MENTA : Estilo.CEREZA);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblVar.setAlignmentX(Component.LEFT_ALIGNMENT);
        info.add(lblTitulo);
        info.add(Box.createRigidArea(new Dimension(0, 4)));
        info.add(lblValor);
        info.add(Box.createRigidArea(new Dimension(0, 4)));
        info.add(lblVar);

        tarjeta.add(circulo, BorderLayout.WEST);
        tarjeta.add(info, BorderLayout.CENTER);
        return tarjeta;
    }

    private JPanel graficoBarras() {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(Estilo.BLANCO);
        contenedor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDE_SUAVE, 1),
                BorderFactory.createEmptyBorder(15, 18, 15, 18)
        ));

        JLabel titulo = new JLabel("Ventas últimos 7 días");
        titulo.setFont(Estilo.F_SECCION);
        titulo.setForeground(Estilo.CHOCOLATE);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel grafico = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int[] valores = {60, 90, 45, 120, 80, 150, 110};
                String[] etiquetas = {"L", "M", "M", "J", "V", "S", "D"};
                int ancho = getWidth();
                int alto = getHeight() - 30;
                int n = valores.length;
                int anchoBarra = ancho / n - 12;
                int max = 160;

                for (int i = 0; i < n; i++) {
                    int x = i * (ancho / n) + 10;
                    int altura = (int) ((double) valores[i] / max * (alto - 20));
                    int y = alto - altura;
                    g2.setPaint(new java.awt.GradientPaint(x, y, Estilo.FRESA, x, alto, Estilo.CARAMELO));
                    g2.fillRoundRect(x, y, anchoBarra, altura, 8, 8);
                    g2.setColor(Estilo.TEXTO_GRIS);
                    g2.setFont(Estilo.F_TEXTO);
                    g2.drawString(etiquetas[i], x + anchoBarra / 2 - 4, alto + 18);
                }
                g2.dispose();
            }
        };
        grafico.setOpaque(false);
        grafico.setPreferredSize(new Dimension(0, 160));

        contenedor.add(titulo, BorderLayout.NORTH);
        contenedor.add(grafico, BorderLayout.CENTER);
        return contenedor;
    }

    private JPanel progresoMetas() {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(Estilo.BLANCO);
        contenedor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDE_SUAVE, 1),
                BorderFactory.createEmptyBorder(15, 18, 15, 18)
        ));

        JLabel titulo = new JLabel("Metas del mes");
        titulo.setFont(Estilo.F_SECCION);
        titulo.setForeground(Estilo.CHOCOLATE);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JPanel lista = new JPanel();
        lista.setOpaque(false);
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.add(meta("Ventas totales", 72, Estilo.MENTA));
        lista.add(Box.createRigidArea(new Dimension(0, 12)));
        lista.add(meta("Pedidos entregados", 58, Estilo.FRESA));
        lista.add(Box.createRigidArea(new Dimension(0, 12)));
        lista.add(meta("Clientes nuevos", 45, Estilo.LAVANDA));
        lista.add(Box.createRigidArea(new Dimension(0, 12)));
        lista.add(meta("Productos surtidos", 88, Estilo.CARAMELO));

        contenedor.add(titulo, BorderLayout.NORTH);
        contenedor.add(lista, BorderLayout.CENTER);
        return contenedor;
    }

    private JPanel meta(String nombre, int porcentaje, Color color) {
        JPanel panel = new JPanel(new BorderLayout(10, 4));
        panel.setOpaque(false);

        JLabel lbl = new JLabel(nombre);
        lbl.setFont(Estilo.F_TEXTO);
        lbl.setForeground(Estilo.CHOCOLATE);

        JLabel lblPorc = new JLabel(porcentaje + "%");
        lblPorc.setFont(Estilo.F_BOTON);
        lblPorc.setForeground(color);

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.add(lbl, BorderLayout.WEST);
        cabecera.add(lblPorc, BorderLayout.EAST);

        JProgressBar barra = Estilo.barra(porcentaje, color);

        panel.add(cabecera, BorderLayout.NORTH);
        panel.add(barra, BorderLayout.CENTER);
        return panel;
    }
}