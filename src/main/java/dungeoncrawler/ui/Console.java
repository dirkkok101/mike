package dungeoncrawler.ui;

import java.util.function.Consumer;

public interface Console {
    void print(String text);
    void println(String text);
    void printf(String format, Object... args);
    String readLine();
    char readChar();
    void clear();
    void setKeyListener(Consumer<Character> listener);
}
