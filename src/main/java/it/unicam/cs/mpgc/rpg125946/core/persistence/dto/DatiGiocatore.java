package it.unicam.cs.mpgc.rpg125946.core.persistence.dto;

import java.util.List;

public final class DatiGiocatore {

    public String nome;
    public DatiPosizione posizione;
    public DatiStatistiche statistiche;
    public List<DatiOggetto> inventario;
    public String nomeArmaEquipaggiata;

    public DatiGiocatore() {
    }

    public DatiGiocatore(String nome, DatiPosizione posizione, DatiStatistiche statistiche,
                         List<DatiOggetto> inventario, String nomeArmaEquipaggiata) {
        this.nome = nome;
        this.posizione = posizione;
        this.statistiche = statistiche;
        this.inventario = inventario;
        this.nomeArmaEquipaggiata = nomeArmaEquipaggiata;
    }
}
