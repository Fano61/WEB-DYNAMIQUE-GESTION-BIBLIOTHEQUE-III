package com.maBibliotheque.service;

import com.maBibliotheque.repository.AdherentRepository;
import com.maBibliotheque.repository.ExemplaireRepository;
import com.maBibliotheque.repository.EmpruntRepository;
import com.maBibliotheque.util.DatabaseConnection;
import com.maBibliotheque.util.DateUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class EmpruntService {

    private AdherentRepository adherentRepository;
    private ExemplaireRepository exemplaireRepository;
    private EmpruntRepository empruntRepository;

    public EmpruntService(AdherentRepository adherentRepository,
                          ExemplaireRepository exemplaireRepository,
                          EmpruntRepository empruntRepository) {
        this.adherentRepository = adherentRepository;
        this.exemplaireRepository = exemplaireRepository;
        this.empruntRepository = empruntRepository;
    }

    public List<Map<String, Object>> getAllAdherents() {
        return adherentRepository.findAllAdherents();
    }

    public List<Map<String, Object>> getExemplairesDisponibles() {
        return exemplaireRepository.findExemplairesDisponibles();
    }

    public String emprunterLivre(int idAdherent, int idExemplaire) {
        if (!adherentRepository.adherentExiste(idAdherent)) return "Erreur : adhérent inexistant.";
        if (!adherentRepository.adherentActif(idAdherent)) return "Erreur : adhérent inactif.";
        if (adherentRepository.estSanctionne(idAdherent)) return "Erreur : adhérent sanctionné.";
        if (!exemplaireRepository.exemplaireExiste(idExemplaire)) return "Erreur : exemplaire inexistant.";
        if (!exemplaireRepository.estDisponible(idExemplaire)) return "Erreur : exemplaire non disponible.";
        if (adherentRepository.quotaDepasse(idAdherent)) return "Erreur : quota d'emprunts atteint.";
        if (!adherentRepository.estAutoriseAEmprunter(idAdherent)) return "Erreur : type d’adhérent non autorisé à emprunter.";

        int duree = adherentRepository.getDureeEmprunt(idAdherent);
        LocalDate aujourdHui = LocalDate.now();
        LocalDate dateRetour = DateUtil.ajouterJoursOuvrables(aujourdHui, duree);

        empruntRepository.enregistrerEmprunt(idAdherent, idExemplaire, dateRetour);
        exemplaireRepository.marquerIndisponible(idExemplaire);

        return "Emprunt validé. Retour prévu le : " + dateRetour;
    }

    public String retournerLivre(int idAdherent, int idExemplaire) {
        if (!adherentRepository.adherentExiste(idAdherent)) return "Erreur : adhérent inexistant.";
        if (!exemplaireRepository.exemplaireExiste(idExemplaire)) return "Erreur : exemplaire inexistant.";
        if (!empruntRepository.empruntActifExiste(idAdherent, idExemplaire)) return "Erreur : emprunt inexistant ou déjà retourné.";

        LocalDate dateRetourPrevue = empruntRepository.getDateRetourPrevue(idAdherent, idExemplaire);
        LocalDate dateRetourEffectif = LocalDate.now();

        int idEmprunt = getIdEmpruntActif(idAdherent, idExemplaire);
        if (idEmprunt == -1) return "Erreur : emprunt introuvable.";

        empruntRepository.enregistrerRetour(idAdherent, idExemplaire, dateRetourEffectif);
        empruntRepository.enregistrerHistorique(idEmprunt, 2);
        exemplaireRepository.marquerDisponible(idExemplaire);

        if (dateRetourEffectif.isAfter(dateRetourPrevue)) {
            int dureePenalite = adherentRepository.getDureePenalite(idAdherent);
            adherentRepository.appliquerPenalite(idAdherent, dureePenalite);
            return "Retour enregistré avec retard. Pénalité appliquée pour " + dureePenalite + " jours.";
        } else {
            return "Retour enregistré dans les temps.";
        }
    }

    public int getIdEmpruntActif(int idAdherent, int idExemplaire) {
        String sql = "SELECT id_emprunt FROM emprunt WHERE id_adherent = ? AND id_exemplaire = ? AND date_retour_effective IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAdherent);
            ps.setInt(2, idExemplaire);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id_emprunt");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Map<String, Object>> getExemplairesEmpruntes() {
    return exemplaireRepository.findExemplairesEmpruntes();
}

public List<Map<String, Object>> getHistoriqueEmprunts() {
    return empruntRepository.getHistoriqueEmprunts();
}

public List<Map<String, Object>> getHistoriqueRetours() {
    return empruntRepository.getHistoriqueRetours();
}

}
