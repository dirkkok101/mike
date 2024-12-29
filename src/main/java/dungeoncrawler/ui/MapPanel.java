package dungeoncrawler.ui;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {
    private char[][] map;
    private static final int CELL_SIZE = 20;
    private static final Font MAP_FONT = new Font("Monospaced", Font.BOLD, 16);

    public MapPanel() {
        setBackground(Color.BLACK);
        setFont(MAP_FONT);
    }

    public void updateMap(char[][] newMap) {
        this.map = newMap;
        if (map != null) {
            setPreferredSize(new Dimension(
                map[0].length * CELL_SIZE,
                map.length * CELL_SIZE
            ));
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (map == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        FontMetrics fm = g2d.getFontMetrics();
        int charHeight = fm.getHeight();
        int charWidth = fm.charWidth('W');

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                char cell = map[y][x];
                switch (cell) {
                    case '@': // Player
                        g2d.setColor(Color.GREEN);
                        break;
                    case 'E': // Enemy
                        g2d.setColor(Color.RED);
                        break;
                    case '#': // Wall
                        g2d.setColor(Color.GRAY);
                        break;
                    default: // Floor
                        g2d.setColor(Color.DARK_GRAY);
                }
                
                int xPos = x * CELL_SIZE + (CELL_SIZE - charWidth) / 2;
                int yPos = y * CELL_SIZE + (CELL_SIZE + charHeight) / 2 - fm.getDescent();
                g2d.drawString(String.valueOf(cell), xPos, yPos);
            }
        }
    }
}
