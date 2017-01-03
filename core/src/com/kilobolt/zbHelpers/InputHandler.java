package com.kilobolt.zbHelpers;

import com.badlogic.gdx.InputProcessor;
import com.kilobolt.gameobjects.Bird;
import com.kilobolt.gameworld.GameWorld;

public class InputHandler implements InputProcessor {
    private Bird myBird;
    private GameWorld myWorld;

    public InputHandler(GameWorld myWorld) {

        this.myWorld = myWorld;
        myBird = myWorld.getBird();
    }

    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (myWorld.isReady()){
            myWorld.start();
        }
        this.myBird.onClick();
        if (myWorld.isGameOver()||myWorld.isHighScore()){
            myWorld.restart();
        }
        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
}
