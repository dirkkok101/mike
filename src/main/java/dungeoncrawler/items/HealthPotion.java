package dungeoncrawler.items;

import dungeoncrawler.Player;

public class HealthPotion extends Item {
    private final int healAmount;

    public HealthPotion() {
        super("Health Potion", "Restores 50% of max HP");
        this.healAmount = 50; // 50% of max HP
    }

    @Override
    public void use(Player player) {
        int healingAmount = (player.getMaxHealth() * healAmount) / 100;
        int oldHealth = player.getCurrentHealth();
        player.heal(healingAmount);
        int actualHealing = player.getCurrentHealth() - oldHealth;
        System.out.println("[+] Healed for " + actualHealing + " HP!");
    }
}
