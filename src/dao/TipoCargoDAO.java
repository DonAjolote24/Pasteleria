package dao;

import Controlador.ConexionDB;
import modelo.TipoCargo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoCargoDAO {

    public boolean insertar(TipoCargo tipoCargo) {
        String sql = "INSERT INTO tipoCargo (Descripcion, TipoCargo, salario) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tipoCargo.getDescripcion());
            pstmt.setString(2, tipoCargo.getTipoCargo());
            pstmt.setDouble(3, tipoCargo.getSalario());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar tipo de cargo: " + e.getMessage());
            return false;
        }
    }

    public List<TipoCargo> listar() {
        List<TipoCargo> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipoCargo";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new TipoCargo(
                    rs.getInt("idCargo"),
                    rs.getString("Descripcion"),
                    rs.getString("TipoCargo"),
                    rs.getDouble("salario"),
                    rs.getString("fechaActualizacion")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar tipos de cargo: " + e.getMessage());
        }
        return lista;
    }
}