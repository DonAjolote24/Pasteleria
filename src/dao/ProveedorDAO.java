package dao;

import Controlador.ConexionDB;
import modelo.Proveedor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {
    public boolean insertar(String empresa, String horario, String tipoEntrega, int idUsuario) {
        String sql = "INSERT INTO proveedor (empresa, horarioTrabajo, tipoEntrega, idUsuario) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, empresa); pstmt.setString(2, horario); pstmt.setString(3, tipoEntrega); pstmt.setInt(4, idUsuario);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public Object[] obtenerPorIdUsuario(int idUsuario) {
        String sql = "SELECT empresa, horarioTrabajo, tipoEntrega FROM proveedor WHERE idUsuario = ?";
        try (Connection conn = ConexionDB.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return new Object[]{rs.getString("empresa"), rs.getString("horarioTrabajo"), rs.getString("tipoEntrega")};
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    public boolean actualizarPorIdUsuario(int idUsuario, String empresa, String horario, String tipoEntrega) {
        String sql = "UPDATE proveedor SET empresa=?, horarioTrabajo=?, tipoEntrega=? WHERE idUsuario=?";
        try (Connection conn = ConexionDB.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, empresa); pstmt.setString(2, horario); pstmt.setString(3, tipoEntrega); pstmt.setInt(4, idUsuario);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean eliminar(int idProveedor) {
        String sql = "DELETE FROM proveedor WHERE idProveedor = ?";
        try (Connection conn = ConexionDB.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idProveedor);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }
}