package dungeoncrawler;

public class Enemy extends Character {
    private static final int BASE_HEALTH = 50;

    public Enemy() {
        super(BASE_HEALTH);
    }

    @Override
    public char getSymbol() {
        return 'E';
    }

    public boolean isAlive() {
        return getCurrentHealth() > 0;
    }
}
