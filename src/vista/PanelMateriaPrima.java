package vista;

import dao.MateriaPrimaDAO;
import modelo.MateriaPrima;
import component.FormularioMateriaPrima;
import Estilo.Estilo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

public class PanelMateriaPrima extends JPanel {

    private FormularioMateriaPrima formulario;
    private MateriaPrimaDAO dao;
    
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtBuscar;
    
    private int idSeleccionado = -1;
    private final String PLACEHOLDER = " 🔍 Buscar por nombre, marca o proveedor...";

    // Variables para el modo edición
    private boolean modoEdicion = false;
    private int idEdicion = -1;

    public PanelMateriaPrima() {
        dao = new MateriaPrimaDAO();
        setLayout(new BorderLayout());
        setBackground(Estilo.VAINILLA);

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(Estilo.VAINILLA);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- FORMULARIO ---
        formulario = new FormularioMateriaPrima();
        formulario.setAlignmentX(Component.LEFT_ALIGNMENT);
        formulario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));
        panelContenido.add(formulario);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- BOTONES ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBotones.setOpaque(false);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnGuardar = crearBotonFuerte("Registrar Materia Prima", Estilo.CHOCOLATE);
        btnGuardar.addActionListener(e -> guardar());

        JButton btnLimpiar = crearBotonFuerte("Limpiar Campos", new Color(150, 120, 90));
        btnLimpiar.addActionListener(e -> limpiar());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelContenido.add(panelBotones);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 30)));

        // --- TABLA Y BUSCADOR ---
        JPanel panelTablaCompleto = new JPanel(new BorderLayout(0, 10));
        panelTablaCompleto.setOpaque(false);
        panelTablaCompleto.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTablaCompleto.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Estilo.BORDE_SUAVE, 1),
                "Inventario de Materias Primas", 0, 0, Estilo.F_SECCION, Estilo.CHOCOLATE));

        JPanel panelSuperiorTabla = new JPanel(new BorderLayout(10, 0));
        panelSuperiorTabla.setOpaque(false);

        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelBuscar.setOpaque(false);
        
        txtBuscar = Estilo.campo(25);
        txtBuscar.setForeground(Color.GRAY);
        txtBuscar.setText(PLACEHOLDER);
        txtBuscar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtBuscar.getText().equals(PLACEHOLDER)) {
                    txtBuscar.setText("");
                    txtBuscar.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtBuscar.getText().isEmpty()) {
                    txtBuscar.setForeground(Color.GRAY);
                    txtBuscar.setText(PLACEHOLDER);
                }
            }
        });
        
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { buscar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { buscar(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { buscar(); }
        });

        panelBuscar.add(txtBuscar);
        panelSuperiorTabla.add(panelBuscar, BorderLayout.WEST);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelAcciones.setOpaque(false);

        JButton btnEditar = crearBotonFuerte("Editar Seleccionado", Estilo.CARAMELO);
        btnEditar.addActionListener(e -> prepararEdicion());

        JButton btnEliminar = crearBotonFuerte("Eliminar Seleccionado", new Color(180, 50, 50));
        btnEliminar.addActionListener(e -> eliminar());

        panelAcciones.add(btnEditar);
        panelAcciones.add(btnEliminar);
        panelSuperiorTabla.add(panelAcciones, BorderLayout.EAST);

        String[] columnas = {"ID", "Nombre", "Presentación", "Marca", "Contenido", "Unidad", "Proveedor"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(Estilo.F_SECCION);
        tabla.getTableHeader().setBackground(Estilo.FRESA_CLARO);
        tabla.getTableHeader().setForeground(Estilo.CHOCOLATE);

        sorter = new TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                int filaModelo = tabla.convertRowIndexToModel(tabla.getSelectedRow());
                idSeleccionado = (int) modeloTabla.getValueAt(filaModelo, 0);
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createLineBorder(Estilo.BORDE_SUAVE, 1));
        scrollTabla.setPreferredSize(new Dimension(0, 250));

        panelTablaCompleto.add(panelSuperiorTabla, BorderLayout.NORTH);
        panelTablaCompleto.add(scrollTabla, BorderLayout.CENTER);
        panelContenido.add(panelTablaCompleto);

        JScrollPane scrollPrincipal = new JScrollPane(panelContenido);
        scrollPrincipal.setBorder(null);
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16);
        scrollPrincipal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPrincipal.getViewport().setBackground(Estilo.VAINILLA);

        add(scrollPrincipal, BorderLayout.CENTER);
        cargarTabla();
    }

    private JButton crearBotonFuerte(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(colorFondo);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 40));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(colorFondo.brighter()); }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(colorFondo); }
        });
        return btn;
    }

    // ==========================================
    // LÓGICA DE NEGOCIO Y EDICIÓN
    // ==========================================

    private void prepararEdicion() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una materia prima de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modoEdicion = true;
        idEdicion = idSeleccionado;

        Object[] datos = dao.obtenerPorId(idEdicion);
        if (datos != null) {
            formulario.setDatos(
                (String) datos[0], 
                (String) datos[1], 
                (String) datos[2], 
                (Double) datos[3], 
                (String) datos[4], 
                (Integer) datos[5]
            );
            JOptionPane.showMessageDialog(this, "Datos cargados. Modifica los campos y presiona 'Actualizar'.", "Modo Edición", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardar() {
        if (formulario.getNombre().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (formulario.getIdProveedorSeleccionado() == -1) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un proveedor válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        MateriaPrima mp = new MateriaPrima();
        mp.setNombre(formulario.getNombre());
        mp.setPresentacion(formulario.getPresentacion());
        mp.setMarca(formulario.getMarca());
        mp.setContenidoNeto(formulario.getContenido());
        mp.setUnidadMedida(formulario.getUnidad());
        mp.setIdProveedor(formulario.getIdProveedorSeleccionado());

        boolean exito;
        if (modoEdicion) {
            exito = dao.actualizar(idEdicion, mp);
            if (exito) JOptionPane.showMessageDialog(this, "¡Materia prima actualizada con éxito!");
        } else {
            exito = dao.insertar(mp);
            if (exito) JOptionPane.showMessageDialog(this, "¡Materia prima registrada con éxito!");
        }

        if (exito) {
            limpiar();
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Hubo un error al guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una materia prima de la tabla primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar esta materia prima?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.eliminar(idSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Registro eliminado con éxito.");
                limpiar();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar. Es posible que esté en uso.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiar() {
        formulario.limpiar();
        idSeleccionado = -1;
        idEdicion = -1;
        modoEdicion = false;
        tabla.clearSelection();
        // Si tienes un botón que cambia de texto, aquí podrías volverlo a poner en "Registrar"
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Object[]> datos = dao.listar();
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }

    private void buscar() {
        String texto = txtBuscar.getText();
        if (texto.equals(PLACEHOLDER) || texto.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto.trim()));
        }
    }
}