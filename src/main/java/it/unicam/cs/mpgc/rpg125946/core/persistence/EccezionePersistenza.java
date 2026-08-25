package it.unicam.cs.mpgc.rpg125946.core.persistence;

/**
 * Segnala un errore durante il salvataggio o il caricamento della partita.
 * Avvolge le eccezioni tecniche (I/O, formato non valido) in un tipo del dominio della persistenza,
 * così gli strati superiori non devono conoscerne i dettagli implementativi.
 */
public class EccezionePersistenza extends RuntimeException {

    public EccezionePersistenza(String messaggio, Throwable causa) {
        super(messaggio, causa);
    }

    public EccezionePersistenza(String messaggio) {
        super(messaggio);
    }
}
