package component;

import Estilo.Estilo;
import javax.swing.*;
import java.awt.*;

public class FormularioCargo extends JPanel {
    private JTextField txtCup;
    private JTextField txtRfc;
    private JPasswordField txtContrasena;
    private JComboBox<String> cmbTipoCargo;
    private JTextField txtDescripcion;
    private JTextField txtSalario;

    public FormularioCargo() {
        setLayout(new GridBagLayout());
        setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel titulo = new JLabel("Datos del Cargo y Tipo de Cargo");
        titulo.setFont(Estilo.F_SECCION);
        titulo.setForeground(Estilo.CHOCOLATE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("CUP:"), gbc);
        txtCup = Estilo.campo(15);
        gbc.gridx = 1; add(txtCup, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("RFC:"), gbc);
        txtRfc = Estilo.campo(15);
        gbc.gridx = 1; add(txtRfc, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Contraseña:"), gbc);
        txtContrasena = new JPasswordField(15);
        txtContrasena.setFont(Estilo.F_TEXTO);
        gbc.gridx = 1; add(txtContrasena, gbc);

        JLabel lblSub = new JLabel("Información del Puesto:");
        lblSub.setFont(Estilo.F_TEXTO);
        lblSub.setForeground(Estilo.CHOCOLATE);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(lblSub, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Puesto (Tipo):"), gbc);
        cmbTipoCargo = new JComboBox<>(new String[]{"Gerente", "Administrador", "Empleado"});
        cmbTipoCargo.setFont(Estilo.F_TEXTO);
        gbc.gridx = 1; add(cmbTipoCargo, gbc);

        gbc.gridx = 0; gbc.gridy = 6; add(new JLabel("Descripción:"), gbc);
        txtDescripcion = Estilo.campo(15);
        gbc.gridx = 1; add(txtDescripcion, gbc);

        gbc.gridx = 0; gbc.gridy = 7; add(new JLabel("Salario:"), gbc);
        txtSalario = Estilo.campo(15);
        gbc.gridx = 1; add(txtSalario, gbc);
    }

    public String getCup() { return txtCup.getText().trim(); }
    public String getRfc() { return txtRfc.getText().trim(); }
    public String getContrasena() { return new String(txtContrasena.getPassword()).trim(); }
    public String getTipoCargoSeleccionado() { return cmbTipoCargo.getSelectedItem().toString(); }
    public String getDescripcion() { return txtDescripcion.getText().trim(); }
    public String getSalario() { return txtSalario.getText().trim(); }

    // ESTE ES EL MÉTODO QUE FALTABA PARA LA EDICIÓN
    public void setDatos(String cup, String rfc, String contrasena, String tipoCargo, String descripcion, String salario) {
        txtCup.setText(cup != null ? cup : "");
        txtRfc.setText(rfc != null ? rfc : "");
        txtContrasena.setText(contrasena != null ? contrasena : "");
        if (tipoCargo != null) {
            cmbTipoCargo.setSelectedItem(tipoCargo);
        }
        txtDescripcion.setText(descripcion != null ? descripcion : "");
        txtSalario.setText(salario != null ? salario : "");
    }

    public void limpiar() {
        txtCup.setText("");
        txtRfc.setText("");
        txtContrasena.setText("");
        cmbTipoCargo.setSelectedIndex(0);
        txtDescripcion.setText("");
        txtSalario.setText("");
    }
}