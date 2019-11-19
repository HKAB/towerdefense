package mrmathami.thegame;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import mrmathami.thegame.drawer.MenuDrawer;
import mrmathami.thegame.entity.UIEntity;
import mrmathami.thegame.net.MPConfig;
import mrmathami.thegame.net.MPSocketController;
import mrmathami.utilities.ThreadFactoryBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class MenuController extends AnimationTimer {
    /**
     * Advance stuff. Just don't touch me. Google me if you are curious.
     */
    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(
            new ThreadFactoryBuilder()
                    .setNamePrefix("Tick")
                    .setDaemon(true)
                    .setPriority(Thread.NORM_PRIORITY)
                    .build()
    );

    /**
     * Root. Needed for changing scene.
     */
    private Group root;

    /**
     * The screen to draw on. Just don't touch me. Google me if you are curious.
     */
    private final GraphicsContext graphicsContext;

    /**
     * Game drawer. Responsible to draw the field every tick.
     * Kinda advance, modify if you are sure about your change.
     */
    private MenuDrawer drawer;

    /**
     * Menu UI. Contains UI elements.
     */
    private MenuUI menuUI;

    /**
     * Beat-keeper Manager. Just don't touch me. Google me if you are curious.
     */
    private ScheduledFuture<?> scheduledFuture;

    /**
     * The tick value. Just don't touch me.
     * Google me if you are curious, especially about volatile.
     * WARNING: Wall of text.
     */
    private volatile long tick;

    /**
     * The constructor.
     *
     * @param graphicsContext the screen to draw on
     */
    public MenuController(GraphicsContext graphicsContext, Group root) throws FileNotFoundException {
        this.graphicsContext = graphicsContext;
        this.root = root;

        // Just a few acronyms.
        final long width = Config.TILE_HORIZONTAL;
        final long height = Config.TILE_VERTICAL;

        this.menuUI = new MenuUI("/menu/buttonConfig.dat");
        this.drawer = new MenuDrawer(graphicsContext, menuUI, "/menu/background.png", "/menu/button.png");
        drawer.setFieldViewRegion(0.0, 0.0, Config.TILE_SIZE);
    }

    /**
     * Beat-keeper. Just don't touch me.
     */
    private void tick() {
        this.tick += 1;
    }

    /**
     * A JavaFX loop.
     * Kinda advance, modify if you are sure about your change.
     *
     * @param now not used. It is a timestamp in nanosecond.
     */
    @Override
    public void handle(long now) {
        // don't touch me.
        final long currentTick = tick;
        final long startNs = System.nanoTime();

        // draw a new frame, as fast as possible.
        try {
            drawer.render();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // MSPT display. MSPT stand for Milliseconds Per Tick.
        // It means how many ms your game spend to update and then draw the game once.
        // Draw it out mostly for debug
        final double mspt = (System.nanoTime() - startNs) / 1000000.0;
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillText(String.format("MSPT: %3.2f", mspt), 0, 12);

        // if we have time to spend, do a spin
        while (currentTick == tick) Thread.onSpinWait();
    }

    /**
     * Start rendering and ticking. Just don't touch me.
     * Anything that need to initialize should be in the constructor.
     */
    @Override
    public void start() {
        // Start the beat-keeper. Start to call this::tick at a fixed rate.
        this.scheduledFuture = SCHEDULER.scheduleAtFixedRate(this::tick, 0, Config.GAME_NSPT, TimeUnit.NANOSECONDS);
        // start the JavaFX loop.
        super.start();
    }

    private void moveToGameScene() {
        scheduledFuture.cancel(true);
        stop();
        Canvas gameCanvas = new Canvas(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        GraphicsContext graphicsContext = gameCanvas.getGraphicsContext2D();
        GameController gameController = null;
        try {
            gameController = new GameController(graphicsContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gameCanvas.setOnMouseClicked(gameController::mouseClickHandler);
        gameCanvas.setOnMouseMoved(gameController::mouseMoveHandler);
        root.getChildren().clear();
        root.getChildren().add(gameCanvas);
        gameController.start();
    }

    private void moveToMPScene() {
        scheduledFuture.cancel(true);
        Canvas gameCanvas = new Canvas(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);

        this.graphicsContext.setFill(Color.rgb(0, 0, 0, 0.5));
        this.graphicsContext.fillRect(0.0, 0.0, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        this.graphicsContext.fillText("Connecting to server...", Config.SCREEN_WIDTH/2.0, Config.SCREEN_HEIGHT/2.0);
        stop();

        // TODO: Replace later when player can decide which host to connect to.
        try {
            MPSocketController socket = new MPSocketController(MPConfig.DEFAULT_SERVER_HOST, MPConfig.DEFAULT_LISTEN_PORT);
        } catch (IOException e) {
            MPSocketController socket = new MPSocketController();
        }

        GraphicsContext graphicsContext = gameCanvas.getGraphicsContext2D();
        MPGameController gameController = null;
        try {
            gameController = new MPGameController(graphicsContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gameCanvas.setOnMouseClicked(gameController::mouseClickHandler);
        gameCanvas.setOnMouseMoved(gameController::mouseMoveHandler);
        root.getChildren().clear();
        root.getChildren().add(gameCanvas);
        gameController.start();
    }

    /**
     * On window close request.
     * Kinda advance, modify if you are sure about your change.
     *
     * @param windowEvent currently not used
     */
    final void closeRequestHandler(WindowEvent windowEvent) {
        scheduledFuture.cancel(true);
        stop();
        Platform.exit();
        System.exit(0);
    }

    /**
     * Key down handler.
     *
     * @param keyEvent the key that you press down
     */
    final void keyDownHandler(KeyEvent keyEvent) { }

    /**
     * Key up handler.
     *
     * @param keyEvent the key that you release up.
     */
    final void keyUpHandler(KeyEvent keyEvent) { }

    /**
     * Mouse down handler.
     *
     * @param mouseEvent the mouse button you press down.
     */
    final void mouseDownHandler(MouseEvent mouseEvent) { }

    /**
     * Mouse up handler.
     *
     * @param mouseEvent the mouse button you release up.
     */
    final void mouseUpHandler(MouseEvent mouseEvent) { }

    final void mouseMoveHandler(MouseEvent mouseEvent) {
        Collection<UIEntity> UIEntities = this.menuUI.getEntities();
        double mousePosX = mouseEvent.getX();
        double mousePosY = mouseEvent.getY();

        for (UIEntity entity: UIEntities) {
            double startX = (entity.getPosX() - drawer.getFieldStartPosX()) * drawer.getFieldZoom();
            double startY = (entity.getPosY() - drawer.getFieldStartPosY()) * drawer.getFieldZoom();
            double endX = startX + entity.getWidth() * drawer.getFieldZoom();
            double endY = startY + entity.getHeight() * drawer.getFieldZoom();
            if (Double.compare(mousePosX, startX) >= 0 && Double.compare(mousePosX, endX) <= 0
                    && Double.compare(mousePosY, startY) >= 0 && Double.compare(mousePosY, endY) <= 0) {
                entity.onFocus();
            } else {
                entity.outFocus();
            }
        }
    }

    final void mouseClickHandler(MouseEvent mouseEvent) {
        Collection<UIEntity> UIEntities = this.menuUI.getEntities();
        double mousePosX = mouseEvent.getX();
        double mousePosY = mouseEvent.getY();

        for (UIEntity entity: UIEntities) {
            double startX = (entity.getPosX() - drawer.getFieldStartPosX()) * drawer.getFieldZoom();
            double startY = (entity.getPosY() - drawer.getFieldStartPosY()) * drawer.getFieldZoom();
            double endX = startX + entity.getWidth() * drawer.getFieldZoom();
            double endY = startY + entity.getHeight() * drawer.getFieldZoom();
            if (Double.compare(mousePosX, startX) >= 0 && Double.compare(mousePosX, endX) <= 0
                    && Double.compare(mousePosY, startY) >= 0 && Double.compare(mousePosY, endY) <= 0) {
                final String command = entity.onClick();
                switch (command) {
                    case "SinglePlayerButton":
                        moveToGameScene();
                        break;
                    case "MultiPlayerButton":
                        moveToMPScene();
                        break;
                    case "SettingsButton":
                        break;
                    case "CreditsButton":
                        break;
                }
            }
        }
    }
}
