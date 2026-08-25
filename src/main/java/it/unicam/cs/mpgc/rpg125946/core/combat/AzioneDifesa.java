package it.unicam.cs.mpgc.rpg125946.core.combat;


public final class AzioneDifesa implements AzioneCombattimento {

    @Override
    public String esegui(Combattimento combattimento) {
        combattimento.giocatoreInDifesa();
        return combattimento.giocatore().nome() + " si mette in guardia.";
    }
}
