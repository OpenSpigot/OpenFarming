package org.openspigot.openfarming.farm.upgrade.type;

public class FarmUpgradeLevel<T> {
    private final int level;
    private final T value;
    private final Integer ecoCost;
    private final Integer expCost;

    public FarmUpgradeLevel(int level, T value, Integer ecoCost, Integer expCost) {
        this.level = level;
        this.value = value;
        this.ecoCost = ecoCost;
        this.expCost = expCost;
    }

    //
    // Getters
    //

    public int getLevel() {
        return level;
    }

    public T getValue() {
        return value;
    }

    public Integer getEcoCost() {
        return ecoCost;
    }

    public Integer getExpCost() {
        return expCost;
    }
}
