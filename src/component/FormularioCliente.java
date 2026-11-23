package component;

import Estilo.Estilo;
import javax.swing.*;
import java.awt.*;

public class FormularioCliente extends JPanel {
    private JTextField txtNumeroInterno;
    private JTextField txtReferencia;

    public FormularioCliente() {
        setLayout(new GridBagLayout());
        setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel titulo = new JLabel("Datos del Cliente");
        titulo.setFont(Estilo.F_SECCION);
        titulo.setForeground(Estilo.CHOCOLATE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Número Interno:"), gbc);
        txtNumeroInterno = Estilo.campo(15);
        gbc.gridx = 1; add(txtNumeroInterno, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Referencia:"), gbc);
        txtReferencia = Estilo.campo(15);
        gbc.gridx = 1; add(txtReferencia, gbc);
    }

    public String getNumeroInterno() { return txtNumeroInterno.getText().trim(); }
    public String getReferencia() { return txtReferencia.getText().trim(); }

    // MÉTODO FALTANTE PARA LA EDICIÓN
    public void setDatos(String numeroInterno, String referencia) {
        txtNumeroInterno.setText(numeroInterno != null ? numeroInterno : "");
        txtReferencia.setText(referencia != null ? referencia : "");
    }

    public void limpiar() {
        txtNumeroInterno.setText("");
        txtReferencia.setText("");
    }
}