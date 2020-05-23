package org.openspigot.openfarming.level;

public class FarmLevel {
    private int level, radius, expCost, ecoCost, pages;
    private double speed;
    private boolean replant;

    public FarmLevel(int level, int radius, double speed, int expCost, int ecoCost, int pages, boolean replant) {
        this.level = level;
        this.radius = radius;
        this.speed = speed;
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

    public double getSpeed() {
        return speed;
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
