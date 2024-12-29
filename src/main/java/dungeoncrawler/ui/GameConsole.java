package dungeoncrawler.ui;

import dungeoncrawler.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class GameConsole extends JFrame implements Console {
    private JTextArea outputArea;
    private JTextField inputField;
    private BlockingQueue<String> inputQueue;
    private BlockingQueue<Character> charQueue;
    private Consumer<Character> keyListener;
    private Font consoleFont;
    private boolean singleCharMode;
    private MessageLog messageLog;
    private AdditionalMessageLog additionalLog;
    private MapPanel mapPanel;

    public GameConsole() {
        super("Dungeon Crawler");
        this.inputQueue = new LinkedBlockingQueue<>();
        this.charQueue = new LinkedBlockingQueue<>();
        this.singleCharMode = false;
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create a monospaced font for ASCII art
        consoleFont = new Font("Monospaced", Font.PLAIN, 14);

        // Output area
        outputArea = new JTextArea(25, 80);
        outputArea.setEditable(false);
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Color.GREEN);
        outputArea.setFont(consoleFont);
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        // Message log
        messageLog = new MessageLog();
        add(messageLog, BorderLayout.EAST);

        // Map panel
        mapPanel = new MapPanel();
        add(mapPanel, BorderLayout.WEST);

        // Additional log
        additionalLog = null;

        // Input area
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.BLACK);
        
        JLabel prompt = new JLabel("> ");
        prompt.setForeground(Color.GREEN);
        prompt.setFont(consoleFont);
        inputPanel.add(prompt, BorderLayout.WEST);

        inputField = new JTextField();
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(Color.GREEN);
        inputField.setCaretColor(Color.GREEN);
        inputField.setFont(consoleFont);
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitInput();
            }
        });
        
        // Add key listener for single character input
        inputField.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                char keyChar = Character.toLowerCase(e.getKeyChar());
                if (singleCharMode) {
                    charQueue.offer(keyChar);
                    if (keyListener != null) {
                        keyListener.accept(keyChar);
                    }
                    e.consume();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyTyped(KeyEvent e) {
                if (singleCharMode) {
                    charQueue.offer(e.getKeyChar());
                    if (keyListener != null) {
                        keyListener.accept(e.getKeyChar());
                    }
                }
            }
        });
        
        inputPanel.add(inputField, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // Handle ESC key to clear input
        InputMap inputMap = inputField.getInputMap();
        ActionMap actionMap = inputField.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clear");
        actionMap.put("clear", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputField.setText("");
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void submitInput() {
        String input = inputField.getText().trim();
        if (!input.isEmpty()) {
            inputQueue.offer(input);
            inputField.setText("");
        }
    }

    @Override
    public void println(String message) {
        // Game messages (combat, items, etc.) go to message log
        messageLog.addMessage(message);
    }

    public void printToOutput(String message) {
        // UI messages (menus, character selection) go to output area
        outputArea.append(message + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    public void clearOutput() {
        outputArea.setText("");
    }

    public void clearMessageLog() {
        messageLog.clear();
    }

    @Override
    public void print(String text) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(text);
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }

    @Override
    public void printf(String format, Object... args) {
        print(String.format(format, args));
    }

    @Override
    public void clear() {
        SwingUtilities.invokeLater(() -> outputArea.setText(""));
    }

    @Override
    public String readLine() {
        singleCharMode = false;
        inputField.setEditable(true);
        try {
            inputField.requestFocusInWindow();
            return inputQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "";
        }
    }

    @Override
    public char readChar() {
        singleCharMode = true;
        inputField.setEditable(false);
        try {
            return charQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return '\0';
        }
    }

    @Override
    public void setKeyListener(Consumer<Character> listener) {
        this.keyListener = listener;
        singleCharMode = true;
        inputField.setEditable(false);
        inputField.requestFocusInWindow();
    }

    public void setPlayer(Player player) {
        if (additionalLog != null) {
            remove(additionalLog);
        }
        additionalLog = new AdditionalMessageLog(player);
        add(additionalLog, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    public void updateMap(char[][] map) {
        mapPanel.updateMap(map);
    }

    public void updateAdditionalInfo() {
        if (additionalLog != null) {
            additionalLog.update();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameConsole console = new GameConsole();
            console.setVisible(true);
        });
    }
}
