package dungeoncrawler.commands;

import dungeoncrawler.Combat;
import dungeoncrawler.Enemy;
import dungeoncrawler.Game;
import dungeoncrawler.Position;
import dungeoncrawler.ui.GameConsole;

public class AttackCommand implements GameCommand {
    private final Game game;

    public AttackCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        Enemy enemy = findAdjacentEnemy();
        if (enemy != null) {
            initiateCombat(enemy);
        }
    }

    private Enemy findAdjacentEnemy() {
        Position playerPos = game.getPlayer().getPosition();
        for (Enemy enemy : game.getEnemies()) {
            if (playerPos.isAdjacent(enemy.getPosition())) {
                return enemy;
            }
        }
        game.getConsole().println("No enemy in range!");
        return null;
    }

    private void initiateCombat(Enemy enemy) {
        Combat combat = new Combat(game, game.getPlayer(), enemy);
        combat.start();
        
        if (!enemy.isAlive()) {
            game.getEnemies().remove(enemy);
            game.getMap().removeCharacter(enemy);
            game.getMap().render(game.getConsole());
            
            if (game.getEnemies().isEmpty()) {
                game.getConsole().println("\nAll enemies defeated! Press N to proceed to next level.");
            }
        }
        
        if (!game.getPlayer().isAlive()) {
            game.getConsole().println("\nGame Over! You have been defeated!");
            game.setRunning(false);
        }
        
        if (game.getConsole() instanceof GameConsole) {
            ((GameConsole) game.getConsole()).updateAdditionalInfo();
        }
    }
}
