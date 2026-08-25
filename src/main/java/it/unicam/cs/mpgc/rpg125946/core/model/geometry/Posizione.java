package it.unicam.cs.mpgc.rpg125946.core.model.geometry;

import java.util.Objects;


public final class Posizione {

    private final int x;
    private final int y;

    public Posizione(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    /**
     * @return una nuova posizione traslata di una cella nella direzione indicata.
     */
    public Posizione spostata(Direzione direzione) {
        Objects.requireNonNull(direzione, "direzione");
        return new Posizione(x + direzione.dx(), y + direzione.dy());
    }

    /** @return distanza di Manhattan fra questa posizione e {@code altra}. */
    public int distanzaManhattan(Posizione altra) {
        Objects.requireNonNull(altra, "altra");
        return Math.abs(x - altra.x) + Math.abs(y - altra.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Posizione altra)) {
            return false;
        }
        return x == altra.x && y == altra.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
