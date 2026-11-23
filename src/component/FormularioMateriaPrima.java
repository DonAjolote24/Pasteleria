package component;

import Estilo.Estilo;
import Controlador.ConexionDB;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class FormularioMateriaPrima extends JPanel {

    private JTextField txtNombre, txtMarca, txtContenido;
    private JComboBox<String> cmbPresentacion, cmbUnidad, cmbProveedor;
    
    // Mapa para guardar la relación "Nombre de Empresa -> ID del Proveedor"
    private Map<String, Integer> mapaProveedores;

    public FormularioMateriaPrima() {
        setLayout(new GridBagLayout());
        setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel titulo = new JLabel("Datos de la Materia Prima");
        titulo.setFont(Estilo.F_SECCION);
        titulo.setForeground(Estilo.CHOCOLATE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titulo, gbc);

        gbc.gridwidth = 1;

        // Nombre
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Nombre:"), gbc);
        txtNombre = Estilo.campo(15);
        gbc.gridx = 1; add(txtNombre, gbc);

        // Marca
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Marca:"), gbc);
        txtMarca = Estilo.campo(15);
        gbc.gridx = 1; add(txtMarca, gbc);

        // Contenido Neto
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Contenido Neto:"), gbc);
        txtContenido = Estilo.campo(15);
        gbc.gridx = 1; add(txtContenido, gbc);

        // Presentación
        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Presentación:"), gbc);
        cmbPresentacion = new JComboBox<>(new String[]{"caja", "botella", "bolsa", "cristal", "carton", "papel", "lata"});
        cmbPresentacion.setFont(Estilo.F_TEXTO);
        gbc.gridx = 1; add(cmbPresentacion, gbc);

        // Unidad de medida
        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Unidad de Medida:"), gbc);
        cmbUnidad = new JComboBox<>(new String[]{"Lt", "Kg", "gr", "ml", "Oz"});
        cmbUnidad.setFont(Estilo.F_TEXTO);
        gbc.gridx = 1; add(cmbUnidad, gbc);

        // Proveedor
        gbc.gridx = 0; gbc.gridy = 6; add(new JLabel("Proveedor:"), gbc);
        cmbProveedor = new JComboBox<>();
        cmbProveedor.setFont(Estilo.F_TEXTO);
        gbc.gridx = 1; add(cmbProveedor, gbc);

        // Llenar el ComboBox de Proveedores al iniciar
        cargarProveedores();
    }

    private void cargarProveedores() {
        mapaProveedores = new HashMap<>();
        String sql = "SELECT idProveedor, empresa FROM proveedor";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("idProveedor");
                String empresa = rs.getString("empresa");
                mapaProveedores.put(empresa, id);
                cmbProveedor.addItem(empresa);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar proveedores: " + e.getMessage());
        }
    }

    public String getNombre() { return txtNombre.getText().trim(); }
    public String getMarca() { return txtMarca.getText().trim(); }
    public double getContenido() { 
        try { return Double.parseDouble(txtContenido.getText().trim()); } 
        catch (NumberFormatException e) { return 0.0; } 
    }
    public String getPresentacion() { return cmbPresentacion.getSelectedItem().toString(); }
    public String getUnidad() { return cmbUnidad.getSelectedItem().toString(); }
    
    public int getIdProveedorSeleccionado() {
        String empresa = (String) cmbProveedor.getSelectedItem();
        if (empresa != null) {
            return mapaProveedores.get(empresa);
        }
        return -1;
    }

    public void limpiar() {
        txtNombre.setText("");
        txtMarca.setText("");
        txtContenido.setText("");
        cmbPresentacion.setSelectedIndex(0);
        cmbUnidad.setSelectedIndex(0);
        if (cmbProveedor.getItemCount() > 0) cmbProveedor.setSelectedIndex(0);
    }

    // ==========================================
    // MÉTODO PARA RELLENAR LOS CAMPOS AL EDITAR
    // ==========================================
    public void setDatos(String nombre, String presentacion, String marca, double contenido, String unidad, int idProveedor) {
        txtNombre.setText(nombre != null ? nombre : "");
        txtMarca.setText(marca != null ? marca : "");
        txtContenido.setText(String.valueOf(contenido));
        
        if (presentacion != null) cmbPresentacion.setSelectedItem(presentacion);
        if (unidad != null) cmbUnidad.setSelectedItem(unidad);
        
        // Buscamos el nombre de la empresa en el mapa para seleccionarlo en el ComboBox
        for (Map.Entry<String, Integer> entry : mapaProveedores.entrySet()) {
            if (entry.getValue() == idProveedor) {
                cmbProveedor.setSelectedItem(entry.getKey());
                break;
            }
        }
    }
}