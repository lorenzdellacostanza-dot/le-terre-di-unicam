package it.unicam.cs.mpgc.rpg125946.core.model.stats;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Statistiche mutabili di un personaggio: punti vita/magia, attacco, difesa,
 * livello, esperienza e oro.
 * <p>
 * Responsabilità (Single Responsibility): custodire e far evolvere in modo coerente
 * questi valori (nessun valore può uscire dai propri limiti) e notificare gli osservatori.
 * Il calcolo del danno resta fuori da questa classe: qui si applica solo il risultato,
 * così la logica di combattimento può cambiare senza modificare le statistiche.
 */
public final class Statistiche {

    private final Progressione progressione;
    private final List<OsservatoreStatistiche> osservatori = new ArrayList<>();

    private int livello;
    private int esperienza;
    private int oro;
    private int pvMassimi;
    private int pv;
    private int pmMassimi;
    private int pm;
    private int attacco;
    private int difesa;

    /** Crea statistiche "fresche" al livello 1, con PV e PM al massimo. */
    public Statistiche(int pvMassimi, int pmMassimi, int attacco, int difesa, int oro, Progressione progressione) {
        this(1, 0, oro, pvMassimi, pvMassimi, pmMassimi, pmMassimi, attacco, difesa, progressione);
    }

    private Statistiche(int livello, int esperienza, int oro,
                        int pvMassimi, int pv, int pmMassimi, int pm,
                        int attacco, int difesa, Progressione progressione) {
        this.progressione = Objects.requireNonNull(progressione, "progressione");
        this.livello = livello;
        this.esperienza = esperienza;
        this.oro = oro;
        this.pvMassimi = pvMassimi;
        this.pv = pv;
        this.pmMassimi = pmMassimi;
        this.pm = pm;
        this.attacco = attacco;
        this.difesa = difesa;
    }

    /** Ricostruisce statistiche a partire da uno stato salvato (usato in caricamento). */
    public static Statistiche ripristina(int livello, int esperienza, int oro,
                                         int pvMassimi, int pv, int pmMassimi, int pm,
                                         int attacco, int difesa, Progressione progressione) {
        return new Statistiche(livello, esperienza, oro, pvMassimi, pv, pmMassimi, pm, attacco, difesa, progressione);
    }

    // --- Osservatori -------------------------------------------------------

    public void aggiungiOsservatore(OsservatoreStatistiche osservatore) {
        osservatori.add(Objects.requireNonNull(osservatore));
    }

    public void rimuoviOsservatore(OsservatoreStatistiche osservatore) {
        osservatori.remove(osservatore);
    }

    private void notificaCambiamento() {
        for (OsservatoreStatistiche o : List.copyOf(osservatori)) {
            o.statisticheCambiate(this);
        }
    }

    private void notificaSalitaLivello() {
        for (OsservatoreStatistiche o : List.copyOf(osservatori)) {
            o.salitaLivello(this, livello);
        }
    }

    // --- Punti vita / magia ------------------------------------------------

    public boolean vivo() {
        return pv > 0;
    }

    /** Applica un danno già calcolato. @return i PV effettivamente persi. */
    public int subisciDanno(int quantita) {
        int applicati = Math.min(Math.max(0, quantita), pv);
        pv -= applicati;
        notificaCambiamento();
        return applicati;
    }

    /** Cura il personaggio senza superare il massimo. @return i PV effettivamente recuperati. */
    public int cura(int quantita) {
        int curati = Math.min(Math.max(0, quantita), pvMassimi - pv);
        pv += curati;
        notificaCambiamento();
        return curati;
    }

    /** Consuma punti magia se disponibili. @return {@code true} se l'operazione è riuscita. */
    public boolean consumaPm(int quantita) {
        if (quantita < 0 || pm < quantita) {
            return false;
        }
        pm -= quantita;
        notificaCambiamento();
        return true;
    }

    public void ripristinaPm(int quantita) {
        pm = Math.min(pmMassimi, pm + Math.max(0, quantita));
        notificaCambiamento();
    }

    // --- Oro ---------------------------------------------------------------

    public void aggiungiOro(int quantita) {
        oro += Math.max(0, quantita);
        notificaCambiamento();
    }

    public boolean spendiOro(int quantita) {
        if (quantita < 0 || oro < quantita) {
            return false;
        }
        oro -= quantita;
        notificaCambiamento();
        return true;
    }

    // --- Esperienza e livelli ---------------------------------------------

    /**
     * Aggiunge esperienza e gestisce eventuali passaggi di livello a catena.
     * Ad ogni livello i massimi crescono secondo la {@link Progressione} e PV/PM
     * vengono ripristinati al nuovo massimo.
     */
    public void guadagnaEsperienza(int quantita) {
        esperienza += Math.max(0, quantita);
        boolean salito = false;
        while (esperienza >= progressione.esperienzaProssimoLivello(livello)) {
            esperienza -= progressione.esperienzaProssimoLivello(livello);
            livello++;
            BonusLivello bonus = progressione.bonusPerLivello(livello);
            pvMassimi += bonus.pvMassimi();
            pmMassimi += bonus.pmMassimi();
            attacco += bonus.attacco();
            difesa += bonus.difesa();
            pv = pvMassimi;
            pm = pmMassimi;
            salito = true;
        }
        notificaCambiamento();
        if (salito) {
            notificaSalitaLivello();
        }
    }

    // --- Lettura -----------------------------------------------------------

    public int livello() {
        return livello;
    }

    public int esperienza() {
        return esperienza;
    }

    public int esperienzaProssimoLivello() {
        return progressione.esperienzaProssimoLivello(livello);
    }

    public int oro() {
        return oro;
    }

    public int pvMassimi() {
        return pvMassimi;
    }

    public int pv() {
        return pv;
    }

    public int pmMassimi() {
        return pmMassimi;
    }

    public int pm() {
        return pm;
    }

    public int attacco() {
        return attacco;
    }

    public int difesa() {
        return difesa;
    }
}
