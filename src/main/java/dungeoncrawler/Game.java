package dungeoncrawler;

import dungeoncrawler.commands.*;
import dungeoncrawler.ui.Console;
import dungeoncrawler.ui.GameConsole;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private Player player;
    private GameMap map;
    private List<Enemy> enemies;
    private boolean isRunning;
    private Console console;
    private int currentLevel;
    private Map<String, GameCommand> commands;
    private CharacterClass characterClass;

    public Game() {
        this(new GameConsole());
    }

    public Game(Console console) {
        this.console = console;
        this.enemies = new ArrayList<>();
        this.currentLevel = 1;
        initializeCommands();
    }

    private void initializeCommands() {
        commands = new HashMap<>();
        commands.put("w", new MoveCommand(this, Direction.UP));
        commands.put("s", new MoveCommand(this, Direction.DOWN));
        commands.put("a", new MoveCommand(this, Direction.LEFT));
        commands.put("d", new MoveCommand(this, Direction.RIGHT));
        commands.put("i", new ShowInventoryCommand(this));
        commands.put("e", new AttackCommand(this));
        commands.put("n", new NextLevelCommand(this));
        commands.put("esc", new QuitCommand(this));
    }

    public void start() {
        if (console instanceof GameConsole) {
            ((GameConsole) console).setVisible(true);
            ((GameConsole) console).clearOutput();
            ((GameConsole) console).clearMessageLog();
        }
        showWelcome();
        selectClass();
        initializeGame();
        gameLoop();
    }

    private void showWelcome() {
        if (console instanceof GameConsole) {
            ((GameConsole) console).printToOutput("Welcome to Dungeon Crawler!");
            ((GameConsole) console).printToOutput("Your bizarre adventure starts with an educational decision...\n");
        } else {
            console.println("Welcome to Dungeon Crawler!");
            console.println("Your bizarre adventure starts with an educational decision...\n");
        }
    }

    private void selectClass() {
        if (console instanceof GameConsole) {
            GameConsole gameConsole = (GameConsole) console;
            gameConsole.printToOutput("Choose your class:");
            gameConsole.printToOutput("\n=== Warrior (W) ===");
            gameConsole.printToOutput(AsciiArt.WARRIOR);
            gameConsole.printToOutput("• HP: 120");
            gameConsole.printToOutput("• Swing (10-25 damage)");
            gameConsole.printToOutput("• Slash (5-25 damage)");
            gameConsole.printToOutput("• Special: High HP");

            gameConsole.printToOutput("\n=== Rogue (R) ===");
            gameConsole.printToOutput(AsciiArt.ROGUE);
            gameConsole.printToOutput("• HP: 80");
            gameConsole.printToOutput("• Stab (15-35 damage)");
            gameConsole.printToOutput("• Slash (8-28 damage)");
            gameConsole.printToOutput("• Special: 20% Dodge\n");
        } else {
            console.println("Choose your class:");
            console.println("\n=== Warrior (W) ===");
            console.println(AsciiArt.WARRIOR);
            console.println("• HP: 120");
            console.println("• Swing (10-25 damage)");
            console.println("• Slash (5-25 damage)");
            console.println("• Special: High HP");

            console.println("\n=== Rogue (R) ===");
            console.println(AsciiArt.ROGUE);
            console.println("• HP: 80");
            console.println("• Stab (15-35 damage)");
            console.println("• Slash (8-28 damage)");
            console.println("• Special: 20% Dodge\n");
        }

        while (true) {
            if (console instanceof GameConsole) {
                ((GameConsole) console).printToOutput("Press W for Warrior or R for Rogue: ");
            } else {
                console.println("Press W for Warrior or R for Rogue: ");
            }
            
            char choice = String.valueOf(console.readChar()).toLowerCase().charAt(0);
            
            if (choice == 'w') {
                characterClass = CharacterClass.WARRIOR;
                console.println("\n[+] You have chosen the Warrior class!");
                break;
            } else if (choice == 'r') {
                characterClass = CharacterClass.ROGUE;
                console.println("\n[+] You have chosen the Rogue class!");
                break;
            }
            console.println("\n[-] Invalid choice. Please try again.");
        }
    }

    private void initializeGame() {
        isRunning = true;
        
        // Create map
        map = new GameMap(20, 20);
        
        // Create player if not exists
        if (player == null) {
            player = new Player(characterClass);
            Position startPos = map.getRandomEmptyPosition();
            player.setPosition(startPos);
            map.placeCharacter(player);
            
            if (console instanceof GameConsole) {
                ((GameConsole) console).setPlayer(player);
                ((GameConsole) console).clearOutput();
                ((GameConsole) console).clearMessageLog();
            }
        }
        
        // Spawn enemies
        spawnEnemies();
        
        // Initial render
        map.render(console);
        console.println("\n[!] Use WASD to move, E to attack, I for inventory, N for next level");
    }

    private void spawnEnemies() {
        int numEnemies = currentLevel + 2;
        List<Position> enemyPositions = map.getRandomEmptyPositions(numEnemies);
        
        enemies.clear();
        for (Position pos : enemyPositions) {
            Enemy enemy = new Enemy();
            enemy.setPosition(pos);
            enemies.add(enemy);
            map.placeCharacter(enemy);
        }
    }

    private void gameLoop() {
        console.setKeyListener(this::handleKeyPress);
        if (console instanceof GameConsole) {
            ((GameConsole) console).clearOutput();
            map.render(console);
        }
        
        while (isRunning) {
            map.render(console);
            if (console instanceof GameConsole) {
                ((GameConsole) console).updateAdditionalInfo();
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        console.println("Thanks for playing!");
    }

    private void handleKeyPress(char key) {
        String keyStr = String.valueOf(key).toLowerCase();
        if (key == 27) { // ESC key
            keyStr = "esc";
        }
        
        GameCommand command = commands.get(keyStr);
        if (command != null) {
            command.execute();
            if (console instanceof GameConsole) {
                ((GameConsole) console).updateAdditionalInfo();
            }
        }
    }

    // Getters and setters
    public Player getPlayer() { return player; }
    public GameMap getMap() { return map; }
    public void setMap(GameMap map) { this.map = map; }
    public List<Enemy> getEnemies() { return enemies; }
    public Console getConsole() { return console; }
    public boolean isRunning() { return isRunning; }
    public void setRunning(boolean running) { this.isRunning = running; }
    public int getCurrentLevel() { return currentLevel; }
    public void incrementLevel() { currentLevel++; }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
