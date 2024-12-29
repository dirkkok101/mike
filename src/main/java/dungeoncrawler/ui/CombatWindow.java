package dungeoncrawler.ui;

import dungeoncrawler.Enemy;
import dungeoncrawler.Player;
import dungeoncrawler.items.HealthPotion;
import dungeoncrawler.items.Item;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class CombatWindow extends JDialog {
    private final Player player;
    private final Enemy enemy;
    private boolean playerWon;
    private final Random random = new Random();
    private static final double ROGUE_DODGE_CHANCE = 0.20; // 20% chance to dodge

    public CombatWindow(GameConsole parent, Player player, Enemy enemy) {
        super(parent, "Combat!", true);
        this.player = player;
        this.enemy = enemy;
        
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        // Status panel
        JPanel statusPanel = createStatusPanel();
        add(statusPanel, BorderLayout.NORTH);
        
        // Combat log
        JTextArea combatLog = new JTextArea(10, 40);
        combatLog.setEditable(false);
        combatLog.setBackground(Color.BLACK);
        combatLog.setForeground(Color.GREEN);
        combatLog.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(combatLog);
        add(scrollPane, BorderLayout.CENTER);
        
        // Action buttons
        JPanel buttonPanel = createButtonPanel(combatLog);
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(parent);
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBackground(Color.BLACK);
        
        JLabel playerStatus = new JLabel(String.format("Player HP: %d/%d", 
            player.getCurrentHealth(), player.getMaxHealth()));
        playerStatus.setForeground(Color.GREEN);
        
        JLabel enemyStatus = new JLabel(String.format("Enemy HP: %d/%d", 
            enemy.getCurrentHealth(), enemy.getMaxHealth()));
        enemyStatus.setForeground(Color.RED);
        
        panel.add(playerStatus);
        panel.add(enemyStatus);
        return panel;
    }

    private JPanel createButtonPanel(JTextArea combatLog) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(Color.BLACK);
        
        // Attack button
        JButton attackButton = new JButton("Attack");
        attackButton.addActionListener(e -> {
            int damage = random.nextInt(20) + 10; // 10-30 damage
            enemy.takeDamage(damage);
            combatLog.append(String.format("You attack for %d damage!\n", damage));
            
            if (enemy.getCurrentHealth() <= 0) {
                playerWon = true;
                dispose();
                return;
            }
            
            // Enemy turn
            enemyTurn(combatLog);
        });
        
        // Use potion button
        JButton potionButton = new JButton("Use Potion");
        potionButton.addActionListener(e -> {
            boolean used = false;
            for (int i = 0; i < player.getInventory().size(); i++) {
                Item item = player.getInventory().get(i);
                if (item instanceof HealthPotion) {
                    player.useItem(i);
                    used = true;
                    combatLog.append("You used a health potion!\n");
                    break;
                }
            }
            if (!used) {
                combatLog.append("No health potions available!\n");
            }
        });
        
        // Flee button
        JButton fleeButton = new JButton("Flee");
        fleeButton.addActionListener(e -> {
            playerWon = false;
            dispose();
        });
        
        panel.add(attackButton);
        panel.add(potionButton);
        panel.add(fleeButton);
        return panel;
    }

    private void enemyTurn(JTextArea combatLog) {
        int damage = random.nextInt(15) + 5; // 5-20 damage
        
        if (player.getCharacterClass() == dungeoncrawler.CharacterClass.ROGUE && 
            random.nextDouble() < ROGUE_DODGE_CHANCE) {
            combatLog.append("You skillfully dodge the enemy's attack!\n");
        } else {
            player.takeDamage(damage);
            combatLog.append(String.format("Enemy hits you for %d damage!\n", damage));
        }
        
        if (player.getCurrentHealth() <= 0) {
            playerWon = false;
            dispose();
        }
    }

    public boolean wasPlayerVictorious() {
        return playerWon;
    }
}
