package it.unicam.cs.mpgc.rpg125946.ui.controller;

public interface VistaCombattimento {

    void aggiungiAlRegistro(String testo);
    void aggiornaIndicatori();

         void mostraAzioniPrincipali();

    void mostraProseguimento(String etichetta);

    void tornaAlMondo();

    void mostraFineGioco();
}
