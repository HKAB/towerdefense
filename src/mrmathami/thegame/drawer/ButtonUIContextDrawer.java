package mrmathami.thegame.drawer;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import mrmathami.thegame.Config;
import mrmathami.thegame.entity.UIEntity;
import mrmathami.thegame.ui.ingame.context.ButtonUIContext;

import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class ButtonUIContextDrawer implements UIEntityDrawer {
    private final Map<String, Integer> ICON_GID = new HashMap<>(Map.ofEntries(
            Map.entry("money", 0),
            Map.entry("lives", 1),
            Map.entry("wave", 2),
            Map.entry("countdown", 3),
            Map.entry("firepower", 4),
            Map.entry("level", 5),
            Map.entry("speed", 6),
            Map.entry("upgrade", 8),
            Map.entry("sell", 9),
            Map.entry("buy", 10)
    ));
    private final long LINE_HEIGHT = 40;
    private final long TEXT_TAB = 30;
    private final long EDGE_SIZE = 10;
    private final long FONT_SIZE = 25;

    public void draw(long tickCount, @Nonnull GraphicsContext graphicsContext, @Nonnull UIEntity entity, double screenPosX, double screenPosY, double screenWidth, double screenHeight, double fieldZoom) throws FileNotFoundException {
        Image img = GameDrawer.getContextIconImage();
        PixelReader reader = img.getPixelReader();

        ButtonUIContext context = (ButtonUIContext) entity;

        final double leftIconPosX = screenPosX + EDGE_SIZE;
        final double rightIconPosX = screenPosX + context.getWidth() * fieldZoom / 2;
        final double leftTextPosX = leftIconPosX + TEXT_TAB;
        final double rightTextPosX = rightIconPosX + TEXT_TAB;
        double linePosY = screenPosY + EDGE_SIZE;
        WritableImage icon;

        graphicsContext.setTextAlign(TextAlignment.LEFT);
        graphicsContext.setTextBaseline(VPos.TOP);
        graphicsContext.setFont(new Font(FONT_SIZE));

        /**
         * Money
         */
        icon = new WritableImage(reader, (int)(getIconGID("money") * fieldZoom), 0, (int)Config.TILE_SIZE, (int)Config.TILE_SIZE);
        graphicsContext.drawImage(icon, leftIconPosX, linePosY);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText(context.getMoney() + "$", leftTextPosX, linePosY, 75);

        /**
         * Current wave
         */
        icon = new WritableImage(reader, (int)(getIconGID("wave") * fieldZoom), 0, (int)Config.TILE_SIZE, (int)Config.TILE_SIZE);
        graphicsContext.drawImage(icon, rightIconPosX, linePosY);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText(Long.toString(context.getCurrentWave()), rightTextPosX, linePosY, 75);

        linePosY += LINE_HEIGHT;

        /**
         * Health
         */
        icon = new WritableImage(reader, (int)(getIconGID("lives") * fieldZoom), 0, (int)Config.TILE_SIZE, (int)Config.TILE_SIZE);
        graphicsContext.drawImage(icon, leftIconPosX, linePosY);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText(Long.toString(context.getTargetHealth()), leftTextPosX, linePosY, 75);

        /**
         * Countdown to next turn
         */
        icon = new WritableImage(reader, (int)(getIconGID("countdown") * fieldZoom), 0, (int)Config.TILE_SIZE, (int)Config.TILE_SIZE);
        graphicsContext.drawImage(icon, rightIconPosX, linePosY);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText(context.getCountdown() + "s", rightTextPosX, linePosY, 75);

        linePosY += (LINE_HEIGHT + 15);

        /**
         * Price
         */
        icon = new WritableImage(reader, (int)(getIconGID("money") * fieldZoom), 0, (int) Config.TILE_SIZE, (int)Config.TILE_SIZE);
        graphicsContext.drawImage(icon, leftIconPosX, linePosY);
        if (context.getTower().getPrice() > context.getMoney()) graphicsContext.setFill(Color.RED);
        else graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText(context.getTower().getPrice() + "$", leftTextPosX, linePosY, 75);

        linePosY += LINE_HEIGHT;

        /**
         * Firepower
         */
        icon = new WritableImage(reader, (int)(getIconGID("firepower") * fieldZoom), 0, (int)Config.TILE_SIZE, (int)Config.TILE_SIZE);
        graphicsContext.drawImage(icon, leftIconPosX, linePosY);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText(Long.toString(context.getTower().getFirepower()), leftTextPosX, linePosY, 75);

        /**
         * Speed
         */
        icon = new WritableImage(reader, (int)(getIconGID("speed") * fieldZoom), 0, (int)Config.TILE_SIZE, (int)Config.TILE_SIZE);
        graphicsContext.drawImage(icon, rightIconPosX, linePosY);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText(Long.toString(context.getTower().getSpeed()), rightTextPosX, linePosY, 75);
    }

    private int getIconGID (String icon) {
        return ICON_GID.get(icon);
    }
}