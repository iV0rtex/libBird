package com.kilobolt.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.kilobolt.gameworld.GameRenderer;
import com.kilobolt.gameworld.GameWorld;
import com.kilobolt.zbHelpers.InputHandler;

public class GameScreen implements Screen {
    private GameWorld world;
    private GameRenderer renderer;
    private float runTime;

    public GameScreen() {
        float screenWidth = (float)Gdx.graphics.getWidth();
        float screenHeight = (float)Gdx.graphics.getHeight();
        float gameWidth = 136.0F;
        float gameHeight = screenHeight / (screenWidth / gameWidth);
        int midPointY = (int)(gameHeight / 2.0F);

        this.world = new GameWorld(midPointY);

        Gdx.input.setInputProcessor(new InputHandler(world, screenWidth/gameWidth, screenHeight/gameHeight));
        this.renderer = new GameRenderer(this.world, (int)gameHeight, midPointY);
    }

    public void show() {
        Gdx.app.log("GameScreen", "show");
    }

    public void render(float delta) {
        this.runTime += delta;
        this.world.update(delta);
        this.renderer.render(delta,this.runTime);
    }

    public void resize(int width, int height) {
        Gdx.app.log("GameScreen", "resize");
    }

    public void pause() {
        Gdx.app.log("GameScreen", "pause");
    }

    public void resume() {
        Gdx.app.log("GameScreen", "resume");
    }

    public void hide() {
        Gdx.app.log("GameScreen", "hide");
    }

    public void dispose() {
    }
}
