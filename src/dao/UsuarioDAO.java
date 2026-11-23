package dao;

import Controlador.ConexionDB;
import modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario validarLogin(String nombre, String password) {
        Usuario usuario = null;
        String sql = "SELECT u.idUsuario, u.nombre, u.apPaterno, u.correo, t.TipoCargo " +
                     "FROM usuario u " +
                     "INNER JOIN cargo c ON u.idUsuario = c.idUsuario " +
                     "INNER JOIN TipoCargo t ON c.idTipoCargo = t.idCargo " +
                     "WHERE LOWER(TRIM(u.nombre)) = LOWER(TRIM(?)) AND c.contrasena = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("idUsuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApPaterno(rs.getString("apPaterno"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setRol(rs.getString("TipoCargo"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar usuario: " + e.getMessage());
        }
        return usuario;
    }

    public int insertarYObtenerID(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre, apPaterno, apMaterno, telefono, celular, correo, codPostal, numeroDomicilio, calle, colonia, Localidad, Municipio, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getApPaterno());
            pstmt.setString(3, usuario.getApMaterno());
            pstmt.setString(4, usuario.getTelefono());
            pstmt.setString(5, usuario.getCelular());
            pstmt.setString(6, usuario.getCorreo());
            pstmt.setString(7, usuario.getCodPostal());
            pstmt.setString(8, usuario.getNumeroDomicilio());
            pstmt.setString(9, usuario.getCalle());
            pstmt.setString(10, usuario.getColonia());
            pstmt.setString(11, usuario.getLocalidad());
            pstmt.setString(12, usuario.getMunicipio());
            pstmt.setString(13, usuario.getEstado());
            if (pstmt.executeUpdate() > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
        }
        return -1;
    }

    public List<Object[]> listarConDetalles(String filtroRol) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT u.idUsuario, u.nombre, u.apPaterno, u.apMaterno, u.correo, u.telefono, u.estado, " +
                     "p.empresa, p.horarioTrabajo, p.tipoEntrega, c.Cup, c.rfc, t.Descripcion, t.TipoCargo, t.salario, " +
                     "cl.numeroInterno, cl.referencia FROM usuario u " +
                     "LEFT JOIN proveedor p ON u.idUsuario = p.idUsuario " +
                     "LEFT JOIN cargo c ON u.idUsuario = c.idUsuario " +
                     "LEFT JOIN TipoCargo t ON c.idTipoCargo = t.idCargo " +
                     "LEFT JOIN cliente cl ON u.idUsuario = cl.idUsuario ";
        if (!filtroRol.equals("Todos")) sql += "WHERE u.estado = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (!filtroRol.equals("Todos")) pstmt.setString(1, filtroRol);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{
                        rs.getInt("idUsuario"), rs.getString("nombre"), rs.getString("apPaterno"), rs.getString("apMaterno"),
                        rs.getString("correo"), rs.getString("telefono"), rs.getString("estado"),
                        rs.getString("empresa"), rs.getString("horarioTrabajo"), rs.getString("tipoEntrega"),
                        rs.getString("Cup"), rs.getString("rfc"), rs.getString("Descripcion"), rs.getString("TipoCargo"), rs.getString("salario"),
                        rs.getString("numeroInterno"), rs.getString("referencia")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }

    public Object[] obtenerUsuarioPorId(int id) {
        String sql = "SELECT nombre, apPaterno, apMaterno, correo, telefono, celular, estado, codPostal, calle, numeroDomicilio, colonia, Localidad, Municipio FROM usuario WHERE idUsuario = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                        rs.getString("nombre"), rs.getString("apPaterno"), rs.getString("apMaterno"),
                        rs.getString("correo"), rs.getString("telefono"), rs.getString("celular"), rs.getString("estado"),
                        rs.getString("codPostal"), rs.getString("calle"), rs.getString("numeroDomicilio"),
                        rs.getString("colonia"), rs.getString("Localidad"), rs.getString("Municipio")
                    };
                }
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuario SET nombre=?, apPaterno=?, apMaterno=?, telefono=?, celular=?, correo=?, codPostal=?, numeroDomicilio=?, calle=?, colonia=?, Localidad=?, Municipio=?, estado=? WHERE idUsuario=?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, u.getNombre()); pstmt.setString(2, u.getApPaterno()); pstmt.setString(3, u.getApMaterno());
            pstmt.setString(4, u.getTelefono()); pstmt.setString(5, u.getCelular()); pstmt.setString(6, u.getCorreo());
            pstmt.setString(7, u.getCodPostal()); pstmt.setString(8, u.getNumeroDomicilio()); pstmt.setString(9, u.getCalle());
            pstmt.setString(10, u.getColonia()); pstmt.setString(11, u.getLocalidad()); pstmt.setString(12, u.getMunicipio());
            pstmt.setString(13, u.getEstado()); pstmt.setInt(14, u.getIdUsuario());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean eliminar(int idUsuario) {
        String sql = "DELETE FROM usuario WHERE idUsuario = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }
}