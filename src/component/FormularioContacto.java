package component;

import Estilo.Estilo;
import javax.swing.*;
import java.awt.*;

public class FormularioContacto extends JPanel {

    private JTextField txtCorreo, txtTelefono, txtCelular;
    private JComboBox<String> comboEstado;

    public FormularioContacto() {
        setLayout(new GridBagLayout());
        setBackground(Estilo.BLANCO);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Estilo.BORDE_SUAVE, 1),
                "Contacto y Rol", 0, 0, Estilo.F_SECCION, Estilo.CHOCOLATE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- COLUMNA IZQUIERDA ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.3;
        txtTelefono = Estilo.campo(10);
        add(txtTelefono, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Celular:"), gbc);
        gbc.gridx = 1;
        txtCelular = Estilo.campo(10);
        add(txtCelular, gbc);

        // --- COLUMNA DERECHA ---
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.2;
        add(new JLabel("Correo:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.3;
        txtCorreo = Estilo.campo(10);
        add(txtCorreo, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        add(new JLabel("Rol:"), gbc);
        gbc.gridx = 3;
        comboEstado = Estilo.combo("Activo", "Administrador", "Empleado", "Cliente", "Proveedor");
        add(comboEstado, gbc);
    }

    public String getTelefono() { return txtTelefono.getText().trim(); }
    public String getCelular() { return txtCelular.getText().trim(); }
    public String getCorreo() { return txtCorreo.getText().trim(); }
    public String getEstado() { return comboEstado.getSelectedItem().toString(); }

    // ESTE ES EL MÉTODO QUE USA TU PANEL USUARIOS PARA RELLENAR LOS DATOS AL EDITAR
    public void setDatos(String tel, String cel, String correo, String estado) {
        txtTelefono.setText(tel != null ? tel : "");
        txtCelular.setText(cel != null ? cel : "");
        txtCorreo.setText(correo != null ? correo : "");
        if (estado != null) {
            comboEstado.setSelectedItem(estado);
        }
    }

    public void limpiar() {
        txtTelefono.setText("");
        txtCelular.setText("");
        txtCorreo.setText("");
        comboEstado.setSelectedIndex(0);
    }
} 