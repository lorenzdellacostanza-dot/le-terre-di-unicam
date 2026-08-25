⭐ Le Terre di Unicam  RPG (matricola 125946)


 
 
 <img width="621" height="498" alt="{37CA97AB-38C2-43C6-9B7D-7C324855464B}" src="https://github.com/user-attachments/assets/886401e9-804d-4fc5-bf1f-0176835d7dbf" />





Piccolo gioco di ruolo a turni con interfaccia grafica JavaFX, sviluppato per il corso di
**Metodologie di Programmazione** (Università di Camerino, A.A. 2025/26).

Il giocatore esplora un villaggio visto dall'alto, dialoga con un personaggio non giocante,
combatte slime e goblin a turni, raccoglie oro ed esperienza, sale di livello e può salvare e
riprendere la partita.

🎗️ Requisiti

- **Git** (per clonare il repository).
- Una connessione a Internet alla **prima** esecuzione: Gradle scarica da solo la propria
  distribuzione, le librerie e  se non è già presente un **JDK 25**.

Non è necessario installare manualmente né Gradle né Java: ci pensa il *Gradle Wrapper* incluso nel
progetto, insieme al plugin che provvede automaticamente alla toolchain Java 25.

🎮 Come eseguire

Da terminale, nella cartella del progetto:

```bash
# Avvia il gioco
./gradlew run

# Compila e verifica il progetto
./gradlew build
```

Su Windows si usano gli stessi comandi con `gradlew.bat` al posto di `./gradlew`:

```bat
gradlew.bat run
gradlew.bat build
```

> La prima esecuzione può richiedere qualche minuto (download di Gradle 9.1.0, di JavaFX 25 e,
> se serve, del JDK 25). Le esecuzioni successive sono immediate.

## Comandi di gioco

- **Movimento:** `W` `A` `S` `D` oppure le frecce direzionali.
- **Dialoghi:** `Spazio` / `Invio` per proseguire, `Esc` per chiudere.
- **Combattimento e menu:** si usano i pulsanti a schermo.

Il salvataggio viene scritto nella cartella personale dell'utente, in
`~/.rpg125946/salvataggio.json`.

## Struttura del progetto

```
src/main/java/it/unicam/cs/mpgc/rpg125946/
├── core/        logica di gioco, del tutto indipendente da JavaFX
│   ├── model/       personaggi, statistiche, oggetti, geometria
│   ├── world/       mappe e loro costruzione
│   ├── combat/      combattimento a turni (azioni + comportamenti nemici)
│   ├── engine/      motore di esplorazione e stato di gioco
│   └── persistence/ salvataggio/caricamento (interfaccia + implementazione JSON)
└── ui/          interfaccia grafica JavaFX (l'unico strato che dipende da JavaFX)
    ├── controller/  controllori MVC + contratti delle viste (senza JavaFX)
    ├── screen/      le schermate, cioè le viste
    ├── render/      disegno del mondo su Canvas
    └── hud/         pannello delle statistiche
```

L'interfaccia è organizzata secondo il pattern **MVC (Model-View-Controller)**: il *Model* è il
package `core`, le *View* sono le schermate e i *Controller* stanno in `ui/controller`.

La documentazione completa (funzionalità, responsabilità delle classi, organizzazione dei dati e
meccanismi di estensione) si trova nella **Wiki** del repository GitHub.

## Autore

Matricola **125946**  corso di Metodologie di Programmazione, Università di Camerino.
