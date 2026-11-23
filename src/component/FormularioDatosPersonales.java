package component;

import Estilo.Estilo;
import javax.swing.*;
import java.awt.*;

public class FormularioDatosPersonales extends JPanel {

    private JTextField txtNombre, txtApPaterno, txtApMaterno;

    public FormularioDatosPersonales() {
        setLayout(new GridBagLayout());
        setBackground(Estilo.BLANCO);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Estilo.BORDE_SUAVE, 1),
                "Datos Personales", 0, 0, Estilo.F_SECCION, Estilo.CHOCOLATE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtNombre = Estilo.campo(15);
        add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        add(new JLabel("Apellido Paterno:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtApPaterno = Estilo.campo(15);
        add(txtApPaterno, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.2;
        add(new JLabel("Apellido Materno:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtApMaterno = Estilo.campo(15);
        add(txtApMaterno, gbc);
    }

    public String getNombre() { return txtNombre.getText().trim(); }
    public String getApPaterno() { return txtApPaterno.getText().trim(); }
    public String getApMaterno() { return txtApMaterno.getText().trim(); }

    public void setDatos(String nombre, String paterno, String materno) {
        txtNombre.setText(nombre);
        txtApPaterno.setText(paterno);
        txtApMaterno.setText(materno != null ? materno : "");
    }

    public void limpiar() {
        txtNombre.setText("");
        txtApPaterno.setText("");
        txtApMaterno.setText("");
    }
    
    public void establecerDatos(String nombre, String apPaterno, String apMaterno) {
        txtNombre.setText(nombre);
        txtApPaterno.setText(apPaterno);
        txtApMaterno.setText(apMaterno);
    }
}