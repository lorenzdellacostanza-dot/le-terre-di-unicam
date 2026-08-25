package it.unicam.cs.mpgc.rpg125946.core.model.stats;


public interface Progressione {

    int esperienzaProssimoLivello(int livelloAttuale);


    BonusLivello bonusPerLivello(int livelloRaggiunto);
}
