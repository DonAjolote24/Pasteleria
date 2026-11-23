package modelo;


public class Proveedor {
    private int idProveedor;
    private String empresa;
    private String horarioTrabajo; 
    private String tipoEntrega;
    private int idUsuario;
    
    public Proveedor() {
    }

    public Proveedor(int idProveedor, String empresa, String horarioTrabajo, String tipoEntrega, int idUsuario) {
        this.idProveedor = idProveedor;
        this.empresa = empresa;
        this.horarioTrabajo = horarioTrabajo;
        this.tipoEntrega = tipoEntrega;
        this.idUsuario = idUsuario;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getHorarioTrabajo() {
        return horarioTrabajo;
    }

    public void setHorarioTrabajo(String horarioTrabajo) {
        this.horarioTrabajo = horarioTrabajo;
    }

    public String getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(String tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}