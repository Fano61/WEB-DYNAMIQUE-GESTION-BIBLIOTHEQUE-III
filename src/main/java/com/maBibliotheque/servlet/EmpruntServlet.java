package com.maBibliotheque.servlet;

import com.maBibliotheque.service.EmpruntService;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class EmpruntServlet extends HttpServlet {

    private EmpruntService empruntService;

    @Override
    public void init() {
        empruntService = new EmpruntService(
            new com.maBibliotheque.repository.AdherentRepository(),
            new com.maBibliotheque.repository.ExemplaireRepository(),
            new com.maBibliotheque.repository.EmpruntRepository()
        );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Charger la liste des adhérents actifs
        List<Map<String, Object>> adherents = empruntService.getAllAdherents();
        // Charger la liste des exemplaires disponibles
        List<Map<String, Object>> exemplairesDisponibles = empruntService.getExemplairesDisponibles();

        request.setAttribute("adherents", adherents);
        request.setAttribute("exemplairesDisponibles", exemplairesDisponibles);

        request.getRequestDispatcher("/WEB-INF/jsp/emprunt.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idAdherentStr = request.getParameter("idAdherent");
        String idExemplaireStr = request.getParameter("idExemplaire");

        try {
            int idAdherent = Integer.parseInt(idAdherentStr);
            int idExemplaire = Integer.parseInt(idExemplaireStr);

            String message = empruntService.emprunterLivre(idAdherent, idExemplaire);
            request.setAttribute("message", message);
        } catch (NumberFormatException e) {
            request.setAttribute("message", "Entrée invalide.");
        }

        // Après post, recharger les listes pour ne pas perdre les options dans le formulaire
        List<Map<String, Object>> adherents = empruntService.getAllAdherents();
        List<Map<String, Object>> exemplairesDisponibles = empruntService.getExemplairesDisponibles();

        request.setAttribute("adherents", adherents);
        request.setAttribute("exemplairesDisponibles", exemplairesDisponibles);

        request.getRequestDispatcher("/WEB-INF/jsp/emprunt.jsp").forward(request, response);
    }
}
