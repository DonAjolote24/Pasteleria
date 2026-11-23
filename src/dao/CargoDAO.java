package dao;

import Controlador.ConexionDB;
import modelo.Cargo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {
    public boolean insertarCompleto(String cup, String rfc, int idUsuario, String contrasena, String tipoCargo, String descripcion, double salario) {
        String sqlTipoCargo = "INSERT INTO TipoCargo (Descripcion, TipoCargo, salario) VALUES (?, ?, ?)";
        String sqlCargo = "INSERT INTO cargo (Cup, rfc, idUsuario, contrasena, idTipoCargo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar()) {
            conn.setAutoCommit(false);
            int idTipoCargoGenerado = -1;
            try (PreparedStatement psTipo = conn.prepareStatement(sqlTipoCargo, Statement.RETURN_GENERATED_KEYS)) {
                psTipo.setString(1, descripcion); psTipo.setString(2, tipoCargo); psTipo.setDouble(3, salario);
                if (psTipo.executeUpdate() > 0) {
                    try (ResultSet rs = psTipo.getGeneratedKeys()) {
                        if (rs.next()) idTipoCargoGenerado = rs.getInt(1);
                    }
                }
            }
            if (idTipoCargoGenerado == -1) { conn.rollback(); return false; }
            try (PreparedStatement psCargo = conn.prepareStatement(sqlCargo)) {
                psCargo.setString(1, cup); psCargo.setString(2, rfc); psCargo.setInt(3, idUsuario);
                psCargo.setString(4, contrasena); psCargo.setInt(5, idTipoCargoGenerado);
                psCargo.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) { return false; }
    }

    public Object[] obtenerPorIdUsuario(int idUsuario) {
        String sql = "SELECT c.Cup, c.rfc, c.contrasena, t.TipoCargo, t.Descripcion, t.salario FROM cargo c INNER JOIN TipoCargo t ON c.idTipoCargo = t.idCargo WHERE c.idUsuario = ?";
        try (Connection conn = ConexionDB.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return new Object[]{
                    rs.getString("Cup"), rs.getString("rfc"), rs.getString("contrasena"), 
                    rs.getString("TipoCargo"), rs.getString("Descripcion"), String.valueOf(rs.getDouble("salario"))
                };
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    public boolean actualizarCompleto(int idUsuario, String cup, String rfc, String contrasena, String tipoCargo, String descripcion, double salario) {
        String sqlUpdateCargo = "UPDATE cargo SET Cup=?, rfc=?, contrasena=? WHERE idUsuario=?";
        String sqlUpdateTipo = "UPDATE TipoCargo t INNER JOIN cargo c ON t.idCargo = c.idTipoCargo SET t.TipoCargo=?, t.Descripcion=?, t.salario=? WHERE c.idUsuario=?";
        try (Connection conn = ConexionDB.conectar()) {
            try (PreparedStatement ps1 = conn.prepareStatement(sqlUpdateCargo)) {
                ps1.setString(1, cup); ps1.setString(2, rfc); ps1.setString(3, contrasena); ps1.setInt(4, idUsuario);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sqlUpdateTipo)) {
                ps2.setString(1, tipoCargo); ps2.setString(2, descripcion); ps2.setDouble(3, salario); ps2.setInt(4, idUsuario);
                ps2.executeUpdate();
            }
            return true;
        } catch (SQLException e) { return false; }
    }

    public boolean eliminar(int idCargo) {
        String sql = "DELETE FROM cargo WHERE idCargo_int = ?";
        try (Connection conn = ConexionDB.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCargo);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }
}