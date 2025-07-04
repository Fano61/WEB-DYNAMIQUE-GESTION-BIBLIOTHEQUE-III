package com.maBibliotheque.servlet;

import com.maBibliotheque.service.EmpruntService;
import com.maBibliotheque.repository.AdherentRepository;
import com.maBibliotheque.repository.ExemplaireRepository;
import com.maBibliotheque.repository.EmpruntRepository;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RetourServlet extends HttpServlet {

    private EmpruntService empruntService;

    @Override
    public void init() {
        empruntService = new EmpruntService(
            new AdherentRepository(),
            new ExemplaireRepository(),
            new EmpruntRepository()
        );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Charger les listes nécessaires au formulaire
        List<Map<String, Object>> adherents = empruntService.getAllAdherents();
        List<Map<String, Object>> exemplaires = empruntService.getExemplairesEmpruntes();

        request.setAttribute("adherents", adherents);
        request.setAttribute("exemplairesEmpruntes", exemplaires);

        request.getRequestDispatcher("/WEB-INF/jsp/retour.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idAdherentStr = request.getParameter("idAdherent");
        String idExemplaireStr = request.getParameter("idExemplaire");
        String message;

        try {
            int idAdherent = Integer.parseInt(idAdherentStr);
            int idExemplaire = Integer.parseInt(idExemplaireStr);

            message = empruntService.retournerLivre(idAdherent, idExemplaire);
        } catch (NumberFormatException e) {
            message = "Entrée invalide.";
        }

        request.setAttribute("message", message);

        // Recharge les listes pour réafficher correctement le formulaire
        List<Map<String, Object>> adherents = empruntService.getAllAdherents();
        List<Map<String, Object>> exemplaires = empruntService.getExemplairesEmpruntes();

        request.setAttribute("adherents", adherents);
        request.setAttribute("exemplairesEmpruntes", exemplaires);

        request.getRequestDispatcher("/WEB-INF/jsp/retour.jsp").forward(request, response);
    }
}
