package it.unicam.cs.mpgc.rpg125946.core.model.stats;


public final class ProgressioneStandard implements Progressione {

    private static final int PE_BASE = 20;
    private static final BonusLivello BONUS = new BonusLivello(10, 5, 2, 1);

    @Override
    public int esperienzaProssimoLivello(int livelloAttuale) {
        return PE_BASE * livelloAttuale;
    }

    @Override
    public BonusLivello bonusPerLivello(int livelloRaggiunto) {
        return BONUS;
    }
}
