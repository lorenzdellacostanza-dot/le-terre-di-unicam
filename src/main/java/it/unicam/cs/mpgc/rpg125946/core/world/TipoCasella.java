package it.unicam.cs.mpgc.rpg125946.core.world;


public enum TipoCasella {

    ERBA(true),
    SENTIERO(true),
    FIORI(true),
    PONTE(true),
    ACQUA(false),
    ALBERO(false),
    CASA(false),
    STACCIONATA(false);

    private final boolean calpestabile;

    TipoCasella(boolean calpestabile) {
        this.calpestabile = calpestabile;
    }

    public boolean calpestabile() {
        return calpestabile;
    }
}
