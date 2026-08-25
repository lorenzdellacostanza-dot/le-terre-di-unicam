package it.unicam.cs.mpgc.rpg125946.core.persistence.dto;

public final class DatiStatistiche {

    public int livello;
    public int esperienza;
    public int oro;
    public int pvMassimi;
    public int pv;
    public int pmMassimi;
    public int pm;
    public int attacco;
    public int difesa;

    public DatiStatistiche() {
    }

    public DatiStatistiche(int livello, int esperienza, int oro, int pvMassimi, int pv,
                           int pmMassimi, int pm, int attacco, int difesa) {
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
}
