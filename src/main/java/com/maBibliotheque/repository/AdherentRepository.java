package com.maBibliotheque.repository;

import com.maBibliotheque.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdherentRepository {

    public boolean adherentExiste(int idAdherent) {
        String sql = "SELECT COUNT(*) FROM adherent WHERE id_adherent = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, idAdherent);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean estActif(int idAdherent) {
    String sql = "SELECT abonnement_paye FROM adherent WHERE id_adherent = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idAdherent);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getBoolean("abonnement_paye");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public boolean estSanctionne(int idAdherent) {
    String sql = "SELECT sanction_jusqua FROM adherent WHERE id_adherent = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idAdherent);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            java.sql.Date sanctionDate = rs.getDate("sanction_jusqua");
            if (sanctionDate != null) {
                return sanctionDate.after(new java.util.Date());
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public int getQuotaRestant(int idAdherent) {
    // À adapter si le quota est stocké dans une autre table
String sql = "SELECT quotat_auth - (SELECT COUNT(*) FROM emprunt WHERE id_adherent = ? AND date_retour_effective IS NULL) AS restant "
           + "FROM type_adherent ta JOIN adherent a ON a.id_type_adherent = ta.id_type_adherent WHERE a.id_adherent = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, idAdherent);
        ps.setInt(2, idAdherent);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("restant");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}

public boolean quotaDepasse(int idAdherent) {
    return getQuotaRestant(idAdherent) <= 0;
}

public boolean estAutoriseAEmprunter(int idAdherent) {
    String sql = "SELECT ta.quotat_auth FROM type_adherent ta " +
                 "JOIN adherent a ON a.id_type_adherent = ta.id_type_adherent " +
                 "WHERE a.id_adherent = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, idAdherent);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("quotat_auth") > 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public boolean adherentActif(int idAdherent) {
    String sql = "SELECT id_statut FROM adherent WHERE id_adherent = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idAdherent);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int idStatut = rs.getInt("id_statut");
            // Statut actif est 1 selon ta table statut
            return idStatut == 1;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public void appliquerPenalite(int idAdherent, int dureePenaliteEnJours) {
    String sql = "UPDATE adherent SET sanction_jusqua = DATE_ADD(CURRENT_DATE, INTERVAL ? DAY) WHERE id_adherent = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, dureePenaliteEnJours);
        ps.setInt(2, idAdherent);
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public int getDureePenalite(int idAdherent) {
    String sql = "SELECT ta.duree_penalite FROM adherent a JOIN type_adherent ta ON a.id_type_adherent = ta.id_type_adherent WHERE a.id_adherent = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idAdherent);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("duree_penalite");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}

public int getDureeEmprunt(int idAdherent) {
    String sql = "SELECT ta.duree_emprunt_auth FROM adherent a JOIN type_adherent ta ON a.id_type_adherent = ta.id_type_adherent WHERE a.id_adherent = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idAdherent);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt("duree_emprunt_auth");
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}


public List<Map<String, Object>> findAllAdherents() {
    List<Map<String, Object>> list = new ArrayList<>();
    String sql = "SELECT id_adherent AS id, nom, prenom FROM adherent WHERE id_statut = 1";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Map<String, Object> adh = new HashMap<>();
            adh.put("id", rs.getInt("id"));
            adh.put("nom", rs.getString("nom"));
            adh.put("prenom", rs.getString("prenom"));
            list.add(adh);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}

}
