package dungeoncrawler.commands;

import dungeoncrawler.Game;
import dungeoncrawler.items.Item;
import java.util.List;

public class ShowInventoryCommand implements GameCommand {
    private final Game game;

    public ShowInventoryCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        List<Item> inventory = game.getPlayer().getInventory();
        game.getConsole().println("\n=== Inventory ===");
        game.getConsole().println("Gold: " + game.getPlayer().getGold());
        if (inventory.isEmpty()) {
            game.getConsole().println("Empty");
        } else {
            for (int i = 0; i < inventory.size(); i++) {
                game.getConsole().println((i + 1) + ". " + inventory.get(i));
            }
        }
        game.getConsole().println("");
    }
}
