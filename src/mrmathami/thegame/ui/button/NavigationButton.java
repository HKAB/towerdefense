package mrmathami.thegame.ui.button;

import mrmathami.thegame.ui.AbstractButton;

public class NavigationButton extends AbstractButton {
    private String command;

    public NavigationButton (long createdTick, double assetPosX, double assetPosY, double posX, double posY, double width, double height, String command) {
        super(createdTick, assetPosX, assetPosY, posX, posY, width, height);
        this.command = command;
    }

    @Override
    public String onClick () {
        return command;
    }
}
