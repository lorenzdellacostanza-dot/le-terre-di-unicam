package it.unicam.cs.mpgc.rpg125946.ui.render;

import it.unicam.cs.mpgc.rpg125946.core.model.entity.Entita;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Giocatore;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Nemico;
import it.unicam.cs.mpgc.rpg125946.core.model.entity.Png;
import it.unicam.cs.mpgc.rpg125946.core.model.geometry.Posizione;
import it.unicam.cs.mpgc.rpg125946.core.world.Mappa;
import it.unicam.cs.mpgc.rpg125946.core.world.TipoCasella;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Disegna la porzione visibile della mappa, le entità e il giocatore su un {@code Canvas}.
 * <p>
 * È l'unico punto in cui il modello viene tradotto in grafica: usa soltanto forme vettoriali
 * (nessuna risorsa immagine esterna) così il progetto resta autosufficiente. La telecamera segue
 * il giocatore e viene "agganciata" ai bordi della mappa perché non si veda mai il vuoto oltre i
 * confini. Il modello non sa nulla di questo disegnatore: la dipendenza va solo dalla grafica verso
 * il {@code core}, mai il contrario.
 */
public final class DisegnatoreMondo {

    /**
     * @param g        contesto grafico su cui disegnare
     * @param mappa    mappa da rappresentare
     * @param giocatore giocatore (centro della telecamera)
     * @param colonne  numero di celle visibili in orizzontale
     * @param righe    numero di celle visibili in verticale
     * @param tile     lato in pixel di una cella
     */
    public void disegna(GraphicsContext g, Mappa mappa, Giocatore giocatore, int colonne, int righe, int tile) {
        int origineX = clamp(giocatore.posizione().x() - colonne / 2, 0, Math.max(0, mappa.larghezza() - colonne));
        int origineY = clamp(giocatore.posizione().y() - righe / 2, 0, Math.max(0, mappa.altezza() - righe));

        g.setFill(Color.web("#0e1020"));
        g.fillRect(0, 0, (double) colonne * tile, (double) righe * tile);

        for (int ry = 0; ry < righe; ry++) {
            for (int rx = 0; rx < colonne; rx++) {
                Posizione pos = new Posizione(origineX + rx, origineY + ry);
                if (!mappa.dentro(pos)) {
                    continue;
                }
                disegnaTerreno(g, mappa.casellaIn(pos), rx * tile, ry * tile, tile);
            }
        }

        for (Entita entita : mappa.entita()) {
            int ex = entita.posizione().x() - origineX;
            int ey = entita.posizione().y() - origineY;
            if (ex < 0 || ey < 0 || ex >= colonne || ey >= righe) {
                continue;
            }
            disegnaEntita(g, entita, ex * tile, ey * tile, tile);
        }

        int px = (giocatore.posizione().x() - origineX) * tile;
        int py = (giocatore.posizione().y() - origineY) * tile;
        disegnaGiocatore(g, px, py, tile);
    }

    // --- Terreni -----------------------------------------------------------

    private void disegnaTerreno(GraphicsContext g, TipoCasella tipo, int x, int y, int t) {
        // Base d'erba sotto ogni cella, per evitare bordi "vuoti".
        g.setFill(Color.web("#5aa457"));
        g.fillRect(x, y, t, t);

        switch (tipo) {
            case ERBA -> ciuffiErba(g, x, y, t);
            case SENTIERO -> {
                g.setFill(Color.web("#d8b98a"));
                g.fillRect(x, y, t, t);
            }
            case FIORI -> {
                ciuffiErba(g, x, y, t);
                g.setFill(Color.web("#ff6f9c"));
                g.fillOval(x + t * 0.25, y + t * 0.3, t * 0.16, t * 0.16);
                g.setFill(Color.web("#ffe14d"));
                g.fillOval(x + t * 0.6, y + t * 0.55, t * 0.16, t * 0.16);
            }
            case PONTE -> {
                g.setFill(Color.web("#8a5a2b"));
                g.fillRect(x, y, t, t);
                g.setStroke(Color.web("#5e3d1c"));
                g.setLineWidth(1);
                for (int i = 1; i < 4; i++) {
                    double yy = y + i * (t / 4.0);
                    g.strokeLine(x, yy, x + t, yy);
                }
            }
            case ACQUA -> {
                g.setFill(Color.web("#4f9fe0"));
                g.fillRect(x, y, t, t);
                g.setStroke(Color.web("#7fc0f0"));
                g.setLineWidth(1.5);
                g.strokeArc(x + t * 0.15, y + t * 0.3, t * 0.3, t * 0.2, 0, 180, javafx.scene.shape.ArcType.OPEN);
                g.strokeArc(x + t * 0.55, y + t * 0.6, t * 0.3, t * 0.2, 0, 180, javafx.scene.shape.ArcType.OPEN);
            }
            case ALBERO -> {
                ciuffiErba(g, x, y, t);
                g.setFill(Color.web("#6b4423"));
                g.fillRect(x + t * 0.44, y + t * 0.55, t * 0.12, t * 0.35);
                g.setFill(Color.web("#2f7d32"));
                g.fillOval(x + t * 0.15, y + t * 0.1, t * 0.7, t * 0.6);
                g.setFill(Color.web("#3c9a40"));
                g.fillOval(x + t * 0.25, y + t * 0.18, t * 0.4, t * 0.35);
            }
            case CASA -> {
                g.setFill(Color.web("#caa06a"));
                g.fillRect(x + t * 0.1, y + t * 0.4, t * 0.8, t * 0.55);
                g.setFill(Color.web("#8a3324"));
                g.fillPolygon(
                        new double[]{x + t * 0.05, x + t * 0.5, x + t * 0.95},
                        new double[]{y + t * 0.42, y + t * 0.08, y + t * 0.42}, 3);
                g.setFill(Color.web("#5e3d1c"));
                g.fillRect(x + t * 0.42, y + t * 0.62, t * 0.16, t * 0.33);
            }
            case STACCIONATA -> {
                ciuffiErba(g, x, y, t);
                g.setFill(Color.web("#b98a4b"));
                g.fillRect(x + t * 0.15, y + t * 0.2, t * 0.12, t * 0.7);
                g.fillRect(x + t * 0.7, y + t * 0.2, t * 0.12, t * 0.7);
                g.fillRect(x + t * 0.05, y + t * 0.38, t * 0.9, t * 0.12);
                g.fillRect(x + t * 0.05, y + t * 0.62, t * 0.9, t * 0.12);
            }
        }
    }

