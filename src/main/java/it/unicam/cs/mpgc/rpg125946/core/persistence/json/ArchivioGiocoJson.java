package it.unicam.cs.mpgc.rpg125946.core.persistence.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.rpg125946.core.engine.StatoGioco;
import it.unicam.cs.mpgc.rpg125946.core.persistence.ArchivioGioco;
import it.unicam.cs.mpgc.rpg125946.core.persistence.MapperStatoGioco;
import it.unicam.cs.mpgc.rpg125946.core.persistence.EccezionePersistenza;
import it.unicam.cs.mpgc.rpg125946.core.persistence.dto.DatiSalvataggio;
import it.unicam.cs.mpgc.rpg125946.core.world.FabbricaMappe;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementazione di {@link ArchivioGioco} che memorizza la partita in un file JSON leggibile.
 * <p>
 * Ha l'unica responsabilità dell'I/O: delega la traduzione dominio/DTO al {@link MapperStatoGioco}
 * e la serializzazione a Gson. Il percorso del file e la fabbrica delle mappe sono iniettati dal
 * costruttore, così la classe non "cabla" dipendenze e resta facile da testare (basta un percorso
 * temporaneo).
 */
public final class ArchivioGiocoJson implements ArchivioGioco {

    private final Path fileSalvataggio;
    private final Gson gson;
    private final MapperStatoGioco mapper;

    public ArchivioGiocoJson(Path fileSalvataggio, FabbricaMappe fabbricaMappe) {
        this.fileSalvataggio = Objects.requireNonNull(fileSalvataggio, "fileSalvataggio");
        this.mapper = new MapperStatoGioco(fabbricaMappe);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /** Percorso predefinito del salvataggio, nella cartella utente. */
    public static Path fileSalvataggioDefault() {
        return Paths.get(System.getProperty("user.home"), ".rpg125946", "salvataggio.json");
    }

    @Override
    public void salva(StatoGioco stato) {
        DatiSalvataggio dati = mapper.aDati(stato);
        try {
            if (fileSalvataggio.getParent() != null) {
                Files.createDirectories(fileSalvataggio.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(fileSalvataggio, StandardCharsets.UTF_8)) {
                gson.toJson(dati, writer);
            }
        } catch (IOException e) {
            throw new EccezionePersistenza("Impossibile salvare la partita su " + fileSalvataggio, e);
        }
    }

    @Override
    public Optional<StatoGioco> carica() {
        if (!esisteSalvataggio()) {
            return Optional.empty();
        }
        try (Reader reader = Files.newBufferedReader(fileSalvataggio, StandardCharsets.UTF_8)) {
            DatiSalvataggio dati = gson.fromJson(reader, DatiSalvataggio.class);
            if (dati == null) {
                return Optional.empty();
            }
            return Optional.of(mapper.daDati(dati));
        } catch (IOException | RuntimeException e) {
            throw new EccezionePersistenza("Salvataggio non valido o illeggibile: " + fileSalvataggio, e);
        }
    }

    @Override
    public boolean esisteSalvataggio() {
        return Files.isRegularFile(fileSalvataggio);
    }

    @Override
    public void elimina() {
        try {
            Files.deleteIfExists(fileSalvataggio);
        } catch (IOException e) {
            throw new EccezionePersistenza("Impossibile eliminare il salvataggio: " + fileSalvataggio, e);
        }
    }
}
