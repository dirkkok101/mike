package dungeoncrawler;

import dungeoncrawler.combat.Attack;
import dungeoncrawler.items.HealthPotion;
import dungeoncrawler.items.Item;
import java.util.ArrayList;
import java.util.List;

public class Player extends Character {
    private final CharacterClass characterClass;
    private final List<Attack> attacks;
    private final List<Item> inventory;
    private int gold;
    private int healthPotions;

    public Player(CharacterClass characterClass) {
        super(characterClass == CharacterClass.WARRIOR ? 120 : 80); // Warriors have more HP
        this.characterClass = characterClass;
        this.inventory = new ArrayList<>();
        this.attacks = initializeAttacks();
        this.gold = 0;
        this.healthPotions = 2; // Start with 2 health potions
    }

    private List<Attack> initializeAttacks() {
        List<Attack> attackList = new ArrayList<>();
        
        if (characterClass == CharacterClass.ROGUE) {
            // Rogue specializes in precise strikes with high crit chance
            attackList.add(new Attack("Stab", 15, 35, 0.25, 2.0));  // High damage, 25% crit for x2
            attackList.add(new Attack("Slash", 8, 28, 0.15, 1.5));  // Medium damage, 15% crit for x1.5
            attackList.add(new Attack("Quick Strike", 5, 15, 0.40, 1.8)); // Low damage but 40% crit
        } else {
            // Warrior has strong consistent damage but lower crit
            attackList.add(new Attack("Swing", 10, 25, 0.10, 1.5));
            attackList.add(new Attack("Slash", 5, 25, 0.15, 1.5));
            attackList.add(new Attack("Heavy Strike", 15, 30, 0.05, 2.0));
        }
        
        return attackList;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void useItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            Item item = inventory.get(index);
            item.use(this);
            if (item instanceof HealthPotion) {
                inventory.remove(index);
            }
        }
    }

    public void addHealthPotion() {
        healthPotions++;
    }

    public int getHealthPotions() {
        return healthPotions;
    }

    public boolean useHealthPotion() {
        if (healthPotions > 0) {
            healthPotions--;
            heal(30); // Restore 30 HP
            return true;
        }
        return false;
    }

    public void heal(int amount) {
        setCurrentHealth(Math.min(getCurrentHealth() + amount, getMaxHealth()));
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    public int getGold() {
        return gold;
    }

    public List<Attack> getAttacks() {
        return attacks;
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    @Override
    public char getSymbol() {
        return '@';
    }
}