    private void ciuffiErba(GraphicsContext g, int x, int y, int t) {
        g.setFill(Color.web("#4f9a4c"));
        g.fillRect(x + t * 0.2, y + t * 0.7, t * 0.08, t * 0.14);
        g.fillRect(x + t * 0.65, y + t * 0.25, t * 0.08, t * 0.14);
    }

    // --- Entità ------------------------------------------------------------

    private void disegnaEntita(GraphicsContext g, Entita entita, int x, int y, int t) {
        if (entita instanceof Nemico nemico) {
            if ("Goblin".equals(nemico.nome())) {
                disegnaGoblin(g, x, y, t);
            } else {
                disegnaSlime(g, x, y, t);
            }
        } else if (entita instanceof Png) {
            disegnaAnziano(g, x, y, t);
        }
    }

    private void disegnaSlime(GraphicsContext g, int x, int y, int t) {
        g.setFill(Color.web("#43d17a"));
        g.fillArc(x + t * 0.15, y + t * 0.3, t * 0.7, t * 0.7, 0, 180, javafx.scene.shape.ArcType.ROUND);
        g.fillRect(x + t * 0.15, y + t * 0.62, t * 0.7, t * 0.12);
        g.setFill(Color.web("#0e1020"));
        g.fillOval(x + t * 0.34, y + t * 0.5, t * 0.08, t * 0.08);
        g.fillOval(x + t * 0.58, y + t * 0.5, t * 0.08, t * 0.08);
    }

    private void disegnaGoblin(GraphicsContext g, int x, int y, int t) {
        g.setFill(Color.web("#7f9b3d"));
        g.fillOval(x + t * 0.28, y + t * 0.2, t * 0.44, t * 0.4);
        g.fillRect(x + t * 0.32, y + t * 0.52, t * 0.36, t * 0.34);
        g.setFill(Color.web("#a7c356"));
        g.fillPolygon(new double[]{x + t * 0.28, x + t * 0.2, x + t * 0.34},
                new double[]{y + t * 0.3, y + t * 0.2, y + t * 0.24}, 3);
        g.fillPolygon(new double[]{x + t * 0.72, x + t * 0.8, x + t * 0.66},
                new double[]{y + t * 0.3, y + t * 0.2, y + t * 0.24}, 3);
        g.setFill(Color.web("#0e1020"));
        g.fillOval(x + t * 0.38, y + t * 0.33, t * 0.07, t * 0.07);
        g.fillOval(x + t * 0.55, y + t * 0.33, t * 0.07, t * 0.07);
    }

    private void disegnaAnziano(GraphicsContext g, int x, int y, int t) {
        g.setFill(Color.web("#3f51b5"));
        g.fillRect(x + t * 0.3, y + t * 0.42, t * 0.4, t * 0.5);
        g.setFill(Color.web("#f0c8a0"));
        g.fillOval(x + t * 0.34, y + t * 0.2, t * 0.32, t * 0.32);
        g.setFill(Color.web("#e8ebf5"));
        g.fillOval(x + t * 0.36, y + t * 0.44, t * 0.28, t * 0.2);
    }

    private void disegnaGiocatore(GraphicsContext g, int x, int y, int t) {
        g.setStroke(Color.web("#0e1020"));
        g.setLineWidth(2);
        g.setFill(Color.web("#e53935"));
        g.fillRect(x + t * 0.3, y + t * 0.42, t * 0.4, t * 0.46);
        g.strokeRect(x + t * 0.3, y + t * 0.42, t * 0.4, t * 0.46);
        g.setFill(Color.web("#f0c8a0"));
        g.fillOval(x + t * 0.33, y + t * 0.16, t * 0.34, t * 0.34);
        g.strokeOval(x + t * 0.33, y + t * 0.16, t * 0.34, t * 0.34);
        g.setFill(Color.web("#0e1020"));
        g.fillOval(x + t * 0.4, y + t * 0.28, t * 0.06, t * 0.06);
        g.fillOval(x + t * 0.54, y + t * 0.28, t * 0.06, t * 0.06);
    }

    private static int clamp(int valore, int min, int max) {
        return Math.max(min, Math.min(max, valore));
    }
}
