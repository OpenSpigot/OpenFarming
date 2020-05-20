package io.github.openspigot.openfarming.level;

public class FarmLevel {
    private int level, radius, expCost, ecoCost, pages;
    private boolean replant;

    public FarmLevel(int level, int radius, int expCost, int ecoCost, int pages, boolean replant) {
        this.level = level;
        this.radius = radius;
        this.expCost = expCost;
        this.ecoCost = ecoCost;
        this.pages = pages;
        this.replant = replant;
    }

    //
    // Getters
    //

    public int getLevel() {
        return level;
    }

    public int getRadius() {
        return radius;
    }

    public int getExpCost() {
        return expCost;
    }

    public int getEcoCost() {
        return ecoCost;
    }

    public int getPages() {
        return pages;
    }

    public boolean isReplant() {
        return replant;
    }
}
