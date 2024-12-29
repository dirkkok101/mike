package dungeoncrawler.ui;

import dungeoncrawler.Player;
import dungeoncrawler.items.HealthPotion;
import dungeoncrawler.items.Item;

import javax.swing.*;
import java.awt.*;

public class AdditionalMessageLog extends JPanel {
    private final JLabel goldLabel;
    private final JLabel potionsLabel;
    private final JLabel healthLabel;
    private final Player player;

    public AdditionalMessageLog(Player player) {
        this.player = player;
        setLayout(new GridLayout(3, 1));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        healthLabel = new JLabel();
        healthLabel.setForeground(Color.GREEN);
        healthLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));

        goldLabel = new JLabel();
        goldLabel.setForeground(Color.YELLOW);
        goldLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));

        potionsLabel = new JLabel();
        potionsLabel.setForeground(Color.RED);
        potionsLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));

        add(healthLabel);
        add(goldLabel);
        add(potionsLabel);

        update();
    }

    public void update() {
        healthLabel.setText(String.format("Health: %d/%d", player.getCurrentHealth(), player.getMaxHealth()));
        goldLabel.setText(String.format("Gold: %d", player.getGold()));
        
        // Count health potions
        int potionCount = 0;
        for (Item item : player.getInventory()) {
            if (item instanceof HealthPotion) {
                potionCount++;
            }
        }
        potionsLabel.setText(String.format("Health Potions: %d", potionCount));
    }
}
