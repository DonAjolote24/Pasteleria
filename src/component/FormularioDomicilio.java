package component;

import Estilo.Estilo;
import javax.swing.*;
import java.awt.*;

public class FormularioDomicilio extends JPanel {

    private JTextField txtCodPostal, txtCalle, txtNumero, txtColonia, txtLocalidad, txtMunicipio;

    public FormularioDomicilio() {
        setLayout(new GridBagLayout());
        setBackground(Estilo.BLANCO);
        // Un borde con un poco de padding interno para que nada choque con el título
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Estilo.BORDE_SUAVE, 1),
                        "Domicilio", 0, 0, Estilo.F_SECCION, Estilo.CHOCOLATE),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Agregamos un margen superior (Insets) al primer elemento para que no choque con el título
        gbc.insets = new Insets(15, 8, 8, 8); 
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        add(new JLabel("Código Postal:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtCodPostal = Estilo.campo(15);
        add(txtCodPostal, gbc);

        // Para los siguientes elementos, el margen superior vuelve a la normalidad
        gbc.insets = new Insets(8, 8, 8, 8); 
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Calle:"), gbc);
        gbc.gridx = 1;
        txtCalle = Estilo.campo(15);
        add(txtCalle, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Número:"), gbc);
        gbc.gridx = 1;
        txtNumero = Estilo.campo(15);
        add(txtNumero, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Colonia:"), gbc);
        gbc.gridx = 1;
        txtColonia = Estilo.campo(15);
        add(txtColonia, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Localidad:"), gbc);
        gbc.gridx = 1;
        txtLocalidad = Estilo.campo(15);
        add(txtLocalidad, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Municipio:"), gbc);
        gbc.gridx = 1;
        txtMunicipio = Estilo.campo(15);
        add(txtMunicipio, gbc);
    }

    public String getCodPostal() { return txtCodPostal.getText().trim(); }
    public String getCalle() { return txtCalle.getText().trim(); }
    public String getNumero() { return txtNumero.getText().trim(); }
    public String getColonia() { return txtColonia.getText().trim(); }
    public String getLocalidad() { return txtLocalidad.getText().trim(); }
    public String getMunicipio() { return txtMunicipio.getText().trim(); }

    public void setDatos(String cp, String calle, String num, String col, String loc, String mun) {
        txtCodPostal.setText(cp != null ? cp : "");
        txtCalle.setText(calle != null ? calle : "");
        txtNumero.setText(num != null ? num : "");
        txtColonia.setText(col != null ? col : "");
        txtLocalidad.setText(loc != null ? loc : "");
        txtMunicipio.setText(mun != null ? mun : "");
    }

    public void limpiar() {
        txtCodPostal.setText("");
        txtCalle.setText("");
        txtNumero.setText("");
        txtColonia.setText("");
        txtLocalidad.setText("");
        txtMunicipio.setText("");
    }
    
    public void establecerDatos(String codPostal, String calle, String numero, String colonia, String localidad, String municipio) {
        txtCodPostal.setText(codPostal);
        txtCalle.setText(calle);
        txtNumero.setText(numero);
        txtColonia.setText(colonia);
        txtLocalidad.setText(localidad);
        txtMunicipio.setText(municipio);
    }
}