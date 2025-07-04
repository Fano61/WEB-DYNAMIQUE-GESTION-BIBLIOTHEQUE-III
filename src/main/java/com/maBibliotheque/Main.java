package com.maBibliotheque;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.maBibliotheque.service.EmpruntService;
import com.maBibliotheque.repository.AdherentRepository;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        EmpruntService service = (EmpruntService) context.getBean("empruntService");
        AdherentRepository adherentRepo = (AdherentRepository) context.getBean("adherentRepository");

        int idAdherent = 1;
        int idExemplaire = 1;

        int duree = adherentRepo.getDureeEmprunt(idAdherent);
        LocalDate dateRetour = LocalDate.now().plusDays(duree);

        String resultat = service.emprunterLivreAvecDate(idAdherent, idExemplaire, dateRetour);
        System.out.println(resultat);
    }
}
