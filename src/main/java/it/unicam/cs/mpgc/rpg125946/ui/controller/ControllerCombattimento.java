package it.unicam.cs.mpgc.rpg125946.ui.controller;

import it.unicam.cs.mpgc.rpg125946.core.combat.AzioneAttacco;
import it.unicam.cs.mpgc.rpg125946.core.combat.AzioneCombattimento;
import it.unicam.cs.mpgc.rpg125946.core.combat.AzioneDifesa;
import it.unicam.cs.mpgc.rpg125946.core.combat.AzioneFuga;
import it.unicam.cs.mpgc.rpg125946.core.combat.AzioneUsaOggetto;
import it.unicam.cs.mpgc.rpg125946.core.combat.Combattimento;
import it.unicam.cs.mpgc.rpg125946.core.combat.EsitoCombattimento;
import it.unicam.cs.mpgc.rpg125946.core.combat.EsitoTurno;
import it.unicam.cs.mpgc.rpg125946.core.engine.MotoreGioco;
import it.unicam.cs.mpgc.rpg125946.core.engine.RicompensaVittoria;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Giocatore;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Nemico;
import it.unicam.cs.mpgc.rpg125946.core.model.item.Consumabile;

import java.util.List;
import java.util.Objects;

/**
 * Controllore del combattimento (ruolo <em>Controller</em> di MVC).
 * <p>
 * Ogni pulsante della vista corrisponde qui a un metodo che costruisce il comando
 * ({@link AzioneCombattimento}) e lo fa eseguire al modello; poi riporta alla vista i messaggi
 * prodotti e la conseguenza dell'esito. La vista non conosce le regole, il modello non conosce
 * i pulsanti.
 */
public final class ControllerCombattimento {

    private final Combattimento combattimento;
    private final MotoreGioco motore;
    private final Nemico nemico;
    private final VistaCombattimento vista;

    private EsitoCombattimento esitoFinale = EsitoCombattimento.IN_CORSO;

    public ControllerCombattimento(MotoreGioco motore, Nemico nemico, VistaCombattimento vista) {
        this.motore = Objects.requireNonNull(motore, "motore");
        this.nemico = Objects.requireNonNull(nemico, "nemico");
        this.vista = Objects.requireNonNull(vista, "vista");
        this.combattimento = new Combattimento(motore.stato().giocatore(), nemico);
    }

    /** Prepara la scena iniziale del duello. */
    public void avvia() {
        vista.aggiungiAlRegistro("Un " + nemico.nome() + " selvatico ti sbarra la strada!");
        vista.aggiornaIndicatori();
        vista.mostraAzioniPrincipali();
    }

    // --- Intenzioni dell'utente -------------------------------------------

    public void attacca() {
        eseguiTurno(new AzioneAttacco());
    }

    public void difendi() {
        eseguiTurno(new AzioneDifesa());
    }

    public void usaOggetto(Consumabile oggetto) {
        eseguiTurno(new AzioneUsaOggetto(oggetto));
    }

    public void fuggi() {
        eseguiTurno(new AzioneFuga());
    }

    /** Premuto il pulsante finale: decide dove portare l'utente in base all'esito. */
    public void prosegui() {
        if (esitoFinale == EsitoCombattimento.SCONFITTA) {
            vista.mostraFineGioco();
        } else {
            vista.tornaAlMondo();
        }
    }

    // --- Coordinamento -----------------------------------------------------

    private void eseguiTurno(AzioneCombattimento azione) {
        EsitoTurno turno = combattimento.eseguiTurno(azione);
        vista.aggiungiAlRegistro(turno.messaggioGiocatore());
        if (turno.messaggioNemico() != null) {
            vista.aggiungiAlRegistro(turno.messaggioNemico());
        }
        vista.aggiornaIndicatori();
        gestisciEsito(turno.esito());
    }

    private void gestisciEsito(EsitoCombattimento esito) {
        esitoFinale = esito;
        switch (esito) {
            case IN_CORSO -> vista.mostraAzioniPrincipali();
            case VITTORIA -> concludiConVittoria();
            case SCONFITTA -> vista.mostraProseguimento("Continua");
            case FUGA -> {
                vista.aggiungiAlRegistro("Sei riuscito a fuggire.");
                vista.mostraProseguimento("Continua");
            }
        }
    }

    private void concludiConVittoria() {
        RicompensaVittoria ricompensa = motore.applicaVittoria(nemico);
        vista.aggiungiAlRegistro("Hai guadagnato " + ricompensa.esperienza()
                + " PE e " + ricompensa.oro() + " oro.");
        ricompensa.bottino().ifPresent(oggetto ->
                vista.aggiungiAlRegistro("Hai ottenuto: " + oggetto.nome() + "."));
        if (ricompensa.salitoLivello()) {
            vista.aggiungiAlRegistro("Sei salito al livello " + ricompensa.nuovoLivello() + "!");
        }
        vista.aggiornaIndicatori();
        vista.mostraProseguimento("Continua");
    }

    // --- Lettura per la vista ---------------------------------------------

    public Giocatore giocatore() {
        return motore.stato().giocatore();
    }

    public Nemico nemico() {
        return nemico;
    }

    /** @return i consumabili attualmente posseduti dal giocatore. */
    public List<Consumabile> consumabili() {
        return giocatore().inventario().diTipo(Consumabile.class);
    }
}
