package dungeoncrawler;

import dungeoncrawler.combat.Attack;
import dungeoncrawler.ui.Console;
import java.util.List;
import java.util.Random;

public class Combat {
    private final Player player;
    private final Enemy enemy;
    private final Console console;
    private final Random random;
    private final Game game;
    private static final double ROGUE_DODGE_CHANCE = 0.20; // 20% chance to dodge

    public Combat(Game game, Player player, Enemy enemy) {
        this.game = game;
        this.player = player;
        this.enemy = enemy;
        this.console = game.getConsole();
        this.random = new Random();
    }

    public boolean start() {
        console.println("\n[!] Combat started!");
        
        while (true) {
            // Player turn
            showCombatStatus();
            Attack selectedAttack = playerTurn();
            if (selectedAttack == null) return false; // Player chose to flee
            
            int damage = calculateDamage(selectedAttack);
            boolean isCritical = random.nextDouble() < selectedAttack.getCriticalChance();
            
            if (isCritical) {
                damage *= selectedAttack.getCriticalMultiplier();
                console.println("[+] Critical hit!");
            }
            
            enemy.takeDamage(damage);
            console.println("[+] You " + selectedAttack.getName().toLowerCase() + " the enemy for " + damage + " damage!");
            
            if (!enemy.isAlive()) {
                dropLoot();
                return true; // Player won
            }

            // Enemy turn
            int enemyDamage = random.nextInt(15) + 5; // Enemy deals 5-20 damage
            
            // Check for rogue dodge
            if (player.getCharacterClass() == CharacterClass.ROGUE && random.nextDouble() < ROGUE_DODGE_CHANCE) {
                console.println("[+] You dodged the attack!");
            } else {
                player.takeDamage(enemyDamage);
                console.println("[-] Enemy hits you for " + enemyDamage + " damage!");
                
                if (!player.isAlive()) {
                    console.println("\n[-] You have been defeated!");
                    return false;
                }
            }
        }
    }

    private void showCombatStatus() {
        console.println("\nPlayer HP: " + player.getCurrentHealth() + "/" + player.getMaxHealth());
        console.println("Enemy HP: " + enemy.getCurrentHealth() + "/" + enemy.getMaxHealth());
    }

    private Attack playerTurn() {
        List<Attack> attacks = player.getAttacks();
        console.println("\nYour turn! Choose your attack:");
        for (int i = 0; i < attacks.size(); i++) {
            Attack attack = attacks.get(i);
            console.println((i + 1) + ") " + attack.getName() + 
                          " (" + attack.getMinDamage() + "-" + attack.getMaxDamage() + " damage)");
        }
        console.println("H) Use Health Potion (" + player.getHealthPotions() + " remaining)");
        console.println("F) Flee");

        while (true) {
            char choice = String.valueOf(console.readChar()).toLowerCase().charAt(0);
            
            if (choice == 'h') {
                if (player.useHealthPotion()) {
                    console.println("[+] Used a health potion. Health restored!");
                    if (game.getConsole() instanceof dungeoncrawler.ui.GameConsole) {
                        ((dungeoncrawler.ui.GameConsole) game.getConsole()).updateAdditionalInfo();
                    }
                    return playerTurn(); // Choose attack after using potion
                } else {
                    console.println("[-] No health potions remaining!");
                    return playerTurn();
                }
            } else if (choice == 'f') {
                console.println("You fled from combat!");
                return null;
            } else {
                try {
                    int attackIndex = Integer.parseInt(String.valueOf(choice)) - 1;
                    if (attackIndex >= 0 && attackIndex < attacks.size()) {
                        return attacks.get(attackIndex);
                    }
                } catch (NumberFormatException e) {
                    // Invalid input, will show error message
                }
            }
            console.println("Invalid choice. Try again.");
        }
    }

    private int calculateDamage(Attack attack) {
        return random.nextInt(attack.getMaxDamage() - attack.getMinDamage() + 1) + attack.getMinDamage();
    }

    private void dropLoot() {
        console.println("\n[+] Enemy defeated!");
        
        // Drop gold
        int goldAmount = random.nextInt(20) + 10; // 10-30 gold
        player.addGold(goldAmount);
        console.println("[+] Found " + goldAmount + " gold!");
        
        // Chance to drop health potion
        if (random.nextDouble() < 0.3) { // 30% chance
            player.addHealthPotion();
            console.println("[+] Found a health potion!");
        }
        
        if (game.getConsole() instanceof dungeoncrawler.ui.GameConsole) {
            ((dungeoncrawler.ui.GameConsole) game.getConsole()).updateAdditionalInfo();
        }
    }
}
