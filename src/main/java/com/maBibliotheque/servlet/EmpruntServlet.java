package com.maBibliotheque.servlet;

import com.maBibliotheque.service.EmpruntService;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
        List<Map<String, Object>> adherents = empruntService.getAllAdherents();
        List<Map<String, Object>> exemplairesDisponibles = empruntService.getExemplairesDisponibles();

        request.setAttribute("adherents", adherents);
        request.setAttribute("exemplairesDisponibles", exemplairesDisponibles);

        request.getRequestDispatcher("/WEB-INF/jsp/emprunt.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idAdherentStr = request.getParameter("idAdherent");
        String idExemplaireStr = request.getParameter("idExemplaire");
        String dateRetourStr = request.getParameter("dateRetourPrevue");

        String message;
        try {
            int idAdherent = Integer.parseInt(idAdherentStr);
            int idExemplaire = Integer.parseInt(idExemplaireStr);
            LocalDate dateRetourPrevue = LocalDate.parse(dateRetourStr);

            message = empruntService.emprunterLivreAvecDate(idAdherent, idExemplaire, dateRetourPrevue);
        } catch (NumberFormatException | DateTimeParseException e) {
            message = "Erreur : données invalides.";
        }

        request.setAttribute("message", message);

        List<Map<String, Object>> adherents = empruntService.getAllAdherents();
        List<Map<String, Object>> exemplairesDisponibles = empruntService.getExemplairesDisponibles();

        request.setAttribute("adherents", adherents);
        request.setAttribute("exemplairesDisponibles", exemplairesDisponibles);

        request.getRequestDispatcher("/WEB-INF/jsp/emprunt.jsp").forward(request, response);
    }
}
