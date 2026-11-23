package modelo;

public class MateriaPrima {
    private int idMPrima;
    private String nombre;
    private String presentacion;
    private String marca;
    private double contenidoNeto;
    private String unidadMedida;
    private int idProveedor;

    public MateriaPrima() {}

    // Getters y Setters
    public int getIdMPrima() { return idMPrima; }
    public void setIdMPrima(int idMPrima) { this.idMPrima = idMPrima; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public double getContenidoNeto() { return contenidoNeto; }
    public void setContenidoNeto(double contenidoNeto) { this.contenidoNeto = contenidoNeto; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }
}