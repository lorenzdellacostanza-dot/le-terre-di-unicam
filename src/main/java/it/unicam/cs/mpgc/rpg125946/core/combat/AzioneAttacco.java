package it.unicam.cs.mpgc.rpg125946.core.combat;

public final class AzioneAttacco implements AzioneCombattimento {

    @Override
    public String esegui(Combattimento combattimento) {
        int inflitti = combattimento.danneggiaNemico(combattimento.giocatore().potenzaAttacco());
        String messaggio = combattimento.giocatore().nome() + " attacca " + combattimento.nemico().nome()
                + " infliggendo " + inflitti + " danni.";
        if (combattimento.esito() == EsitoCombattimento.VITTORIA) {
            messaggio += " " + combattimento.nemico().nome() + " è stato sconfitto!";
        }
        return messaggio;
    }
}
