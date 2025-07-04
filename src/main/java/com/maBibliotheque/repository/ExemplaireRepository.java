package com.maBibliotheque.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maBibliotheque.util.DatabaseConnection;

public class ExemplaireRepository {
    // Code pour gérer les exemplaires en base

    public boolean exemplaireExiste(int idExemplaire) {
    String sql = "SELECT COUNT(*) FROM exemplaire WHERE id_exemplaire = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, idExemplaire);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public boolean estDisponible(int idExemplaire) {
    String sql = "SELECT disponible FROM exemplaire WHERE id_exemplaire = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idExemplaire);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getBoolean("disponible");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public void marquerIndisponible(int idExemplaire) {
    String sql = "UPDATE exemplaire SET disponible = FALSE WHERE id_exemplaire = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idExemplaire);
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public void marquerDisponible(int idExemplaire) {
    String sql = "UPDATE exemplaire SET disponible = TRUE WHERE id_exemplaire = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idExemplaire);
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public List<Map<String, Object>> findExemplairesDisponibles() {
    List<Map<String, Object>> list = new ArrayList<>();
    String sql = """
        SELECT e.id_exemplaire AS id, l.titre
        FROM exemplaire e
        JOIN livre l ON e.id_livre = l.id_livre
        WHERE e.disponible = TRUE
    """;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Map<String, Object> ex = new HashMap<>();
            ex.put("id", rs.getInt("id"));
            ex.put("titre", rs.getString("titre"));
            list.add(ex);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}

public List<Map<String, Object>> findExemplairesEmpruntes() {
    List<Map<String, Object>> list = new ArrayList<>();
    String sql = """
        SELECT e.id_exemplaire AS id, l.titre
        FROM exemplaire e
        JOIN livre l ON e.id_livre = l.id_livre
        WHERE e.disponible = FALSE
    """;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Map<String, Object> ex = new HashMap<>();
            ex.put("id", rs.getInt("id"));
            ex.put("titre", rs.getString("titre"));
            list.add(ex);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}

}
