package mrmathami.thegame.drawer.Entity.Bullet;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import mrmathami.thegame.Config;
import mrmathami.thegame.drawer.Entity.EntityDrawer;
import mrmathami.thegame.drawer.Entity.GameDrawer;
import mrmathami.thegame.entity.GameEntity;
import mrmathami.thegame.entity.bullet.StopSignBullet;
import mrmathami.thegame.entity.tile.tower.RobotPoliceTower;

import javax.annotation.Nonnull;

public final class StopSignBulletDrawer implements EntityDrawer {

    @Override
    public void draw(long tickCount, @Nonnull GraphicsContext graphicsContext, @Nonnull GameEntity entity, double screenPosX, double screenPosY, double screenWidth, double screenHeight, double zoom) {
//		graphicsContext.setStroke(Color.RED);
//        graphicsContext.setLineWidth(4);
//        graphicsContext.strokeRect(screenPosX, screenPosY, Config.STOP_SIGN_BULLET_WIDTH, Config.STOP_SIGN_BULLET_HEIGHT);
        screenPosX += Config.OFFSET;
        screenPosY += Config.OFFSET;

        Image img = GameDrawer.getSheetImage();
        int maxTileWidth = (int)Math.round(img.getWidth()/ Config.TILE_SIZE);
        int maxTileHeight = (int)Math.round(img.getHeight()/Config.TILE_SIZE);
        PixelReader reader = img.getPixelReader();
        WritableImage bulletImage = new WritableImage(reader, (((StopSignBullet)entity).getGID() - 1) % maxTileWidth * (int)(Config.TILE_SIZE), Math.round((((StopSignBullet)entity).getGID() - 1) / maxTileWidth) * (int)(Config.TILE_SIZE), (int)(Config.TILE_SIZE), (int)(Config.TILE_SIZE));
        reader = bulletImage.getPixelReader();
        WritableImage t_bulletImage = new WritableImage(reader, (int)(Config.TILE_SIZE/2 - Config.STOP_SIGN_BULLET_WIDTH/2), (int)(Config.TILE_SIZE/2 - Config.STOP_SIGN_BULLET_HEIGHT/2), (int)(Config.STOP_SIGN_BULLET_WIDTH), (int)(Config.STOP_SIGN_BULLET_HEIGHT));
        graphicsContext.drawImage(t_bulletImage, screenPosX, screenPosY);
    }
}