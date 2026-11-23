package dao;

import Controlador.ConexionDB;
import modelo.MateriaPrima;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaPrimaDAO {

    // Método para insertar una nueva materia prima
    public boolean insertar(MateriaPrima mp) {
        String sql = "INSERT INTO materiaPrima (nombre, presentacion, marca, contenidoNeto, UnidadMedida, idProveedor) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mp.getNombre());
            pstmt.setString(2, mp.getPresentacion());
            pstmt.setString(3, mp.getMarca());
            pstmt.setDouble(4, mp.getContenidoNeto());
            pstmt.setString(5, mp.getUnidadMedida());
            pstmt.setInt(6, mp.getIdProveedor());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar materia prima: " + e.getMessage());
            return false;
        }
    }

    // Método para listar todas las materias primas (con el nombre de la empresa del proveedor)
    public List<Object[]> listar() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT m.idMPrima, m.nombre, m.presentacion, m.marca, m.contenidoNeto, m.UnidadMedida, p.empresa " +
                     "FROM materiaPrima m " +
                     "INNER JOIN proveedor p ON m.idProveedor = p.idProveedor";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("idMPrima"),
                    rs.getString("nombre"),
                    rs.getString("presentacion"),
                    rs.getString("marca"),
                    rs.getDouble("contenidoNeto"),
                    rs.getString("UnidadMedida"),
                    rs.getString("empresa")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error al listar materia prima: " + e.getMessage());
        }
        return lista;
    }

    // Método para eliminar
    public boolean eliminar(int idMPrima) {
        String sql = "DELETE FROM materiaPrima WHERE idMPrima = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMPrima);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar materia prima: " + e.getMessage());
            return false;
        }
    }
    
    // Obtener los datos de una materia prima por ID
    public Object[] obtenerPorId(int id) {
        String sql = "SELECT nombre, presentacion, marca, contenidoNeto, UnidadMedida, idProveedor FROM materiaPrima WHERE idMPrima = ?";
        try (java.sql.Connection conn = Controlador.ConexionDB.conectar();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                        rs.getString("nombre"),
                        rs.getString("presentacion"),
                        rs.getString("marca"),
                        rs.getDouble("contenidoNeto"),
                        rs.getString("UnidadMedida"),
                        rs.getInt("idProveedor")
                    };
                }
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error al obtener materia prima: " + e.getMessage());
        }
        return null;
    }

    // Actualizar materia prima
    public boolean actualizar(int id, modelo.MateriaPrima mp) {
        String sql = "UPDATE materiaPrima SET nombre=?, presentacion=?, marca=?, contenidoNeto=?, UnidadMedida=?, idProveedor=? WHERE idMPrima=?";
        try (java.sql.Connection conn = Controlador.ConexionDB.conectar();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mp.getNombre());
            pstmt.setString(2, mp.getPresentacion());
            pstmt.setString(3, mp.getMarca());
            pstmt.setDouble(4, mp.getContenidoNeto());
            pstmt.setString(5, mp.getUnidadMedida());
            pstmt.setInt(6, mp.getIdProveedor());
            pstmt.setInt(7, id);
            return pstmt.executeUpdate() > 0;
        } catch (java.sql.SQLException e) {
            System.out.println("Error al actualizar materia prima: " + e.getMessage());
            return false;
        }
    }
}