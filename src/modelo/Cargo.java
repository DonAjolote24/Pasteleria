package modelo;

public class Cargo {
    private int idCargoInt; 
    private String cup;
    private String rfc;
    private int idUsuario; 
    private int idTipoCargo; 

    public Cargo() {
    }

    public Cargo(int idCargoInt, String cup, String rfc, int idUsuario, int idTipoCargo) {
        this.idCargoInt = idCargoInt;
        this.cup = cup;
        this.rfc = rfc;
        this.idUsuario = idUsuario;
        this.idTipoCargo = idTipoCargo;
    }

    public int getIdCargoInt() {
        return idCargoInt;
    }

    public void setIdCargoInt(int idCargoInt) {
        this.idCargoInt = idCargoInt;
    }

    public String getCup() {
        return cup;
    }

    public void setCup(String cup) {
        this.cup = cup;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTipoCargo() {
        return idTipoCargo;
    }


    @Override
	public String toString() {
		return "Cargo [idCargoInt=" + idCargoInt + ", cup=" + cup + ", rfc=" + rfc + ", idUsuario=" + idUsuario
				+ ", idTipoCargo=" + idTipoCargo + "]";
	}
    
    
}