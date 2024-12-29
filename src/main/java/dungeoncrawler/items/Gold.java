package dungeoncrawler.items;

import dungeoncrawler.Player;

public class Gold extends Item {
    private final int amount;

    public Gold(int amount) {
        super("Gold", amount + " gold pieces");
        this.amount = amount;
    }

    @Override
    public void use(Player player) {
        player.addGold(amount);
        System.out.println("[+] Added " + amount + " gold to your purse!");
    }

    public int getAmount() {
        return amount;
    }
}
