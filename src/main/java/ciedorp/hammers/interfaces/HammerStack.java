package ciedorp.hammers.interfaces;

public interface HammerStack {

    public int getSize();
    public void setSize(int size);
    public boolean upgradeSize();

    public int getHammerDurability();
    public void setHammerDurability(int durability);
    public boolean upgradeHammerDurability();
}
