package it.unicam.cs.mpgc.rpg125946.core.model.geometry;


public enum Direzione {

    SU(0, -1),
    GIU(0, 1),
    SINISTRA(-1, 0),
    DESTRA(1, 0);

    private final int dx;
    private final int dy;

    Direzione(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int dx() {
        return dx;
    }

    public int dy() {
        return dy;
    }
}
