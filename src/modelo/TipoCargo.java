package modelo;

public class TipoCargo {
    private int idCargo; // En el diagrama dice idCargo (siendo la PK de tipoCargo)
    private String descripcion;
    private String tipoCargo; // Gerente, Administrador, Empleado
    private double salario;
    private String fechaActualizacion; // DATETIME manejado como String

    public TipoCargo() {
    }

    public TipoCargo(int idCargo, String descripcion, String tipoCargo, double salario, String fechaActualizacion) {
        this.idCargo = idCargo;
        this.descripcion = descripcion;
        this.tipoCargo = tipoCargo;
        this.salario = salario;
        this.fechaActualizacion = fechaActualizacion;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoCargo() {
        return tipoCargo;
    }

    public void setTipoCargo(String tipoCargo) {
        this.tipoCargo = tipoCargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}