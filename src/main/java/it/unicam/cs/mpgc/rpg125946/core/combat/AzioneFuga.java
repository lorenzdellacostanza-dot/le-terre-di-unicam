package it.unicam.cs.mpgc.rpg125946.core.combat;


public final class AzioneFuga implements AzioneCombattimento {

    @Override
    public String esegui(Combattimento combattimento) {
        boolean fuggito = combattimento.tentaFuga();
        return fuggito
                ? combattimento.giocatore().nome() + " fugge dal combattimento"
                : "Tentativo di fuga fallit";
    }
}
