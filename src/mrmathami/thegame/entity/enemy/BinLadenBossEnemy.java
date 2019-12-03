package mrmathami.thegame.entity.enemy;

import javafx.scene.media.AudioClip;
import mrmathami.thegame.Config;
import mrmathami.thegame.GameEntities;
import mrmathami.thegame.GameField;
import mrmathami.thegame.audio.GameAudio;
import mrmathami.thegame.entity.tile.effect.ExplosionEffect;
import mrmathami.thegame.entity.tile.tower.AbstractTower;

public class BinLadenBossEnemy extends BossEnemy {
    public BinLadenBossEnemy (long createdTick, double posX, double posY) {
        super(createdTick, posX, posY, Config.BIN_LADEN_BOSS_ENEMY_WIDTH / Config.TILE_SIZE, Config.BIN_LADEN_BOSS_ENEMY_HEIGHT / Config.TILE_SIZE, Config.BIN_LADEN_BOSS_ENEMY_HEALTH, Config.BIN_LADEN_BOSS_ENEMY_ARMOR, Config.BIN_LADEN_BOSS_ENEMY_SPEED, Config.BIN_LADEN_BOSS_ENEMY_REWARD, Config.BIN_LADEN_BOSS_ENEMY_GID);
    }

    @Override
    public void skillCheck(GameField field) {
        if (this.isDestroyed()) {
            GameAudio.getInstance().playSound(new AudioClip(GameAudio.binLadenSkillSound));
            for (AbstractTower towerEntity: GameEntities.getFilteredOverlappedEntities(field.getEntities(), AbstractTower.class, getPosX() - 1 + Config.OFFSET/Config.TILE_SIZE, getPosY() - 1 + Config.OFFSET/Config.TILE_SIZE , 3, 3)) {
                towerEntity.doDestroy();
                field.addSFX(new ExplosionEffect(0, towerEntity.getPosX(), towerEntity.getPosY()));
            }
        }
    }
}
