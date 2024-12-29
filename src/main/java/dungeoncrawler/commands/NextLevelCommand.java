package dungeoncrawler.commands;

import dungeoncrawler.Game;
import dungeoncrawler.Position;
import dungeoncrawler.ui.GameConsole;

public class NextLevelCommand implements GameCommand {
    private final Game game;

    public NextLevelCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        if (game.getEnemies().isEmpty()) {
            game.incrementLevel();
            initializeNextLevel();
            game.getConsole().println("[+] Welcome to level " + game.getCurrentLevel() + "!");
        }
    }

    private void initializeNextLevel() {
        // Create new map
        game.setMap(new dungeoncrawler.GameMap(20, 20));
        
        // Place player in new position
        Position startPos = game.getMap().getRandomEmptyPosition();
        game.getPlayer().setPosition(startPos);
        game.getMap().placeCharacter(game.getPlayer());
        
        // Spawn new enemies
        int numEnemies = game.getCurrentLevel() + 2;
        java.util.List<Position> enemyPositions = game.getMap().getRandomEmptyPositions(numEnemies);
        
        game.getEnemies().clear();
        for (Position pos : enemyPositions) {
            dungeoncrawler.Enemy enemy = new dungeoncrawler.Enemy();
            enemy.setPosition(pos);
            game.getEnemies().add(enemy);
            game.getMap().placeCharacter(enemy);
        }
        
        // Update UI
        game.getMap().render(game.getConsole());
        if (game.getConsole() instanceof GameConsole) {
            ((GameConsole) game.getConsole()).updateAdditionalInfo();
        }
    }
}
