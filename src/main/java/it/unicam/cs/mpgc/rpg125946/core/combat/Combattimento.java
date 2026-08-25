package it.unicam.cs.mpgc.rpg125946.core.combat;

import it.unicam.cs.mpgc.rpg125946.core.combat.behavior.MossaNemico;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Giocatore;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Nemico;

import java.util.Objects;
import java.util.Random;


public final class Combattimento {

    private static final double VARIANZA = 0.20;
    private static final double PROBABILITA_FUGA = 0.5;

    private final Giocatore giocatore;
    private final Nemico nemico;
    private final Random casuale;

    private boolean giocatoreInDifesa;
    private boolean nemicoInDifesa;
    private EsitoCombattimento esito = EsitoCombattimento.IN_CORSO;

    public Combattimento(Giocatore giocatore, Nemico nemico) {
        this(giocatore, nemico, new Random());
    }

    /** Costruttore con generatore casuale iniettabile: rende il combattimento riproducibile nei test. */
    public Combattimento(Giocatore giocatore, Nemico nemico, Random casuale) {
        this.giocatore = Objects.requireNonNull(giocatore, "giocatore");
        this.nemico = Objects.requireNonNull(nemico, "nemico");
        this.casuale = Objects.requireNonNull(casuale, "casuale");
    }

    // --- Esecuzione di un round -------------------------------------------

    /**
     * Esegue un round completo: prima l'azione del giocatore, poi (se il duello continua)
     * la reazione del nemico.
     *
     * @return il resoconto del round con i messaggi e l'esito aggiornato
     */
    public EsitoTurno eseguiTurno(AzioneCombattimento azioneGiocatore) {
        Objects.requireNonNull(azioneGiocatore, "azioneGiocatore");
        String messaggioGiocatore = azioneGiocatore.esegui(this);
        if (esito != EsitoCombattimento.IN_CORSO) {
            return new EsitoTurno(messaggioGiocatore, null, esito);
        }
        String messaggioNemico = nemicoAgisce();
        return new EsitoTurno(messaggioGiocatore, messaggioNemico, esito);
    }

    private String nemicoAgisce() {
        MossaNemico mossa = nemico.comportamento().scegliMossa(nemico.statistiche(), giocatore.statistiche());
        if (mossa == MossaNemico.DIFENDI) {
            nemicoInDifesa = true;
            return nemico.nome() + " assume una posizione difensiva.";
        }
        int inflitti = danneggiaGiocatore(nemico.potenzaAttacco());
        return nemico.nome() + " attacca e infligge " + inflitti + " danni.";
    }

    // --- Operazioni richieste dalle azioni --------------------------------

    /** Applica al nemico il danno prodotto da {@code potenzaAttacco}. @return danni inflitti. */
    public int danneggiaNemico(int potenzaAttacco) {
        int danno = calcolaDanno(potenzaAttacco, nemico.potenzaDifesa(), nemicoInDifesa);
        nemicoInDifesa = false;
        int applicati = nemico.statistiche().subisciDanno(danno);
        if (!nemico.statistiche().vivo()) {
            esito = EsitoCombattimento.VITTORIA;
        }
        return applicati;
    }

    /** Applica al giocatore il danno prodotto da {@code potenzaAttacco}. @return danni inflitti. */
    public int danneggiaGiocatore(int potenzaAttacco) {
        int danno = calcolaDanno(potenzaAttacco, giocatore.potenzaDifesa(), giocatoreInDifesa);
        giocatoreInDifesa = false;
        int applicati = giocatore.statistiche().subisciDanno(danno);
        if (!giocatore.statistiche().vivo()) {
            esito = EsitoCombattimento.SCONFITTA;
        }
        return applicati;
    }

    public void giocatoreInDifesa() {
        this.giocatoreInDifesa = true;
    }

    /** Tenta la fuga; in caso di successo conclude il combattimento. @return esito del tentativo. */
    public boolean tentaFuga() {
        boolean fuggito = casuale.nextDouble() < PROBABILITA_FUGA;
        if (fuggito) {
            esito = EsitoCombattimento.FUGA;
        }
        return fuggito;
    }

    private int calcolaDanno(int attacco, int difesa, boolean inDifesa) {
        int base = Math.max(1, attacco - difesa);
        double fattore = 1.0 + (casuale.nextDouble() * 2 - 1) * VARIANZA; // 1 ± 20%
        int danno = (int) Math.round(base * fattore);
        if (inDifesa) {
            danno = Math.max(1, danno / 2);
        }
        return Math.max(1, danno);
    }

    // --- Lettura -----------------------------------------------------------

    public Giocatore giocatore() {
        return giocatore;
    }

    public Nemico nemico() {
        return nemico;
    }

    public EsitoCombattimento esito() {
        return esito;
    }

    public boolean concluso() {
        return esito != EsitoCombattimento.IN_CORSO;
    }
}
