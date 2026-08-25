package it.unicam.cs.mpgc.rpg125946.core.model.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public final class Inventario {

    private final List<Oggetto> oggetti = new ArrayList<>();

    public void aggiungi(Oggetto oggetto) {
        oggetti.add(Objects.requireNonNull(oggetto, "oggetto"));
    }

    public boolean rimuovi(Oggetto oggetto) {
        return oggetti.remove(oggetto);
    }

    public boolean contiene(Oggetto oggetto) {
        return oggetti.contains(oggetto);
    }

    public boolean vuoto() {
        return oggetti.isEmpty();
    }

    public int dimensione() {
        return oggetti.size();
    }

    /** @return vista non modificabile di tutti gli oggetti, nell'ordine di inserimento. */
    public List<Oggetto> oggetti() {
        return Collections.unmodifiableList(oggetti);
    }

    /**
     * @return gli oggetti compatibili con il tipo richiesto (es. {@code diTipo(Consumabile.class)}).
     */
    public <T extends Oggetto> List<T> diTipo(Class<T> tipo) {
        List<T> risultato = new ArrayList<>();
        for (Oggetto oggetto : oggetti) {
            if (tipo.isInstance(oggetto)) {
                risultato.add(tipo.cast(oggetto));
            }
        }
        return risultato;
    }
}
