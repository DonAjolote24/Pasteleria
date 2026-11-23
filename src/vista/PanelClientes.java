package vista;

import dao.ClienteDAO;
import modelo.Cliente;
import Estilo.Estilo;
import component.PanelFormularioPersona;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PanelClientes extends JPanel {

    private PanelFormularioPersona panelPersona;
    
    private JTextField txtNumeroInterno, txtReferencia, txtIdUsuario;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private ClienteDAO dao;
    private int idClienteSeleccionado = -1;

    public PanelClientes() {
        dao = new ClienteDAO();
        setLayout(new BorderLayout(10, 10));
        setBackground(Estilo.VAINILLA);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      
        panelPersona = new PanelFormularioPersona();
        
        panelPersona.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); 

       
        JPanel panelInferior = new JPanel(new BorderLayout(15, 0));
        panelInferior.setOpaque(false);
        panelInferior.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Estilo.BORDE_SUAVE, 1),
                "Datos del Cliente", 0, 0, Estilo.F_SECCION, Estilo.CHOCOLATE));

        
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Estilo.BLANCO);
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        panelForm.add(new JLabel("ID Usuario (Persona):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtIdUsuario = Estilo.campo(10);
        panelForm.add(txtIdUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        panelForm.add(new JLabel("No. Interno:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtNumeroInterno = Estilo.campo(10);
        panelForm.add(txtNumeroInterno, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        panelForm.add(new JLabel("Referencia:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtReferencia = Estilo.campo(10);
        panelForm.add(txtReferencia, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBotones.setOpaque(false);
        
        JButton btnGuardar = Estilo.primario("Guardar Cliente");
        btnGuardar.addActionListener(e -> guardarOActualizar());
        
        JButton btnEliminar = Estilo.peligro("Eliminar");
        btnEliminar.addActionListener(e -> eliminarCliente());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panelForm.add(panelBotones, gbc);

        
        String[] columnas = {"ID Cliente", "No. Interno", "Referencia", "Nombre Persona", "Correo"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(Estilo.F_SECCION);
        tabla.getTableHeader().setBackground(Estilo.FRESA_CLARO);
        tabla.getTableHeader().setForeground(Estilo.CHOCOLATE);

        // Evento para seleccionar cliente y cargar en el formulario de abajo
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    idClienteSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                    txtNumeroInterno.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtReferencia.setText(modeloTabla.getValueAt(fila, 2).toString());
                    
                    // Buscamos el ID de usuario (lo agregamos en la columna oculta o lo sacamos del listado)
                    List<Object[]> datos = dao.listarClientes();
                    if(fila < datos.size()){
                        txtIdUsuario.setText(String.valueOf(datos.get(fila)[5]));
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(0, 150));

        panelInferior.add(panelForm, BorderLayout.WEST);
        panelInferior.add(scroll, BorderLayout.CENTER);

        // --- ENSAMBLAR TODO ---
        // Usamos un panel con GridLayout para que la parte de arriba (personas) ocupe más espacio
        JPanel panelContenedor = new JPanel(new BorderLayout());
        panelContenedor.setOpaque(false);
        panelContenedor.add(panelPersona, BorderLayout.CENTER);
        panelContenedor.add(panelInferior, BorderLayout.SOUTH);

        add(panelContenedor, BorderLayout.CENTER);

        cargarTabla();
    }

    private void guardarOActualizar() {
        if (txtIdUsuario.getText().isEmpty() || txtNumeroInterno.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El ID de Usuario y el No. Interno son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Cliente c = new Cliente();
            c.setNumeroInterno(txtNumeroInterno.getText().trim());
            c.setReferencia(txtReferencia.getText().trim());
            c.setIdUsuario(Integer.parseInt(txtIdUsuario.getText().trim()));

            if (idClienteSeleccionado == -1) {
                if (dao.insertar(c)) {
                    JOptionPane.showMessageDialog(this, "Cliente guardado con éxito.");
                    limpiarFormulario();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                c.setIdCliente(idClienteSeleccionado);
                if (dao.actualizar(c)) {
                    JOptionPane.showMessageDialog(this, "Cliente actualizado con éxito.");
                    limpiarFormulario();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID de Usuario debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCliente() {
        if (idClienteSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente de la tabla primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.eliminar(idClienteSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado.");
                limpiarFormulario();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormulario() {
        txtNumeroInterno.setText("");
        txtReferencia.setText("");
        txtIdUsuario.setText("");
        idClienteSeleccionado = -1;
        tabla.clearSelection();
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Object[]> datos = dao.listarClientes();
        for (Object[] fila : datos) {
            // Solo agregamos los primeros 5 elementos a la tabla visual
            modeloTabla.addRow(new Object[]{fila[0], fila[1], fila[2], fila[3], fila[4]});
        }
    }
}