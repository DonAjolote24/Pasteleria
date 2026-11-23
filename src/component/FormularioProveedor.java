package component;

import Estilo.Estilo;
import javax.swing.*;
import java.awt.*;

public class FormularioProveedor extends JPanel {
    private JTextField txtEmpresa;
    private JTextField txtHorario;
    private JComboBox<String> cmbTipoEntrega;

    public FormularioProveedor() {
        setLayout(new GridBagLayout());
        setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel titulo = new JLabel("Datos del Proveedor");
        titulo.setFont(Estilo.F_SECCION);
        titulo.setForeground(Estilo.CHOCOLATE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Empresa:"), gbc);
        txtEmpresa = Estilo.campo(15);
        gbc.gridx = 1; add(txtEmpresa, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Horario (HH:MM:SS):"), gbc);
        txtHorario = Estilo.campo(15);
        gbc.gridx = 1; add(txtHorario, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Tipo Entrega:"), gbc);
        cmbTipoEntrega = new JComboBox<>(new String[]{"Domicilio", "Recoger", "Mixta"});
        cmbTipoEntrega.setFont(Estilo.F_TEXTO);
        gbc.gridx = 1; add(cmbTipoEntrega, gbc);
    }

    public String getEmpresa() { return txtEmpresa.getText().trim(); }
    public String getHorario() { return txtHorario.getText().trim(); }
    public String getTipoEntrega() { return cmbTipoEntrega.getSelectedItem().toString(); }
    
    // MÉTODO FALTANTE PARA LA EDICIÓN
    public void setDatos(String empresa, String horario, String tipoEntrega) {
        txtEmpresa.setText(empresa != null ? empresa : "");
        txtHorario.setText(horario != null ? horario : "");
        if (tipoEntrega != null) {
            cmbTipoEntrega.setSelectedItem(tipoEntrega);
        }
    }
    
    public void limpiar() {
        txtEmpresa.setText("");
        txtHorario.setText("");
        cmbTipoEntrega.setSelectedIndex(0);
    }
}