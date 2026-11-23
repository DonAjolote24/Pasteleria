package component;

import dao.UsuarioDAO;
import modelo.Usuario;
import Estilo.Estilo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PanelFormularioPersona extends JPanel {

    private JTextField txtNombre, txtApPaterno, txtApMaterno, txtCorreo, txtNumero;
    private JComboBox<String> comboEstado;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private UsuarioDAO dao;
    
    private int idUsuarioSeleccionado = -1; 

    public PanelFormularioPersona() {
        dao = new UsuarioDAO();
        setLayout(new BorderLayout(15, 15));
        setBackground(Estilo.VAINILLA);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(Estilo.encabezado("Gestión de Personas", "Registra, edita y elimina a las personas en el sistema"), BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Estilo.BLANCO);
        panelForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Estilo.BORDE_SUAVE, 1),
                "Datos de la Persona", 0, 0, Estilo.F_SECCION, Estilo.CHOCOLATE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtNombre = Estilo.campo(15);
        panelForm.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        panelForm.add(new JLabel("Apellido Paterno:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtApPaterno = Estilo.campo(15);
        panelForm.add(txtApPaterno, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        panelForm.add(new JLabel("Apellido Materno:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtApMaterno = Estilo.campo(15);
        panelForm.add(txtApMaterno, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        panelForm.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtCorreo = Estilo.campo(15);
        panelForm.add(txtCorreo, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        panelForm.add(new JLabel("No. Domicilio (Pass):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtNumero = Estilo.campo(15);
        panelForm.add(txtNumero, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.3;
        panelForm.add(new JLabel("Estado/Rol:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        comboEstado = Estilo.combo("Activo", "Administrador", "Empleado", "Cliente", "Proveedor");
        panelForm.add(comboEstado, gbc);

        // Panel de Botones (Guardar, Eliminar)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBotones.setOpaque(false);

        JButton btnGuardar = Estilo.primario("Guardar");
        btnGuardar.addActionListener(e -> guardarOActualizar());
        
        JButton btnEliminar = Estilo.peligro("Eliminar");
        btnEliminar.addActionListener(e -> eliminarUsuario());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panelForm.add(panelBotones, gbc);

        // Configuración de la Tabla
        String[] columnas = {"ID", "Nombre", "A. Paterno", "A. Materno", "Correo", "No. Dom.", "Rol"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);
        
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(Estilo.F_SECCION);
        tabla.getTableHeader().setBackground(Estilo.FRESA_CLARO);
        tabla.getTableHeader().setForeground(Estilo.CHOCOLATE);

        // Evento para seleccionar una fila de la tabla
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    idUsuarioSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtApPaterno.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtApMaterno.setText(modeloTabla.getValueAt(fila, 3) != null ? modeloTabla.getValueAt(fila, 3).toString() : "");
                    txtCorreo.setText(modeloTabla.getValueAt(fila, 4) != null ? modeloTabla.getValueAt(fila, 4).toString() : "");
                    txtNumero.setText(modeloTabla.getValueAt(fila, 5) != null ? modeloTabla.getValueAt(fila, 5).toString() : "");
                    comboEstado.setSelectedItem(modeloTabla.getValueAt(fila, 6).toString());
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Estilo.BORDE_SUAVE, 1),
                "Lista de Personas", 0, 0, Estilo.F_SECCION, Estilo.CHOCOLATE));

        JPanel panelCentro = new JPanel(new BorderLayout(15, 0));
        panelCentro.setOpaque(false);
        panelCentro.add(panelForm, BorderLayout.WEST);
        panelCentro.add(scroll, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        cargarTabla();
    }

    private void guardarOActualizar() {
        if (txtNombre.getText().isEmpty() || txtNumero.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre y el número de domicilio son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = new Usuario();
        u.setNombre(txtNombre.getText().trim());
        u.setApPaterno(txtApPaterno.getText().trim());
        u.setApMaterno(txtApMaterno.getText().trim());
        u.setCorreo(txtCorreo.getText().trim());
        u.setNumeroDomicilio(txtNumero.getText().trim());
        u.setEstado(comboEstado.getSelectedItem().toString());

        if (idUsuarioSeleccionado == -1) {
            // INSERTAR (Nuevo)
            if (dao.insertar(u)) {
                JOptionPane.showMessageDialog(this, "Registro guardado con éxito.");
                limpiarFormulario();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar. Revisa la consola.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // ACTUALIZAR (Existente)
            u.setIdUsuario(idUsuarioSeleccionado);
            if (dao.actualizar(u)) {
                JOptionPane.showMessageDialog(this, "Registro actualizado con éxito.");
                limpiarFormulario();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar. Revisa la consola.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarUsuario() {
        if (idUsuarioSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un usuario de la tabla primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.eliminar(idUsuarioSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Registro eliminado con éxito.");
                limpiarFormulario();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar. (Puede que esté en uso en otra tabla).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtApPaterno.setText("");
        txtApMaterno.setText("");
        txtCorreo.setText("");
        txtNumero.setText("");
        comboEstado.setSelectedIndex(0);
        idUsuarioSeleccionado = -1;
        tabla.clearSelection();
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Object[]> datos = dao.listar();
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }
}