package mrmathami.thegame.ui.ingame.context;

public class NormalUIContext extends AbstractUIContext {
    private long money;
    private long targetHealth;
    private long currentWave;
    private long countdown;

    public NormalUIContext (long createdTick, double[] pos, long money, long targetHealth, long currentWave, long countdown) {
        super(createdTick, pos);
        this.money = money;
        this.targetHealth = targetHealth;
        this.currentWave = currentWave;
        this.countdown = countdown;
    }

    public void fieldUpdate(long money, long targetHealth, long currentWave, long countdown) {
        this.money = money;
        this.targetHealth = targetHealth;
        this.currentWave = currentWave;
        this.countdown = countdown;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getTargetHealth() {
        return targetHealth;
    }

    public void setTargetHealth(long targetHealth) {
        this.targetHealth = targetHealth;
    }

    public long getCurrentWave() {
        return currentWave;
    }

    public void setCurrentWave(long currentWave) {
        this.currentWave = currentWave;
    }

    public long getCountdown() {
        return countdown;
    }

    public void setCountdown(long countdown) {
        this.countdown = countdown;
    }
}
