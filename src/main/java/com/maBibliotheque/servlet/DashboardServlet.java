package com.maBibliotheque.servlet;

import com.maBibliotheque.repository.DashboardRepository;
import com.maBibliotheque.repository.EmpruntRepository;
import com.maBibliotheque.repository.AdherentRepository;
import com.maBibliotheque.repository.ExemplaireRepository;
import com.maBibliotheque.service.DashboardService;
import com.maBibliotheque.service.EmpruntService;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DashboardServlet extends HttpServlet {

    private DashboardService dashboardService;
    private EmpruntService empruntService;

    @Override
    public void init() {
        DashboardRepository dashboardRepo = new DashboardRepository();
        this.dashboardService = new DashboardService(dashboardRepo, null);

        this.empruntService = new EmpruntService(
            new AdherentRepository(),
            new ExemplaireRepository(),
            new EmpruntRepository()
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Statistiques
        req.setAttribute("topLivres", dashboardService.getLivresLesPlusEmpruntes());
        req.setAttribute("topAdherents", dashboardService.getAdherentsLesPlusActifs());
        req.setAttribute("livres", dashboardService.getLivresLesPlusEmpruntes());
        req.setAttribute("adherents", dashboardService.getAdherentsLesPlusActifs());
        req.setAttribute("tauxRetard", dashboardService.getTauxDeRetard());
        req.setAttribute("livresPlusEmpruntes", dashboardService.getLivresPlusEmpruntes());
        req.setAttribute("adherentsActifs", dashboardService.getAdherentsActifs());
        // req.setAttribute("profils", dashboardService.getNombreEmpruntsParProfil()); // décommente si implémenté

        // Historique
        List<Map<String, Object>> historiqueEmprunts = empruntService.getHistoriqueEmprunts();
        List<Map<String, Object>> historiqueRetours = empruntService.getHistoriqueRetours();
        req.setAttribute("historiqueEmprunts", historiqueEmprunts);
        req.setAttribute("historiqueRetours", historiqueRetours);

        // Affichage
        req.getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(req, resp);
    }
}
