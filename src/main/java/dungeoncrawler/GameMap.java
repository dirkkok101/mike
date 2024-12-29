package dungeoncrawler;

import dungeoncrawler.ui.Console;
import dungeoncrawler.ui.GameConsole;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameMap {
    private final int width;
    private final int height;
    private final char[][] tiles;
    private final Map<Position, Character> characterMap;
    private final Random random;
    private final List<Room> rooms;

    private static class Room {
        int x, y, width, height;

        Room(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        boolean overlaps(Room other) {
            return x < other.x + other.width && x + width > other.x &&
                   y < other.y + other.height && y + height > other.y;
        }
    }

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new char[height][width];
        this.characterMap = new HashMap<>();
        this.random = new Random();
        this.rooms = new ArrayList<>();
        generateMap();
    }

    private void generateMap() {
        // Initialize with walls
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[y][x] = '#';
            }
        }

        // Generate rooms
        for (int i = 0; i < 10; i++) {  // Try to place 10 rooms
            int roomWidth = random.nextInt(4) + 4;  // Width between 4-7
            int roomHeight = random.nextInt(4) + 4; // Height between 4-7
            int x = random.nextInt(width - roomWidth - 2) + 1;
            int y = random.nextInt(height - roomHeight - 2) + 1;

            Room newRoom = new Room(x, y, roomWidth, roomHeight);
            boolean overlaps = false;

            // Check if room overlaps with existing rooms
            for (Room room : rooms) {
                if (newRoom.overlaps(room)) {
                    overlaps = true;
                    break;
                }
            }

            if (!overlaps) {
                rooms.add(newRoom);
                carveRoom(newRoom);

                // Connect to previous room
                if (!rooms.isEmpty()) {
                    Room prevRoom = rooms.get(rooms.size() - 2);
                    int prevCenterX = prevRoom.x + prevRoom.width / 2;
                    int prevCenterY = prevRoom.y + prevRoom.height / 2;
                    int newCenterX = newRoom.x + newRoom.width / 2;
                    int newCenterY = newRoom.y + newRoom.height / 2;

                    carveTunnel(prevCenterX, prevCenterY, newCenterX, newCenterY);
                }
            }
        }
    }

    private void carveRoom(Room room) {
        for (int y = room.y; y < room.y + room.height; y++) {
            for (int x = room.x; x < room.x + room.width; x++) {
                tiles[y][x] = '.';
            }
        }
    }

    private void carveTunnel(int x1, int y1, int x2, int y2) {
        // Carve an L-shaped tunnel between two points
        // First go horizontally, then vertically

        // Horizontal tunnel
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            tiles[y1][x] = '.';
        }

        // Vertical tunnel
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            tiles[y][x2] = '.';
        }
    }

    public Position getRandomEmptyPosition() {
        List<Position> allEmpty = new ArrayList<>();

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (tiles[y][x] == '.' && !isOccupied(new Position(x, y))) {
                    allEmpty.add(new Position(x, y));
                }
            }
        }

        if (allEmpty.isEmpty()) {
            return null;
        }

        return allEmpty.get(random.nextInt(allEmpty.size()));
    }

    private boolean isOccupied(Position pos) {
        return characterMap.containsKey(pos);
    }

    public void placeCharacter(Character character) {
        characterMap.put(character.getPosition(), character);
    }

    public void removeCharacter(Character character) {
        characterMap.remove(character.getPosition());
    }

    public void render(Console console) {
        if (console instanceof GameConsole) {
            ((GameConsole) console).clearOutput();
        }
        
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Position pos = new Position(x, y);
                if (characterMap.containsKey(pos)) {
                    sb.append(characterMap.get(pos).getSymbol());
                } else {
                    sb.append(tiles[y][x]);
                }
                sb.append(' ');
            }
            sb.append('\n');
        }
        
        if (console instanceof GameConsole) {
            ((GameConsole) console).printToOutput(sb.toString());
        } else {
            console.println(sb.toString());
        }
    }

    public boolean isWalkable(Position position) {
        return position.getX() > 0 && position.getX() < width - 1 &&
               position.getY() > 0 && position.getY() < height - 1 &&
               tiles[position.getY()][position.getX()] == '.' &&
               !isOccupied(position);
    }

    public List<Position> getRandomEmptyPositions(int count) {
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Position pos = getRandomEmptyPosition();
            if (pos != null) {
                positions.add(pos);
            }
        }
        return positions;
    }
}
