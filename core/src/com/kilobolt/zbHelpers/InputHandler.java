package com.kilobolt.zbHelpers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.kilobolt.gameobjects.Bird;
import com.kilobolt.gameworld.GameWorld;
import com.kilobolt.ui.SimpleButton;

import java.util.ArrayList;
import java.util.List;

public class InputHandler implements InputProcessor {
    private Bird myBird;
    private GameWorld myWorld;

    private List<SimpleButton> menuButtons;
    private SimpleButton playButton;
    private float scaleFactorX;
    private float scaleFactorY;

    public InputHandler(GameWorld myWorld, float scaleFactorX, float scaleFactorY) {

        this.myWorld = myWorld;
        myBird = myWorld.getBird();
        int midPointY = myWorld.getMidPointY();

        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
        menuButtons = new ArrayList<SimpleButton>();
        playButton = new SimpleButton(136/2 - (AssetLoader.playButtonUp.getRegionWidth()/2),midPointY+50,29,16,AssetLoader.playButtonUp,AssetLoader.playButtonDown);
        menuButtons.add(playButton);
    }

    public boolean keyDown(int keycode) {
        if(keycode == Keys.SPACE){
            if(myWorld.isMenu()){
                myWorld.ready();
            }else if(myWorld.isReady()){
                myWorld.start();
            }
            myBird.onClick();
            if(myWorld.isGameOver() || myWorld.isHighScore()){
                myWorld.restart();
            }
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        //System.out.print(screenX + " " + screenY);
        if(myWorld.isMenu()){
            playButton.isTouchDown(screenX,screenY);
        }else if (myWorld.isReady()){
            myWorld.start();
        }
        this.myBird.onClick();
        if (myWorld.isGameOver()||myWorld.isHighScore()){
            myWorld.restart();
        }
        return true;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        if(myWorld.isMenu()){
            if(playButton.isTouchUp(screenX,screenY)){
                myWorld.ready();
                return true;
            }
        }
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean scrolled(int amount) {
        return false;
    }
    private int scaleX(int screenX){
        return (int) (screenX / scaleFactorX);
    }
    private int scaleY(int screenY) {
        return (int) (screenY / scaleFactorY);
    }
    public List<SimpleButton> getMenuButtons() {
        return menuButtons;
    }
}
