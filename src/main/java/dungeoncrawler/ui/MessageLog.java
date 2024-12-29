package dungeoncrawler.ui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MessageLog extends JPanel {
    private JTextPane logArea;
    private List<String> messages;
    private static final int MAX_MESSAGES = 100;
    private Style defaultStyle;
    private Style warningStyle;
    private Style successStyle;
    private Style errorStyle;

    public MessageLog() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 0)); // Width of 300px, height will match parent
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.BLACK);

        messages = new ArrayList<>();
        
        // Create title label
        JLabel titleLabel = new JLabel("Status Messages");
        titleLabel.setForeground(Color.GREEN);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Create log area
        logArea = new JTextPane();
        logArea.setEditable(false);
        logArea.setBackground(Color.BLACK);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        logArea.setMargin(new Insets(10, 10, 10, 10));

        // Create styles
        StyleContext sc = new StyleContext();
        defaultStyle = sc.addStyle("default", null);
        StyleConstants.setForeground(defaultStyle, Color.GREEN);

        warningStyle = sc.addStyle("warning", defaultStyle);
        StyleConstants.setForeground(warningStyle, Color.YELLOW);

        successStyle = sc.addStyle("success", defaultStyle);
        StyleConstants.setForeground(successStyle, new Color(0, 255, 0)); // Bright green

        errorStyle = sc.addStyle("error", defaultStyle);
        StyleConstants.setForeground(errorStyle, Color.RED);

        // Add log area to scroll pane
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        scrollPane.getVerticalScrollBar().setBackground(Color.BLACK);
        scrollPane.getVerticalScrollBar().setForeground(Color.GREEN);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addMessage(String message) {
        messages.add(message);
        if (messages.size() > MAX_MESSAGES) {
            messages.remove(0);
        }
        updateLogArea();
    }

    private void updateLogArea() {
        StyledDocument doc = logArea.getStyledDocument();
        try {
            doc.remove(0, doc.getLength());
            for (String message : messages) {
                Style style = defaultStyle;
                if (message.startsWith("[!]")) {
                    style = warningStyle;
                } else if (message.startsWith("[+]")) {
                    style = successStyle;
                } else if (message.startsWith("[-]")) {
                    style = errorStyle;
                }
                doc.insertString(doc.getLength(), message + "\n", style);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        logArea.setCaretPosition(doc.getLength());
    }

    public void clear() {
        messages.clear();
        logArea.setText("");
    }
}
