package dungeoncrawler.commands;

import dungeoncrawler.Direction;
import dungeoncrawler.Game;
import dungeoncrawler.Position;

public class MoveCommand implements GameCommand {
    private final Game game;
    private final Direction direction;

    public MoveCommand(Game game, Direction direction) {
        this.game = game;
        this.direction = direction;
    }

    @Override
    public void execute() {
        Position currentPos = game.getPlayer().getPosition();
        Position newPos = currentPos.add(direction.getDx(), direction.getDy());
        
        if (game.getMap().isWalkable(newPos)) {
            game.getPlayer().setPosition(newPos);
            game.getMap().render(game.getConsole());
        }
    }
}
