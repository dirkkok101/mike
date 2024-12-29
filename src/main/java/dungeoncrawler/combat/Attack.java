package dungeoncrawler.combat;

public class Attack {
    private final String name;
    private final int minDamage;
    private final int maxDamage;
    private final double criticalChance;
    private final double criticalMultiplier;

    public Attack(String name, int minDamage, int maxDamage, double criticalChance, double criticalMultiplier) {
        this.name = name;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.criticalChance = criticalChance;
        this.criticalMultiplier = criticalMultiplier;
    }

    public String getName() {
        return name;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public double getCriticalChance() {
        return criticalChance;
    }

    public double getCriticalMultiplier() {
        return criticalMultiplier;
    }

    @Override
    public String toString() {
        return name + " (" + minDamage + "-" + maxDamage + " damage)";
    }
}
