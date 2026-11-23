package dao;

import Controlador.ConexionDB;
import modelo.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    public boolean insertar(String numeroInterno, String referencia, int idUsuario) {
        String sql = "INSERT INTO cliente (numeroInterno, referencia, idUsuario) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numeroInterno); pstmt.setString(2, referencia); pstmt.setInt(3, idUsuario);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public Object[] obtenerPorIdUsuario(int idUsuario) {
        String sql = "SELECT numeroInterno, referencia FROM cliente WHERE idUsuario = ?";
        try (Connection conn = ConexionDB.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return new Object[]{rs.getString("numeroInterno"), rs.getString("referencia")};
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    public boolean actualizarPorIdUsuario(int idUsuario, String numeroInterno, String referencia) {
        String sql = "UPDATE cliente SET numeroInterno=?, referencia=? WHERE idUsuario=?";
        try (Connection conn = ConexionDB.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numeroInterno); pstmt.setString(2, referencia); pstmt.setInt(3, idUsuario);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean eliminar(int idCliente) {
        String sql = "DELETE FROM cliente WHERE idCliente = ?";
        try (Connection conn = ConexionDB.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCliente);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }
}