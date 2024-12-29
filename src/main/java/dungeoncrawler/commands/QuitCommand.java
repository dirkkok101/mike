package dungeoncrawler.commands;

import dungeoncrawler.Game;

public class QuitCommand implements GameCommand {
    private final Game game;

    public QuitCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        game.setRunning(false);
    }
}
